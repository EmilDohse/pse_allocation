// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

import allocation.AbstractAllocator;
import allocation.AllocationQueue;
import data.Achievement;
import data.ElipseModel;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.SMTPOptions;
import data.SPO;
import data.Semester;
import data.Student;
import deadline.StateStorage;
import exception.ValidationException;
import form.Forms;
import form.IntValidator;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

/************************************************************/
/**
 * Dieser Controller ist für das Bearbeiten der Http-Requests zuständig, welche
 * beim Ändern der Einstellungen abgeschickt werden.
 */
public class AdminPropertiesController extends Controller {

    private static final String ERROR          = "error";
    private static final String GEN_ERROR      = "index.registration.error.genError";
    private static final String INTERNAL_ERROR = "error.internalError";
    private static final String NUMBER_ERROR   = "admin.properties.numberError";

    @Inject
    FormFactory                 formFactory;

    /**
     * Diese Methode lässt den Administrator ein neues Semester erstellen und
     * anschließend konfigurieren. Nach dem Erstellen wird der Administrator
     * deshalb auf die Einstellungsseite für das Semester weitergeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addSemester() {
        Semester semester = new Semester("newSemester", true);
        semester.save();
        return redirect(controllers.routes.AdminPageController.propertiesPage());
    }

    /**
     * Diese Methode lässt den Administrator ein Semester löschen, wenn mit
     * diesem keine Studentendaten verbunden sind. Der Administrator wird
     * daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeSemester() {
        synchronized (Semester.class) {
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }

            // Hole das Semester aus der Datenbank
            String semesterIdString = form.get("id");
            int semesterId;
            try {
                semesterId = Integer.parseInt(semesterIdString);
            } catch (NumberFormatException e) {
                flash(ERROR, ctx().messages().at(INTERNAL_ERROR));
                return redirect(controllers.routes.AdminPageController
                        .propertiesPage());
            }
            Semester semester = ElipseModel.getById(Semester.class, semesterId);

            // Prüfe, dass das Semester nicht das aktuelle Semester ist
            if (!semester.equals(GeneralData.loadInstance()
                    .getCurrentSemester())) {
                for (Project p : semester.getProjects()) {
                    p.delete();
                }
                for (LearningGroup l : semester.getLearningGroups()) {
                    l.delete();
                }
                semester.delete();
            } else {
                flash(ERROR,
                        ctx().messages().at(
                                "admin.properties.semesterIsActiveError"));
            }

            return redirect(controllers.routes.AdminPageController
                    .propertiesPage());
        }
    }

    /**
     * Diese Methode fügt eine neue leere SPO, mit einem vom Administrator
     * bestimmten Namen, hinzu. Der Administrator wird daraufhin auf die
     * Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addSPO() {
        SPO spo = new SPO("newSPO");
        spo.save();
        return redirect(controllers.routes.AdminPageController.propertiesPage());
    }

    /**
     * Diese Methode löscht eine bereits vorhandene SPO. Die SPO kann nur
     * gelöscht werden, wenn kein Student diese SPO verwendet. Der Administrator
     * wird daraufhin auf die Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result removeSPO() {
        synchronized (SPO.class) {
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }
            String spoIdString = form.get("id");
            int spoId;
            try {
                spoId = new IntValidator(0).validate(spoIdString);
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(controllers.routes.AdminPageController
                        .propertiesPage());
            }
            SPO spo = ElipseModel.getById(SPO.class, spoId);
            // Prüfe, ob die SPO von einem Studenten verwendet wird.
            boolean used = false;
            for (Student s : Student.getStudents()) {
                if (s.getSPO().equals(spo)) {
                    used = true;
                    break;
                }
            }
            // Lösche die SPO, falls sie nicht verwendet wird
            if (!used) {
                for (Achievement a : spo.getAdditionalAchievements()) {
                    a.delete();
                }
                for (Achievement a : spo.getNecessaryAchievements()) {
                    a.delete();
                }
                spo.delete();
            } else {
                flash(ERROR,
                        ctx().messages().at("admin.properties.SPOusedError"));
            }
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage());
        }
    }

    /**
     * Diese Methode übernimmt die Änderungen, welche der Administrator im
     * Semester-ändern-Formular festgelegt hat. Dazu gehören die Deadlines und
     * die allgemeinen Informationen.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result editSemester() {
        synchronized (Semester.class) {
            synchronized (SPO.class) {
                DynamicForm form = formFactory.form().bindFromRequest();
                if (form.data().isEmpty()) {
                    return badRequest(ctx().messages().at(INTERNAL_ERROR));
                }
                // Prüfe alle Eingaben aus dem Formular
                String name = form.get("name2");
                String idString = form.get("id");
                String maxGroupSizeString = form.get("maxGroupSize");
                int maxGroupSize;
                int id;
                ArrayList<SPO> usedSPOs = new ArrayList<>();
                // Hole alle ausgewählten SPOs aus der Datenbank
                String[] spoIdStrings = MultiselectList.getValueArray(form,
                        "spo-multiselect-" + idString);
                for (String spoIdString : spoIdStrings) {
                    try {
                        SPO spo = ElipseModel.getById(SPO.class,
                                new IntValidator(0).validate(spoIdString));
                        if (spo == null) {
                            flash(ERROR,
                                    ctx().messages().at(SPO.CONCURRENCY_ERROR));
                            return redirect(controllers.routes.AdminPageController
                                    .propertiesPage());
                        }
                        usedSPOs.add(spo);
                    } catch (ValidationException e) {
                        flash(ERROR, ctx().messages().at(e.getMessage()));
                        return redirect(controllers.routes.AdminPageController
                                .propertiesPage());
                    }
                }

                try {
                    maxGroupSize = new IntValidator(0)
                            .validate(maxGroupSizeString);
                    id = new IntValidator(0).validate(idString);
                } catch (ValidationException e) {
                    flash(ERROR, ctx().messages().at(e.getMessage()));
                    return redirect(controllers.routes.AdminPageController
                            .propertiesPage());
                }
                String generalInfo = form.get("info");
                String registrationStart = form.get("registrationStart");
                String registrationEnd = form.get("registrationEnd");
                String wintersemester = form.get("wintersemester");
                Date startDate;
                Date endDate;
                String semesterActive = form.get("semester-active");
                // Hole die Startdaten der Phasen aus dem Formular
                try {
                    SimpleDateFormat format = new SimpleDateFormat(
                            "dd.MM.yyyy HH:mm:ss");
                    startDate = format.parse(registrationStart);
                    endDate = format.parse(registrationEnd);
                } catch (ParseException e) {
                    flash(ERROR, ctx().messages().at(GEN_ERROR));
                    return redirect(controllers.routes.AdminPageController
                            .propertiesPage());
                }

                Semester semester = ElipseModel.getById(Semester.class, id);
                // falls das semester gelöscht wurde
                if (semester == null) {
                    flash(ERROR, ctx().messages()
                            .at(Semester.CONCURRENCY_ERROR));
                    return redirect(controllers.routes.AdminPageController
                            .propertiesPage());
                }
              //falls es bereits eine allocation gibt und die zeiten geändert wurden
                if((!semester.getRegistrationStart().equals(startDate) || !semester.getRegistrationEnd().equals(endDate)) && !GeneralData.loadInstance().getCurrentSemester().getAllocations().isEmpty() ) {
                	flash(ERROR, ctx().messages()
                            .at("error.changeTimesAfterFinalAllocation"));
                    return redirect(controllers.routes.AdminPageController
                            .propertiesPage());
                }
                List<SPO> deletedSPOs = semester.getSpos();
                deletedSPOs.removeAll(usedSPOs);
                boolean spoUsed = false;
                for (SPO spo : deletedSPOs) {
                    for (Student s : semester.getStudents()) {
                        if (s.getSPO().equals(spo)) {
                            spoUsed = true;
                            break;
                        }
                    }
                    if (spoUsed) {
                        break;
                    }
                }
                // überprüfungen ob das aktuelle semester aktiv gesetzt werden
                // darf (wenn es nicht aktiv war)
                if (!semester.equals(GeneralData.loadInstance()
                        .getCurrentSemester()) && semesterActive != null) {
                    // wenn im moment studenten bewertungen abgeben können oder
                    // die allocation queue nicht leer ist -> nicht möglich
                    if (StateStorage.getInstance().getCurrentState() == StateStorage.State.REGISTRATION_PHASE
                            || !AllocationQueue.getInstance().getQueue()
                                    .isEmpty()) {
                        flash(ERROR,
                                ctx().messages()
                                        .at("error.activeSemester.changeNotAllowed"));
                        return redirect(controllers.routes.AdminPageController
                                .propertiesPage());
                    }
                }
                // Speichere das Semester
                if (!spoUsed && !startDate.after(endDate)) {
                    semester.doTransaction(() -> {
                        semester.setSpos(usedSPOs);
                        semester.setInfoText(generalInfo);
                        semester.setName(name);
                        semester.setRegistrationStart(startDate);
                        semester.setRegistrationEnd(endDate);
                        semester.setWintersemester(wintersemester != null);
                        // true wenn wintersemester == null
                        semester.setMaxGroupSize(maxGroupSize);
                    });
                    if (semesterActive != null) {
                        GeneralData data = GeneralData.loadInstance();
                        data.doTransaction(() -> {
                            data.setCurrentSemester(semester);
                        });
                        StateStorage.getInstance().initStateChanging(startDate,
                                endDate);
                    }
                } else {
                    flash(ERROR,
                            ctx().messages()
                                    .at("error.editSemester.spoUsedOrStartAfterEnd"));
                }

                return redirect(controllers.routes.AdminPageController
                        .propertiesPage());
            }
        }
    }

    /**
     * Diese Methode fügt eine neue Teilleistung zu einer bereits vorhandenen
     * SPO hinzu. Der Administrator kann die Teilleistung als notwendig oder als
     * nicht notwendig deklarieren und deren Namen ändern. Der Administrator
     * wird daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result addAchievement() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        String nameAchiev;
        String idSPOString = form.get("id");
        int idSPO;
        // Prüfe die Eingaben aus dem Formular
        try {
            nameAchiev = Forms.getNonEmptyStringValidator().validate(
                    form.get("nameAchiev"));
            idSPO = new IntValidator(0).validate(idSPOString);
        } catch (ValidationException e) {
            flash(ERROR, ctx().messages().at(e.getMessage()));
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage());
        }
        SPO spo = ElipseModel.getById(SPO.class, idSPO);

        // Erzeuge die Teilleistung und speichere sie in der Datenbank
        Achievement achievement = new Achievement(nameAchiev);
        achievement.save();
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(achievement);
        });
        return redirect(controllers.routes.AdminPageController.propertiesPage());
    }

    /**
     * Diese Methode übernimmt änderungen an der SPO. Der Administrator wird
     * daraufhin zur Einstellungsseite zurückgeleitet.
     * 
     * @return Die Seite, die als Antwort verschickt wird.
     */
    public Result changeSPO() {
        synchronized (SPO.class) {
            DynamicForm form = formFactory.form().bindFromRequest();
            if (form.data().isEmpty()) {
                return badRequest(ctx().messages().at(INTERNAL_ERROR));
            }

            // Prüfe die Formularinhalte auf korrekte Inhalte
            String nameSPO;
            String idString = form.get("id");
            int id;
            try {
                id = new IntValidator(0).validate(idString);
                nameSPO = Forms.getNonEmptyStringValidator().validate(
                        form.get("nameSPO"));
            } catch (ValidationException e) {
                flash(ERROR, ctx().messages().at(e.getMessage()));
                return redirect(controllers.routes.AdminPageController
                        .propertiesPage());
            }

            // Hole die SPO aus der Datenbank
            SPO spo = ElipseModel.getById(SPO.class, id);
            // wenn die SPO gelöscht wurde fehler
            if (spo == null) {
                flash(ERROR, ctx().messages().at(SPO.CONCURRENCY_ERROR));
                return redirect(controllers.routes.AdminPageController
                        .propertiesPage());
            }
            List<Achievement> necAchiev = spo.getNecessaryAchievements();
            List<Achievement> addAchiev = spo.getAdditionalAchievements();
            // iterators werden kreiert da man sonst nichts entfernen pver
            // hinzufügen kann

            Iterator<Achievement> necAchievments = necAchiev.iterator();
            while (necAchievments.hasNext()) {
                // für alle neccesary und additional achievments wird geprüft ob
                // sie
                // gelöscht werden müssen oder in die andere liste müssen
                Achievement achiev = necAchievments.next();
                if (form.get("delete-" + Integer.toString(achiev.getId())) != null) {
                    spo.doTransaction(() -> {
                        necAchievments.remove();
                    });
                    achiev.delete();
                } else if (form.get("necessary-"
                        + Integer.toString(achiev.getId())) == null) {
                    spo.doTransaction(() -> {
                        spo.addAdditionalAchievement(achiev);
                    });
                    necAchievments.remove();
                }

            }
            Iterator<Achievement> addAchievments = addAchiev.iterator();
            while (addAchievments.hasNext()) {
                Achievement achiev = addAchievments.next();
                if (form.get("delete-" + Integer.toString(achiev.getId())) != null) {
                    spo.doTransaction(() -> {
                        addAchievments.remove();
                    });
                    achiev.delete();
                } else if (form.get("necessary-"
                        + Integer.toString(achiev.getId())) != null) {
                    spo.doTransaction(() -> {
                        spo.addNecessaryAchievement(achiev);
                    });
                    addAchievments.remove();
                }
            }

            // name wird aktualisiert
            spo.doTransaction(() -> {
                spo.setName(nameSPO);
            });
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage());
        }
    }

    /**
     * Diese Methode ändert die SMTP-Options, damit der Administrator einstellen
     * kann, welcher SMTP-Server zum verschicken von Mails verwendet wird.
     * 
     * @return
     */
    public Result editSMTPOptions() {
        DynamicForm form = formFactory.form().bindFromRequest();
        if (form.data().isEmpty()) {
            return badRequest(ctx().messages().at(INTERNAL_ERROR));
        }
        int port;
        int connectionTimeout;
        int timeout;
        boolean ssl = form.get("ssl") != null;
        boolean tls = form.get("tls") != null;
        boolean debug = form.get("debug") != null;
        try {
            port = Integer.parseInt(form.get("port"));
            connectionTimeout = Integer.parseInt(form.get("connectionTimeOut"));
            timeout = Integer.parseInt(form.get("timeout"));
        } catch (NumberFormatException e) {
            flash(ERROR, ctx().messages().at(NUMBER_ERROR));
            return redirect(controllers.routes.AdminPageController
                    .propertiesPage());
        }

        SMTPOptions options = SMTPOptions.getInstance();
        options.doTransaction(() -> {
            options.setHost(form.get("host"));
            options.setMailFrom(form.get("mail"));
            options.setUsername(form.get("username"));
            options.setPort(port);
            options.setConnectionTimeout(connectionTimeout);
            options.setTimeout(timeout);
            options.setSsl(ssl);
            options.setTls(tls);
            options.setDebug(debug);
            options.savePassword(form.get("password"));

        });
        return redirect(controllers.routes.AdminPageController.propertiesPage());
    }
}
