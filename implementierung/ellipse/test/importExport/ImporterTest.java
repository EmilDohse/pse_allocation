package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Allocation;
import data.GeneralData;
import data.SPO;
import data.Semester;
import exception.DataException;
import exception.ImporterException;

public class ImporterTest {

    private static Importer    importerExporter;
    private static EbeanServer server;

    @Before
    public void before() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
        importerExporter = new Importer();

    }

    @Test
    @Ignore
    public void testImportStudents() throws DataException, ImporterException {
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
        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(importStudentSemester);
        });
        // Importiere Projekte
        importerExporter.importProjects(new File("Projekte.csv"),
                importStudentSemester);
        // Importiere Studenten
        importerExporter.importStudents(new File("exportStudents.csv"),
                importStudentSemester);
        assertFalse(Semester.getSemester("importStudentSemester").getStudents()
                .isEmpty());
        assertFalse(Semester.getSemester("importStudentSemester")
                .getLearningGroups().isEmpty());
        importerExporter.exportStudents(new File("exportStudents.csv"),
                Semester.getSemester("importStudentSemester"));
    }

    @Test
    public void testImportProjects() throws DataException {
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
        if (SPO.getSPO("2008") != null) {
            SPO.getSPO("2008").delete();
        }
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
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    @Ignore
    public void testImportAllocation() throws DataException, ImporterException {
        Semester semester = new Semester("test", false, 1970);
        semester.setInfoText("Hi");
        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(semester);
        });
        importerExporter.importSPO(new File("spo2008.csv"));
        importerExporter.importProjects(new File("Projekte.csv"), semester);
        importerExporter.importStudents(new File("exportStudents.csv"),
                semester);
        importerExporter.importAllocation(new File("exportAllocation.csv"),
                semester);
        assertTrue(Allocation.getAllocations().size() > 0);
    }

    @After
    public void after() {
        importerExporter = null;
        server.shutdown(false, false);
    }
}
