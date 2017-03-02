package views;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import data.Administrator;
import data.Adviser;
import data.Allocation;
import data.ElipseModel;
import data.GeneralData;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import data.Team;
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

    public static int createAdviser(String email, String password) {
        return createAdviser("AdviserFirst", "AdviserLast", email, password);
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

    public static int createAndJoinProject(String name, int advId) {
        int id = createProject(name);
        Project p = ElipseModel.getById(Project.class, id);
        p.doTransaction(() -> {
            p.addAdviser(ElipseModel.getById(Adviser.class, advId));
        });
        return id;
    }

    public static void createDataSetForAllocation(int numProjects,
            int numStudents, int minTeamSize, int maxTeamSize) {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        IntStream.rangeClosed(1, numProjects).forEach((number) -> {
            Project project = new Project();
            project.save();
            project.doTransaction(() -> {
                project.setName("TestProject" + number);
                project.setMaxTeamSize(maxTeamSize);
                project.setMinTeamSize(minTeamSize);
                project.setNumberOfTeams(1);
            });
            semester.doTransaction(() -> {
                semester.addProject(project);
            });
        });
        IntStream.rangeClosed(1, numStudents).forEach((number) -> {
            Student student = new Student();
            student.save();
            student.doTransaction(() -> {
                student.setFirstName("StudentFirstName" + number);
                student.setLastName("StudentLastName" + number);
            });
            semester.doTransaction(() -> {
                semester.addStudent(student);
            });
        });
    }

    public static void createDataSetForAllocation() {
        createDataSetForAllocation(1, 2, 1, 2);
    }

    public static int createSpo(String name) {
        SPO spo = new SPO(name);
        spo.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addSPO(spo);
        });
        return spo.getId();
    }
}
