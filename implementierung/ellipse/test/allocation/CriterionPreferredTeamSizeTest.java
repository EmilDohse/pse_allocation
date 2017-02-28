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
import data.Project;
import data.Semester;
import data.Student;

public class CriterionPreferredTeamSizeTest {

    private static EbeanServer                server;
    private static CriterionPreferredTeamSize cpts;

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

    @Before
    public void before() {
        cpts = new CriterionPreferredTeamSize();
    }

    @Test
    public void testGetName() {
        assertEquals("PreferredTeamSize", cpts.getName());
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Beachte gewünschte Teamgröße", cpts.getDisplayName("de"));
        assertEquals("Respect preferred team size", cpts.getDisplayName("en"));
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

        ArrayList<AllocationParameter> paras = new ArrayList<>();
        paras.add(new AllocationParameter("minSize", 1));
        paras.add(new AllocationParameter("maxSize", 1));
        paras.add(new AllocationParameter("prefSize", 1));
        paras.add(new AllocationParameter("PreferredTeamSize", 10));

        Configuration config = new Configuration("test", semester.getStudents(),
                semester.getLearningGroups(), semester.getProjects(), paras);

        GurobiAllocator ga = new GurobiAllocator();
        ga.init(config);
        ga.calculate();

        Allocation alloc = Allocation.getAllocations().get(0);

        assertEquals(1, alloc.getTeams().get(0).getMembers().size());
    }

    @After
    public void after() {
        cpts = null;
    }

    /**
     * Server shutdown.
     */
    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }
}
