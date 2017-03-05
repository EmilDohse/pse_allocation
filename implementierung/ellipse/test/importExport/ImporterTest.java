package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Achievement;
import data.Allocation;
import data.GeneralData;
import data.Grade;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import data.Team;
import exception.ImporterException;

/**
 * Diese Klasse beinhaltet Tests für das Importieren und Exportieren von
 * Studenten SPOs und Einteilungen.
 */
public class ImporterTest {

    private static Importer    importerExporter;
    private static EbeanServer server;

    /**
     * Setup des Servers und Initialisierung der allgemeinen Daten.
     */
    @Before
    public void before() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);

        // Init General Data. Evolutions wollen nicht funktionieren
        GeneralData data = new GeneralData();
        data.save();
        Semester semester = new Semester();
        semester.save();
        data.setCurrentSemester(semester);
        data.save();

        importerExporter = new Importer();

    }

    /**
     * Diese Methode testet das Importieren von Studierenden.
     * 
     * @throws ImporterException
     */
    @Test
    public void testImportStudents() throws ImporterException {
        // Importiere SPO
        importerExporter.importSPO(new File("importSpo.csv"));
        // Lege Semester an
        Semester importStudentSemester = new Semester("importStudentSemester",
                true);
        importStudentSemester.setInfoText("Ich bin ein Infotext");
        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(importStudentSemester);
        });
        // Importiere Projekte
        importerExporter.importProjects(new File("importProjects.csv"),
                importStudentSemester);
        // Importiere Studenten
        importerExporter.importStudents(new File("importStudents.csv"),
                importStudentSemester);
        assertFalse(Semester.getSemester("importStudentSemester").getStudents()
                .isEmpty());
        assertFalse(Semester.getSemester("importStudentSemester")
                .getLearningGroups().isEmpty());
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInImportStudents() {
        try {
            importerExporter.importAllocation(new File(new String()), null);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Testet das Exportieren von Studenten. Alle Exception sollten zum Abbruch
     * des Tests führen.
     * 
     * @throws ImporterException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testExportStudents()
            throws ImporterException, FileNotFoundException, IOException {
        Student student = new Student();
        Achievement firstAchievement = new Achievement();
        firstAchievement.doTransaction(() -> {
            firstAchievement.setName("one");
        });
        Achievement secondAchievement = new Achievement();
        secondAchievement.doTransaction(() -> {
            secondAchievement.setName("two");
        });
        SPO spo = new SPO();
        spo.doTransaction(() -> {
            spo.setName("spo");
            spo.addNecessaryAchievement(firstAchievement);
            spo.addAdditionalAchievement(secondAchievement);
        });
        Project project = new Project();
        project.doTransaction(() -> {
            project.setName("project");
        });
        ArrayList<Achievement> completed = new ArrayList<>();
        ArrayList<Achievement> open = new ArrayList<>();
        completed.add(firstAchievement);
        open.add(secondAchievement);
        student.doTransaction(() -> {
            student.setMatriculationNumber(1);
            student.setFirstName("first Name");
            student.setLastName("last Name");
            student.setEmailAddress("e@mail");
            student.savePassword("password");
            student.setSPO(spo);
            student.setSemester(1);
            student.setCompletedAchievements(completed);
            student.setOralTestAchievements(open);
        });
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(String.valueOf(student.getMatriculationNumber()));
            lg.addMember(student);
            lg.setPrivate(false);
            lg.savePassword("password");
            lg.rate(project, 3);
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
            semester.addLearningGroup(lg);
            semester.addStudent(student);
        });

        importerExporter.exportStudents(new File("exportTestStudents.csv"),
                semester);

        try (BufferedReader br = new BufferedReader(
                new FileReader(new File("exportTestStudents.csv")))) {
            String header = "MatNr;Vorname;Nachname;E-Mail;Passwort;Lerngruppenname;LerngruppePasswort;"
                    + "SPO;Fachsemester;Bestandene Teilleistungen;Noch ausstehende Teilleistungen;"
                    + project.getName();
            assertEquals(header, br.readLine());
            String[] attributes = {
                    String.valueOf(student.getMatriculationNumber()),
                    student.getFirstName(), student.getLastName(),
                    student.getEmailAddress(), student.getPassword(),
                    lg.getName(), lg.getPassword(), spo.getName(),
                    String.valueOf(student.getSemester()),
                    firstAchievement.getName(), secondAchievement.getName(),
                    String.valueOf(lg.getRating(project)) };
            String[] importedAttributes = br.readLine().split(";");
            assertEquals(attributes.length, importedAttributes.length);
            for (int i = 0; i < importedAttributes.length; i++) {
                assertEquals(attributes[i], importedAttributes[i]);
            }
        }
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInExportStudents() {
        Semester semester = new Semester();
        semester.addStudent(new Student());
        try {
            importerExporter.exportStudents(new File(new String()), semester);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Diese Methode testet das Importierern und Exportieren von Projekten.
     * 
     * @throws ImporterException
     */
    @Test
    public void testImportProjects() throws ImporterException {
        Semester importProjects = new Semester("importProjects", true);
        importProjects.setInfoText("hallo");
        Ebean.save(importProjects);
        importerExporter.importProjects(new File("importProjects.csv"),
                importProjects);
        assertFalse(
                Semester.getSemester("importProjects").getProjects().isEmpty());
        assertEquals(23,
                Semester.getSemester("importProjects").getProjects().size());
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInImportProjects() {
        Semester semester = new Semester();
        try {
            importerExporter.importProjects(new File(new String()), semester);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Testet das Exportieren von Projekten. Alle Exception sollten zum Abbruch
     * des Tests führen.
     * 
     * @throws ImporterException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testExportProjects()
            throws ImporterException, FileNotFoundException, IOException {
        Project project = new Project();
        project.doTransaction(() -> {
            project.setName("project");
            project.setInstitute("institute");
            project.setNumberOfTeams(1);
            project.setMinTeamSize(1);
            project.setMaxTeamSize(2);
            project.setProjectURL("url");
            project.setProjectInfo("info");
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });
        importerExporter.exportProjects(new File("exportTestProjects.csv"),
                semester);

        try (BufferedReader br = new BufferedReader(
                new FileReader(new File("exportTestProjects.csv")))) {
            String header = "Name;Institut;Anzahl Teams;Min. Size;Max. Size;Projekt URL;Projektinfo";
            assertEquals(header, br.readLine());
            String[] attributes = { project.getName(), project.getInstitute(),
                    String.valueOf(project.getNumberOfTeams()),
                    String.valueOf(project.getMinTeamSize()),
                    String.valueOf(project.getMaxTeamSize()),
                    project.getProjectURL(), project.getProjectInfo() };
            String[] importedAttributes = br.readLine().split(";");
            assertEquals(attributes.length, importedAttributes.length);
            for (int i = 0; i < attributes.length; i++) {
                assertEquals(attributes[i], importedAttributes[i]);
            }
        }
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInExportProjects() {
        Semester semester = new Semester();
        semester.addProject(new Project());
        try {
            importerExporter.exportProjects(new File(new String()), semester);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Diese Methode testet das Importieren und Exportieren von SPOs.
     * 
     * @throws ImporterException
     */
    @Test
    public void testImportSPO() throws ImporterException {
        if (SPO.getSPO("2008") != null) {
            SPO.getSPO("2008").delete();
        }
        importerExporter.importSPO(new File("importSpo.csv"));
        assertNotNull(SPO.getSPO("2008"));
        assertFalse(SPO.getSPO("2008").getAdditionalAchievements().isEmpty());
        assertFalse(SPO.getSPO("2008").getNecessaryAchievements().isEmpty());
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInImportSpo() {
        try {
            importerExporter.importSPO(new File(new String()));
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Testet das Exportieren einer SPO. Alle geworfenen SPO sollen zum
     * Fehlschlagen des Tests führen.
     * 
     * @throws ImporterException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testExportSPO()
            throws ImporterException, FileNotFoundException, IOException {
        SPO spo = new SPO();
        Achievement firstAchievement = new Achievement();
        Achievement secondAchievement = new Achievement();
        firstAchievement.doTransaction(() -> {
            firstAchievement.setName("one");
        });
        secondAchievement.doTransaction(() -> {
            secondAchievement.setName("two");
        });

        spo.doTransaction(() -> {
            spo.setName("spo");
            spo.addNecessaryAchievement(firstAchievement);
            spo.addAdditionalAchievement(secondAchievement);
        });

        importerExporter.exportSPO(new File("exportTestSpo.csv"), spo);

        try (BufferedReader br = new BufferedReader(
                new FileReader(new File("exportTestSpo.csv")))) {
            String header = "Name;Additional Achievements;Necessary Achievements";
            assertEquals(header, br.readLine());

            String[] attributes = { spo.getName(), secondAchievement.getName(),
                    firstAchievement.getName() };
            String[] importedAttributes = br.readLine().split(";");
            assertEquals(attributes.length, importedAttributes.length);
            for (int i = 0; i < attributes.length; i++) {
                assertEquals(attributes[i], importedAttributes[i]);
            }
        }
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testIOExceptionInExportSPO() {
        try {
            importerExporter.exportSPO(new File(new String()), null);
        } catch (ImporterException e) {
            assertEquals("importer.IOException", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Diese Methode testet das Importieren und Exportieren von Einteilungen.
     * 
     * @throws ImporterException
     */
    @Test
    public void testImportAllocation() throws ImporterException {
        Semester semester = new Semester("test", false);
        semester.setInfoText("Hi");
        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(semester);
        });
        importerExporter.importSPO(new File("importSpo.csv"));
        importerExporter.importProjects(new File("importProjects.csv"),
                semester);
        importerExporter.importStudents(new File("importStudents.csv"),
                semester);
        importerExporter.importAllocation(new File("importAllocation.csv"),
                semester);
        assertTrue(Allocation.getAllocations().size() > 0);
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInImportAllocation() {
        try {
            importerExporter.importAllocation(new File(new String()), null);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Testet das Exportieren einer Einteilung. Alle geworfenen Exceptions
     * sollen zum Fehlschlagen des Tests führen.
     * 
     * @throws ImporterException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void testExportAllocation()
            throws ImporterException, FileNotFoundException, IOException {
        Student firstStudent = new Student();
        firstStudent.doTransaction(() -> {
            firstStudent.setMatriculationNumber(1);
        });
        Student secondStudent = new Student();
        secondStudent.doTransaction(() -> {
            secondStudent.setMatriculationNumber(2);
        });
        Project project = new Project();
        project.doTransaction(() -> {
            project.setName("project");
        });
        Team team = new Team();
        team.addMember(firstStudent);
        team.addMember(secondStudent);
        team.setProject(project);
        team.setTeamNumber(1);
        ArrayList<Team> teams = new ArrayList<>();
        teams.add(team);
        Allocation allocation = new Allocation();
        allocation.doTransaction(() -> {
            allocation.setTeams(teams);
        });

        importerExporter.exportAllocation(new File("exportTestAllocation.csv"),
                allocation);

        try (BufferedReader br = new BufferedReader(
                new FileReader(new File("exportTestAllocation.csv")))) {
            String header = "Projekt;Teamnummer;Mitglieder";
            assertEquals(header, br.readLine());

            String[] attributes = { project.getName(),
                    String.valueOf(team.getTeamNumber()),
                    String.valueOf(firstStudent.getMatriculationNumber()) + ","
                            + String.valueOf(
                                    secondStudent.getMatriculationNumber()) };
            String[] importedAttributes = br.readLine().split(";");
            assertEquals(attributes.length, importedAttributes.length);
            for (int i = 0; i < attributes.length; i++) {
                assertEquals(attributes[i], importedAttributes[i]);
            }
        }
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInExportAllocation() {
        try {
            importerExporter.exportAllocation(new File(new String()), null);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Test für das Importieren der Noten.
     * 
     * @throws ImporterException
     *             ImporterException.
     * @throws FileNotFoundException
     *             FileNotFoundException.
     * @throws IOException
     *             IOException.
     */
    @Test
    public void testExportGrades()
            throws ImporterException, FileNotFoundException, IOException {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setMatriculationNumber(1);
            student.setGradePSE(Grade.TWO_SEVEN);
            student.setGradeTSE(Grade.THREE_ZERO);
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        importerExporter.exportGrades(new File("exportTestGrades.csv"),
                semester);

        try (BufferedReader br = new BufferedReader(
                new FileReader(new File("exportTestGrades.csv")))) {
            String header = "Matrikelnummer;Note PSE;Note TSE";
            assertEquals(header, br.readLine());

            String[] attributes = {
                    String.valueOf(student.getMatriculationNumber()),
                    student.getGradePSE().getName(),
                    student.getGradeTSE().getName() };
            String[] importedAttributes = br.readLine().split(";");

            assertEquals(attributes.length, importedAttributes.length);
            for (int i = 0; i < attributes.length; i++) {
                assertEquals(attributes[i], importedAttributes[i]);
            }
        }
    }

    /**
     * Prüft ob eine File Not Found Exception ausgelöst wird
     */
    @Test
    public void testFileNotFoundExceptionInExportGrades() {
        Semester semester = new Semester();
        semester.addStudent(new Student());
        try {
            importerExporter.exportGrades(new File(new String()), semester);
        } catch (ImporterException e) {
            assertEquals("importer.FileNotFound", e.getMessage());
            return;
        }
        fail();
    }

    @After
    public void after() {
        importerExporter = null;
        server.shutdown(false, false);
    }
}
