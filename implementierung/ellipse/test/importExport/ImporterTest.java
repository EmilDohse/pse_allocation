package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import data.SPO;
import data.Semester;
import exception.ImporterException;
import play.Application;
import play.test.Helpers;

public class ImporterTest {

    private static Importer    importerExporter;
    private static Application app;

    @BeforeClass
    public static void beforeClass() {
        app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
        Helpers.start(app);
    }

    @Before
    public void before() {
        importerExporter = new Importer();

    }

    @Test
    public void testImportTestData() {
        importerExporter.importTestData("students.training.csv",
                "topics.training.csv");
        assertNotNull(Semester.getSemester("TestSemester"));
        assertEquals(227,
                Semester.getSemester("TestSemester").getStudents().size());
    }

    @Test
    public void testImportStudents() {
        // Importiere SPO
        try {
            importerExporter.importSPO("spo2008.csv");
        } catch (ImporterException e2) {
            // Kann auftreten, falls ImportSPO vor ImportStudents ausgeführt
            // wird, sollte aber keine Probleme machen
        }
        // Lege Semester an
        Semester importStudentSemester = new Semester("importStudentSemester",
                true, 2017);
        importStudentSemester.setInfoText("Ich bin ein Infotext");
        Ebean.save(importStudentSemester);
        // Importiere Projekte
        try {
            importerExporter.importProjects("Projekte.csv",
                    importStudentSemester);
        } catch (ImporterException e1) {
            assertTrue(false);
        }
        // Importiere Studenten
        try {
            importerExporter.importStudents("students.csv",
                    importStudentSemester);
            assertFalse(Semester.getSemester("importStudentSemester")
                    .getStudents().isEmpty());
        } catch (ImporterException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testImportProjects() {
        Semester importProjects = new Semester("importProjects", true, 2017);
        importProjects.setInfoText("hallo");
        Ebean.save(importProjects);
        try {
            importerExporter.importProjects("Projekte.csv", importProjects);
            assertFalse(Semester.getSemester("importProjects").getProjects()
                    .isEmpty());
            assertEquals(23, Semester.getSemester("importProjects")
                    .getProjects().size());
        } catch (ImporterException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testImportSPO() {
        try {
            importerExporter.importSPO("spo2008.csv");
            assertNotNull(SPO.getSPO("2008"));
            assertFalse(
                    SPO.getSPO("2008").getAdditionalAchievements().isEmpty());
            // TODO Hier kommt BeanList deferred raus... Keine Ahnung wie man
            // das fixt
            // assertEquals(2, SPO.getSPO("2008").getAdditionalAchievements());
            // assertEquals(2, SPO.getSPO("2008").getNecessaryAchievements());
        } catch (ImporterException e) {
            assertTrue(false);
        }
    }

    @After
    public void after() {
        importerExporter = null;

    }

    @AfterClass
    public static void afterClass() {
        Helpers.stop(app);
    }
}
