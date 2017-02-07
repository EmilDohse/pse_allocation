package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.SPO;
import data.Semester;
import exception.ImporterException;

public class ImporterTest {

    private static Importer    importerExporter;
    private static EbeanServer server;

    @BeforeClass
    public static void beforeClass() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
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
            importerExporter.importSPO(new File("spo2008.csv"));
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
            importerExporter.importProjects(new File("Projekte.csv"),
                    importStudentSemester);
        } catch (ImporterException e1) {
            assertTrue(false);
        }
        // Importiere Studenten
        try {
            importerExporter.importStudents(new File("studentsNew.csv"),
                    importStudentSemester);
            assertFalse(Semester.getSemester("importStudentSemester")
                    .getStudents().isEmpty());
            assertFalse(Semester.getSemester("importStudentSemester")
                    .getLearningGroups().isEmpty());
            importerExporter.exportStudents(new File("exportStudents.csv"),
                    Semester.getSemester("importStudentSemester"));
        } catch (ImporterException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testImportProjects() {
        Semester importProjects = new Semester("importProjects", true, 2017);
        importProjects.setInfoText("hallo");
        Ebean.save(importProjects);
        try {
            importerExporter.importProjects(new File("Projekte.csv"),
                    importProjects);
            assertFalse(Semester.getSemester("importProjects").getProjects()
                    .isEmpty());
            assertEquals(23, Semester.getSemester("importProjects")
                    .getProjects().size());
            importerExporter.exportProjects(new File("exportProjects.csv"),
                    importProjects);
        } catch (ImporterException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testImportSPO() {
        try {
            importerExporter.importSPO(new File("spo2008.csv"));
            assertNotNull(SPO.getSPO("2008"));
            assertFalse(
                    SPO.getSPO("2008").getAdditionalAchievements().isEmpty());
            assertFalse(
                    SPO.getSPO("2008").getNecessaryAchievements().isEmpty());
            // TODO Hier kommt BeanList deferred raus... Keine Ahnung wie man
            // das fixt
            // assertEquals(2, SPO.getSPO("2008").getAdditionalAchievements());
            // assertEquals(2, SPO.getSPO("2008").getNecessaryAchievements());
            importerExporter.exportSPO(new File("exportSpo.csv"),
                    SPO.getSPO("2008"));
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
        server.shutdown(false, false);
    }
}
