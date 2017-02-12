package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
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

        // Init General Data. Evolutions wollen nicht funktionieren
        GeneralData data = new GeneralData();
        data.save();
        Semester semester = new Semester();
        semester.save();
        data.setCurrentSemester(semester);
        data.save();

        importerExporter = new Importer();

    }

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
        importerExporter.exportStudents(new File("exportTestStudents.csv"),
                Semester.getSemester("importStudentSemester"));
    }

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
        importerExporter.exportProjects(new File("exportTestProjects.csv"),
                importProjects);
    }

    @Test
    public void testImportSPO() throws ImporterException {
        if (SPO.getSPO("2008") != null) {
            SPO.getSPO("2008").delete();
        }
        importerExporter.importSPO(new File("importSpo.csv"));
        assertNotNull(SPO.getSPO("2008"));
        assertFalse(SPO.getSPO("2008").getAdditionalAchievements().isEmpty());
        assertFalse(SPO.getSPO("2008").getNecessaryAchievements().isEmpty());
        importerExporter.exportSPO(new File("exportTestSpo.csv"),
                SPO.getSPO("2008"));
    }

    @Test
    public void testImportAllocation() throws DataException, ImporterException {
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
        importerExporter.exportAllocation(new File("exportTestAllocation.csv"),
                Allocation.getAllocations().get(0));
    }

    @After
    public void after() {
        importerExporter = null;
        server.shutdown(false, false);
    }
}
