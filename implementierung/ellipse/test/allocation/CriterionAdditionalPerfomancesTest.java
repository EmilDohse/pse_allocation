package allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Achievement;
import data.Allocation;
import data.AllocationParameter;
import data.GeneralData;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;

public class CriterionAdditionalPerfomancesTest {

    private static EbeanServer                    server;
    private static CriterionAdditionalPerfomances cap;

    /**
     * Setup Server.
     */
    @BeforeClass
    public static void beforeClass() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);

        GeneralData data = new GeneralData();
        data.save();
        Semester semester = new Semester();
        semester.save();
        data.setCurrentSemester(semester);
        data.save();
    }

    @Test
    public void testGetName() {
        assertEquals("AdditionalPerfomances", cap.getName());
    }

    @Test
    public void testDisplayName() {
        assertEquals("Bonus für zusätzliche Teilleistungen",
                cap.getDisplayName("de"));
        assertEquals("bonus additional perfomances", cap.getDisplayName("en"));
    }

    @Test
    public void testUseCriterion() {

        // Vorbereitung
        GeneralData data = GeneralData.loadInstance();

        // Erzeuge Semester
        Semester semester = new Semester();
        data.doTransaction(() -> {
            data.setCurrentSemester(semester);
        });

        // Erstelle SPO mit einer notwendigen und einer zusätzlichen
        // Teilleistung
        SPO spo = new SPO();
        Achievement necessary = new Achievement("one");
        Achievement additional = new Achievement("two");
        necessary.save();
        additional.save();
        ArrayList<Achievement> necessaryAchievements = new ArrayList<>();
        ArrayList<Achievement> additionalAchievements = new ArrayList<>();
        necessaryAchievements.add(necessary);
        additionalAchievements.add(additional);
        spo.doTransaction(() -> {
            spo.setNecessaryAchievements(necessaryAchievements);
            spo.setAdditionalAchievements(additionalAchievements);
        });

        // Erzeuge Projekt mit einem Team, in das genau ein Student kommt
        Project project = new Project();
        project.doTransaction(() -> {
            project.setMaxTeamSize(1);
            project.setMinTeamSize(1);
            project.setNumberOfTeams(1);
        });

        // Erzeuge einen Studenten mit, und einen ohne der zusätzlichen
        // Teilleistung
        Student nothingAdditional = new Student();
        Student hasAdditional = new Student();

        nothingAdditional.doTransaction(() -> {
            nothingAdditional.setSPO(spo);
            ArrayList<Achievement> ca = new ArrayList<>();
            ca.add(necessary);
            nothingAdditional.setCompletedAchievements(ca);
        });

        hasAdditional.doTransaction(() -> {
            hasAdditional.setSPO(spo);
            ArrayList<Achievement> ca = new ArrayList<>();
            ca.add(necessary);
            ca.add(additional);
            hasAdditional.setCompletedAchievements(ca);
        });

        semester.doTransaction(() -> {
            semester.addStudent(nothingAdditional);
            semester.addStudent(hasAdditional);
            semester.addProject(project);
        });

        // Aktiviere das Kriterium
        ArrayList<AllocationParameter> paras = new ArrayList<>();
        paras.add(new AllocationParameter("minSize", 1));
        paras.add(new AllocationParameter("maxSize", 1));
        paras.add(new AllocationParameter("prefSize", 1));
        paras.add(new AllocationParameter("AdditionalPerfomances", 10));

        Configuration config = new Configuration("test", semester.getStudents(),
                semester.getLearningGroups(), semester.getProjects(), paras);

        GurobiAllocator ga = new GurobiAllocator();
        ga.init(config);
        ga.calculate();

        // Stelle sicher, dass immer der Student mit der zusätzlichen
        // Teilleistung zugeteilt wird
        Allocation alloc = Allocation.getAllocations().get(0);
        assertNotNull(alloc.getTeam(hasAdditional));
        assertNull(alloc.getTeam(nothingAdditional));
    }

    @Before
    public void before() {
        cap = new CriterionAdditionalPerfomances();
    }

    @After
    public void after() {
        cap = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
