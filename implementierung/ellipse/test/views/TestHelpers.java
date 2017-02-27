package views;

import java.time.Instant;
import java.util.Date;

import data.Administrator;
import data.Adviser;
import data.GeneralData;
import data.Project;
import data.Semester;
import deadline.StateStorage;

public class TestHelpers {

    public static final String ADMIN_USERNAME = "admin";

    private TestHelpers() {

    }

    public static void setStateToBeforeRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.plusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        initStateChange();
    }

    public static void setStateToRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        initStateChange();
    }

    public static void setStateToAfterRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.minusSeconds(40)));
        });
        initStateChange();
    }

    private static void initStateChange() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
    }

    public static void createAdmin() {
        Administrator admin = new Administrator(ADMIN_USERNAME, "", "a@kit.edu",
                "admin", "admin");
        admin.save();
        admin.doTransaction(() -> {
            admin.savePassword(Administrator.START_PASSWORD);
        });
    }

    public static int createAdviser(String firstName, String lastName,
            String email, String password) {
        Adviser adviser = new Adviser(email, password, email, firstName,
                lastName);
        adviser.save();
        adviser.doTransaction(() -> {
            adviser.savePassword(password);
        });
        return adviser.getId();
    }

    public static int createProject(String name) {
        Project project = new Project(name, "", "", "");
        project.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });
        return project.getId();
    }
}
