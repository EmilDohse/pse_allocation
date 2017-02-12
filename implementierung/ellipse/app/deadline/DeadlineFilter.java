package deadline;

import java.util.HashMap;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import akka.stream.Materializer;
import controllers.IndexPageController;
import play.mvc.Call;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;

public class DeadlineFilter extends Filter {

    private HashMap<String, Call>                    beforeRegistration;

    private HashMap<String, Call>                    duringRegistration;

    private HashMap<String, Call>                    afterRegistration;

    private controllers.ReverseIndexPageController   indexController;

    private controllers.ReverseAdminPageController   adminController;

    private controllers.ReverseAdviserPageController adviserController;

    private controllers.ReverseStudentPageController studentController;

    @Inject
    public DeadlineFilter(Materializer mat) {
        super(mat);
        this.indexController = controllers.routes.IndexPageController;
        this.adminController = controllers.routes.AdminPageController;
        this.adviserController = controllers.routes.AdviserPageController;
        this.studentController = controllers.routes.StudentPageController;
        this.beforeRegistration = initBeforeRegistration();
        this.duringRegistration = initDuringRegistration();
        this.afterRegistration = initAfterRegistration();
    }

    private HashMap<String, Call> initAfterRegistration() {
        HashMap<String, Call> map = new HashMap<>();
        map.put(indexController.registerPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(indexController.register().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));

        map.put(studentController.leaveLearningGroup().path(),
                studentController.notAllowedInCurrentState(
                        studentController.learningGroupPage().path()));
        map.put(studentController.setLearningGroup().path(),
                studentController.notAllowedInCurrentState(
                        studentController.learningGroupPage().path()));
        map.put(studentController.rate().path(),
                studentController.notAllowedInCurrentState(
                        studentController.ratingPage().path()));

        map.put(controllers.routes.AdminProjectController.removeProject()
                .path(),
                adminController.notAllowedInCurrentState(
                        adminController.projectPage().path()));
        map.put(controllers.routes.AdminProjectController.addProject().path(),
                adminController.notAllowedInCurrentState(
                        adminController.projectPage().path()));

        map.put(adviserController.removeProject().path(),
                adviserController.notAllowedInCurrentState(
                        adviserController.projectsPage(-1).path()));
        map.put(adviserController.addProject().path(),
                adviserController.notAllowedInCurrentState(
                        adviserController.projectsPage(-1).path()));
        return map;
    }

    private HashMap<String, Call> initDuringRegistration() {
        HashMap<String, Call> map = new HashMap<>();
        map.put(controllers.routes.GeneralAdminController.addAllocation()
                .path(),
                adminController.notAllowedInCurrentState(
                        adminController.allocationPage().path()));
        map.put(controllers.routes.AdminProjectController.removeProject()
                .path(),
                adminController.notAllowedInCurrentState(
                        adminController.projectPage().path()));
        map.put(controllers.routes.AdminProjectController.addProject().path(),
                adminController.notAllowedInCurrentState(
                        adminController.projectPage().path()));

        map.put(adviserController.removeProject().path(),
                adviserController.notAllowedInCurrentState(
                        adviserController.projectsPage(-1).path()));
        map.put(adviserController.addProject().path(),
                adviserController.notAllowedInCurrentState(
                        adviserController.projectsPage(-1).path()));
        return map;

    }

    private HashMap<String, Call> initBeforeRegistration() {
        HashMap<String, Call> map = new HashMap<>();
        map.put(indexController.registerPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(indexController.register().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));

        map.put(studentController.changeFormPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.changeData().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));

        map.put(studentController.learningGroupPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.ratingPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.resultsPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.accountPage().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.sendNewVerificationLink().path(),
                indexController.notAllowedInCurrentState(
                        indexController.indexPage().path()));
        map.put(studentController.rate().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.editAccount().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.leaveLearningGroup().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));
        map.put(studentController.setLearningGroup().path(), indexController
                .notAllowedInCurrentState(indexController.indexPage().path()));

        map.put(controllers.routes.GeneralAdminController.addAllocation()
                .path(),
                adminController.notAllowedInCurrentState(
                        adminController.allocationPage().path()));
        map.put(controllers.routes.GeneralAdminController.addStudent().path(),
                adminController.notAllowedInCurrentState(
                        adminController.studentEditPage().path()));
        return map;

    }

    @Override
    public CompletionStage<Result> apply(
            Function<RequestHeader, CompletionStage<Result>> nextFilter,
            RequestHeader requestHeader) {
        return nextFilter.apply(requestHeader).thenApply(res -> {
            Result result;
            String path = requestHeader.path();
            Call call;
            switch (StateStorage.getInstance().getCurrentState()) {
            case BEFORE_REGISTRATION_PHASE:
                call = beforeRegistration.get(path);
                break;
            case REGISTRATION_PHASE:
                call = duringRegistration.get(path);
                break;
            case AFTER_REGISTRATION_PHASE:
                call = afterRegistration.get(path);
                break;
            default:
                call = beforeRegistration.get(path);
                break;
            }
            result = Results.redirect(call);
            return result;
        });
    }

}
