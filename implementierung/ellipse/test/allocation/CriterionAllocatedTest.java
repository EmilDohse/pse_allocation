package allocation;

import static org.junit.Assert.assertEquals;
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
import data.Project;
import data.Semester;
import data.Student;

public class CriterionAllocatedTest {

    private static EbeanServer        server;
    private static CriterionAllocated ca;

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
     * Initialisierung.
     */
    @Before
    public void before() {
        ca = new CriterionAllocated();
    }

    /**
     * Test für den Getter des Namens.
     */
    @Test
    public void testGetName() {
        assertEquals("Allocated", ca.getName());
    }

    /**
     * Test für den angezeigten Namen.
     */
    @Test
    public void testGetDisplayName() {
        assertEquals("Teile möglichst viele Studenten ein", ca.getDisplayName("de"));
        assertEquals("Assign as many students as possible", ca.getDisplayName("en"));
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
            project.setNumberOfTeams(1);
        });

        Student first = new Student();
        Student second = new Student();

        semester.doTransaction(() -> {
            semester.addStudent(first);
            semester.addStudent(second);
            semester.addProject(project);
        });

        // Aktiviere das Kriterium
        ArrayList<AllocationParameter> paras = new ArrayList<>();
        paras.add(new AllocationParameter("minSize", 1));
        paras.add(new AllocationParameter("maxSize", 1));
        paras.add(new AllocationParameter("prefSize", 1));
        paras.add(new AllocationParameter("Allocated", 10));

        Configuration config = new Configuration("test", semester.getStudents(), semester.getLearningGroups(),
                semester.getProjects(), paras);

        GurobiAllocator ga = new GurobiAllocator();
        ga.init(config);
        ga.calculate();

        Allocation alloc = Allocation.getAllocations().get(0);
        assertNotNull(alloc.getTeam(first));
        assertNotNull(alloc.getTeam(second));
    }

    /**
     * shutdown data.
     */
    @After
    public void after() {
        ca = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
