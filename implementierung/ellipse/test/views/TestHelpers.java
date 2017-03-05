package views;

import java.time.Instant;
import java.util.Date;
import java.util.stream.IntStream;

import data.Administrator;
import data.Adviser;
import data.Allocation;
import data.ElipseModel;
import data.GeneralData;
import data.Grade;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import deadline.StateStorage;

/**
 * Diese Klasse beinhaltet Hilfsmethoden zur Initialisierung von Testdaten.
 */
public class TestHelpers {

    public static final String ADMIN_USERNAME = "admin";

    private TestHelpers() {

    }

    /**
     * Diese Methode setzte den PSE-Status auf "Registrierungsphase noch nicht
     * begonnen".
     */
    public static void setStateToBeforeRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.plusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        initStateChange();
    }

    /**
     * Diese Methode setzt den PSE-Status auf "In der Registrierungsphase".
     */
    public static void setStateToRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.plusSeconds(40)));
        });
        initStateChange();
    }

    /**
     * Diese Methode setzt den PSE-Status auf "Registrierungsphase beendet".
     */
    public static void setStateToAfterRegistration() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Instant i = Instant.now();
        semester.doTransaction(() -> {
            semester.setRegistrationStart(Date.from(i.minusSeconds(30)));
            semester.setRegistrationEnd(Date.from(i.minusSeconds(40)));
        });
        initStateChange();
    }

    /**
     * Diese Methode initialisiert eine Statusänderung.
     */
    private static void initStateChange() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
    }

    /**
     * Diese Methode erstellt einen default admin.
     */
    public static void createAdmin() {
        Administrator admin = new Administrator(ADMIN_USERNAME, "", "a@kit.edu",
                "admin", "admin");
        admin.save();
        admin.doTransaction(() -> {
            admin.setPassword(Administrator.START_PASSWORD_HASH);
        });
    }

    /**
     * Diese Methode erstellt einen Betreuer.
     * 
     * @param firstName
     *            Vorname des Betreurs.
     * @param lastName
     *            Nachname des Betreuers.
     * @param email
     *            Die Email-Adresse des Betreuers.
     * @param password
     *            Das Passwort des Betreurs.
     * @return Die ID des Betreuers.
     */
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

    /**
     * Diese Methode erstellt einen Betreuer.
     * 
     * @param email
     *            Die Email-Adresse der Betreuers.
     * @param password
     *            Das Passwort des Betreuers.
     * @return Die ID des Betreuers.
     */
    public static int createAdviser(String email, String password) {
        return createAdviser("AdviserFirst", "AdviserLast", email, password);
    }

    /**
     * Diese Methode erstellt ein Projekt.
     * 
     * @param name
     *            Der Name des Projektes.
     * @return Die Projekt-ID.
     */
    public static int createProject(String name) {
        Project project = new Project(name, "", "", "");
        project.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });
        return project.getId();
    }

    /**
     * Diese Methode lässt einen Betreuer einem Projekt beitreten.
     * 
     * @param name
     *            Der Name des Projektes.
     * @param advId
     *            Die ID des Betreuers.
     * @return Die ID des Projektes.
     */
    public static int createAndJoinProject(String name, int advId) {
        int id = createProject(name);
        Project p = ElipseModel.getById(Project.class, id);
        p.doTransaction(() -> {
            p.addAdviser(ElipseModel.getById(Adviser.class, advId));
        });
        return id;
    }

    /**
     * Diese Methode erstellt einen Datensatz für eine Einteilung.
     * 
     * @param numProjects
     *            Anzahl der Projekte.
     * @param numStudents
     *            Anzahl der Studenten.
     * @param minTeamSize
     *            Minimale Teamgröße.
     * @param maxTeamSize
     *            Maximale Teamgröße.
     */
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
            LearningGroup l = new LearningGroup(student.getUserName(), "");
            l.save();
            l.doTransaction(() -> {
                l.addMember(student);
                l.setPrivate(true);
                // Ratings initialisieren
                for (Project p : GeneralData.loadInstance().getCurrentSemester()
                        .getProjects()) {
                    l.rate(p, 3);
                }
            });
            semester.doTransaction(() -> {
                semester.addStudent(student);
                semester.addLearningGroup(l);
            });
        });
    }

    /**
     * Diese Methode erstellt einen Studenten.
     * 
     * @param matrnr
     *            Die Matrikelnummer des Studenten.
     * @param password
     *            Das Passwort des Studenten.
     */
    public static void createStudent(int matrnr, String password) {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Student student = new Student();
        student.save();
        student.doTransaction(() -> {
            student.setFirstName("StudentFirstName");
            student.setLastName("StudentLastName");
            student.setMatriculationNumber(matrnr);
            student.savePassword(password);
            student.setUserName(Integer.toString(matrnr));
        });

        LearningGroup l = new LearningGroup(student.getUserName(), "");
        l.save();
        l.doTransaction(() -> {
            l.addMember(student);
            l.setPrivate(true);
            // Ratings initialisieren
            for (Project p : GeneralData.loadInstance().getCurrentSemester()
                    .getProjects()) {
                l.rate(p, 3);
            }
        });
        semester.doTransaction(() -> {
            semester.addStudent(student);
            semester.addLearningGroup(l);
        });
    }

    /**
     * Diese Mathode erstellt eine Lerngruppe.
     * 
     * @param name
     *            Der Name der Lerngruppe.
     * @param password
     *            Das Passwort der Lerngruppe.
     * @return Die erstellte Lerngruppe.
     */
    public static LearningGroup createLearningGroup(String name,
            String password) {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        LearningGroup l = new LearningGroup(name, "");
        l.save();
        l.doTransaction(() -> {
            l.savePassword(password);
            l.setPrivate(false);
            // Ratings initialisieren
            for (Project p : GeneralData.loadInstance().getCurrentSemester()
                    .getProjects()) {
                l.rate(p, 3);
            }
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(l);
        });
        return l;
    }

    /**
     * Diese Methode erstellt eine Lerngruppe und lässt ihr einen Studenten
     * beitreten.
     * 
     * @param matrnr
     *            Die Matrikelnummer des Studenten.
     */
    public static void createAndJoinLearningGroup(int matrnr) {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        Student student = Student.getStudent(matrnr);
        LearningGroup old = semester.getLearningGroupOf(student);
        old.doTransaction(() -> {
            old.removeMember(student);
        });
        old.delete();
        LearningGroup l = createLearningGroup("Test", "TestPW");
        l.doTransaction(() -> {
            l.addMember(student);
        });
    }

    /**
     * Diese Methode ertellt einen DatenSatz für eine Einteilung.
     */
    public static void createDataSetForAllocation() {
        createDataSetForAllocation(1, 2, 1, 2);
    }

    /**
     * Diese Methode erstellt einen SPO.
     * 
     * @param name
     *            Der Name der SPO.
     * @return Die ID der SPO.
     */
    public static int createSpo(String name) {
        SPO spo = new SPO(name);
        spo.save();
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addSPO(spo);
        });
        return spo.getId();
    }

    public static int createAllocation(String name) {
        Allocation allocation = new Allocation();
        allocation.save();
        allocation.doTransaction(() -> {
            allocation.setName(name);
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addAllocation(allocation);
        });
        return allocation.getId();
    }

    public static void createStudentWithGrades(int matrnr) {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setMatriculationNumber(matrnr);
            student.setGradePSE(Grade.TWO_SEVEN);
            student.setGradeTSE(Grade.THREE_ZERO);
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });
    }
}
