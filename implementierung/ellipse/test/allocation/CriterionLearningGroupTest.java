package allocation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Allocation;
import data.AllocationParameter;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.Semester;
import data.Student;

public class CriterionLearningGroupTest {

    private static EbeanServer            server;
    private static CriterionLearningGroup clg;

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

    /**
     * Initialisiertung.
     */
    @Before
    public void before() {
        clg = new CriterionLearningGroup();
    }

    /**
     * Test für den Getter des Namens.
     */
    @Test
    public void testGetName() {
        assertEquals("LearningGroups", clg.getName());
    }

    /**
     * Test für den angezeigten Namen.
     */
    @Test
    public void testGetDisplayName() {
        assertEquals("Lerngruppe nicht trennen", clg.getDisplayName("de"));
        assertEquals("Do not split learning groups", clg.getDisplayName("en"));
    }

    /**
     * Test für das Benutzen des Kriteriums.
     */
    @Test
    public void testUseCriterion() {

        // Vorbereitung
        GeneralData data = GeneralData.loadInstance();

        // Erzeuge Semester
        Semester semester = new Semester();
        data.doTransaction(() -> {
            data.setCurrentSemester(semester);
        });

        Project project = new Project();
        project.doTransaction(() -> {
            project.setMaxTeamSize(2);
            project.setMinTeamSize(1);
            project.setNumberOfTeams(2);
        });

        Student first = new Student();
        Student second = new Student();
        first.save();
        second.save();

        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.addMember(first);
            lg.addMember(second);
        });

        semester.doTransaction(() -> {
            semester.addStudent(first);
            semester.addStudent(second);
            semester.addProject(project);
            semester.addLearningGroup(lg);
        });

        // Aktiviere das Kriterium
        ArrayList<AllocationParameter> paras = new ArrayList<>();
        paras.add(new AllocationParameter("minSize", 1));
        paras.add(new AllocationParameter("maxSize", 1));
        paras.add(new AllocationParameter("prefSize", 1));
        paras.add(new AllocationParameter("LearningGroups", 10));

        Configuration config = new Configuration("test", semester.getStudents(), semester.getLearningGroups(),
                semester.getProjects(), paras);

        GurobiAllocator ga = new GurobiAllocator();
        ga.init(config);
        ga.calculate();

        Allocation alloc = Allocation.getAllocations().get(0);
        assertEquals(alloc.getTeam(first), alloc.getTeam(second));
    }

    /**
     * shutdown data.
     */
    @After
    public void after() {
        clg = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
