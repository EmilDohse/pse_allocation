package allocation;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import data.AllocationParameter;
import data.Semester;
import exception.ImporterException;
import importExport.Importer;

public class GurobiAllocatorTest {

    private static GurobiAllocator ga;

    @Before
    public void before() {
        ga = new GurobiAllocator();
    }

    @Test
    public void testServiceLoader() {
        List<GurobiCriterion> gc = ga.getAllCriteria();
        assertTrue(gc.size() == 9);
    }

    @Test
    @Ignore
    public void testCalculate() {
        Importer ie = new Importer();
        Semester semester = new Semester("calculateTest", false, 2015);
        semester.setInfoText("Hallo");
        semester.save();
        try {
            ie.importSPO("spo2008.csv");
            ie.importProjects("Projekte.csv", semester);
            ie.importStudents("studentsNew.csv", semester);
        } catch (ImporterException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        semester.save();
        ArrayList<AllocationParameter> para = new ArrayList<>();
        para.add(new AllocationParameter("AdditionalPerfomances", 10));
        para.add(new AllocationParameter("Allocated", 10));
        para.add(new AllocationParameter("LearningGroups", 10));
        para.add(new AllocationParameter("NoSingularStudent", 10));
        para.add(new AllocationParameter("PreferHigherSemester", 10));
        para.add(new AllocationParameter("PreferredTeamSize", 10));
        para.add(new AllocationParameter("Rating", 10));
        para.add(new AllocationParameter("RegisteredAgain", 10));
        para.add(new AllocationParameter("SameSemester", 10));
        Configuration conf = new Configuration("Test", semester.getStudents(),
                semester.getLearningGroups(), semester.getProjects(), para);
        ga.calculate(conf);
    }

    @After
    public void after() {
        ga = null;
    }
}
