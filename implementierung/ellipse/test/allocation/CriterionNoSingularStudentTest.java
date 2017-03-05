package allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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

public class CriterionNoSingularStudentTest {

    private static EbeanServer                server;
    private static CriterionNoSingularStudent cnss;

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
        cnss = new CriterionNoSingularStudent();
    }

    /**
     * Test für den Getter des Namens.
     */
    @Test
    public void testGetName() {
        assertEquals("NoSingularStudent", cnss.getName());
    }

    /**
     * Test für den angezeigten Namen.
     */
    @Test
    public void testGetDisplayName() {
        assertEquals("Kein einzelner Student zu Lerngruppe", cnss.getDisplayName("de"));
        assertEquals("No singular student plus learning group in a team", cnss.getDisplayName("en"));
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
            project.setMaxTeamSize(3);
            project.setMinTeamSize(1);
            project.setNumberOfTeams(2);
        });

        Student first = new Student();
        Student second = new Student();
        Student third = new Student();

        first.save();
        second.save();
        third.save();

        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.addMember(first);
            lg.addMember(second);
        });

        semester.doTransaction(() -> {
            semester.addStudent(first);
            semester.addStudent(second);
            semester.addStudent(third);
            semester.addLearningGroup(lg);
            semester.addProject(project);
        });

        ArrayList<AllocationParameter> paras = new ArrayList<>();
        paras.add(new AllocationParameter("minSize", 1));
        paras.add(new AllocationParameter("maxSize", 1));
        paras.add(new AllocationParameter("prefSize", 1));
        paras.add(new AllocationParameter("LearningGroups", 10));
        paras.add(new AllocationParameter("NoSingularStudent", 10));

        Configuration config = new Configuration("test", semester.getStudents(), semester.getLearningGroups(),
                semester.getProjects(), paras);

        GurobiAllocator ga = new GurobiAllocator();
        ga.init(config);
        ga.calculate();

        Allocation alloc = Allocation.getAllocations().get(0);

        assertNotNull(alloc.getTeam(first));
        assertNotNull(alloc.getTeam(second));
        assertEquals(alloc.getTeam(first), alloc.getTeam(second));
        assertFalse(alloc.getTeam(first).equals(alloc.getTeam(third)));
    }

    /**
     * shutdown data.
     */
    @After
    public void after() {
        cnss = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
