package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest extends DataTest {

    private Project project;

    @Before
    public void beforeTest() {
        project = new Project();
    }

    @Test
    public void testName() {
        String n = "testname";
        project.setName(n);
        assertEquals(n, project.getName());
    }

    @Test
    public void testMinTeamSize() {
        int s = 11;
        project.setMinTeamSize(s);
        assertEquals(s, project.getMinTeamSize());
    }

    @Test
    public void testMaxTeamSize() {
        int s = 11;
        project.setMaxTeamSize(s);
        assertEquals(s, project.getMaxTeamSize());
    }

    @Test
    public void testNumberOfTeams() {
        int s = 11;
        project.setNumberOfTeams(s);
        assertEquals(s, project.getNumberOfTeams());
    }

    @Test
    public void testProjectInfo() {
        String n = "testinfo";
        project.setProjectInfo(n);
        assertEquals(n, project.getProjectInfo());
    }

    @Test
    public void testProjectURL() {
        String n = "testurl";
        project.setProjectURL(n);
        assertEquals(n, project.getProjectURL());
    }

    @Test
    public void testInstitut() {
        String n = "testinstitut";
        project.setInstitute(n);
        assertEquals(n, project.getInstitute());
    }

    @Test
    public void testAdvisers() {
        Adviser firstA = new Adviser();
        Adviser secondA = new Adviser();
        List<Adviser> advisers = new ArrayList<Adviser>();
        advisers.add(firstA);
        project.setAdvisers(advisers);
        assertEquals(project.getAdvisers().size(), 1);
        assertTrue(project.getAdvisers().contains(firstA));

        project.addAdviser(secondA);
        assertEquals(project.getAdvisers().size(), 2);
        assertTrue(project.getAdvisers().contains(firstA));
        assertTrue(project.getAdvisers().contains(secondA));

        project.removeAdviser(firstA);
        assertEquals(project.getAdvisers().size(), 1);
        assertTrue(project.getAdvisers().contains(secondA));
    }

    // @Test
    // public void testGetRating() {
    // Semester semester = new Semester();
    // semester.save();
    // List<Project> projects = new ArrayList<Project>();
    // projects.add(project);
    // project.setSemester(semester);
    // project.save();
    // semester.setProjects(projects);
    // semester.save();
    // LearningGroup l = new LearningGroup();
    // Student s = new Student();
    // s.save();
    // List<Student> students = new ArrayList<Student>();
    // students.add(s);
    // l.setMembers(students);
    // l.save();
    // Rating rating = new Rating();
    // int r = 11;
    // rating.setRating(r);
    // rating.setProject(project);
    // rating.save();
    // List<Rating> ratings = new ArrayList<Rating>();
    // ratings.add(rating);
    // l.setRatings(ratings);
    // l.save();
    // List<LearningGroup> learningGroups = new ArrayList<LearningGroup>();
    // learningGroups.add(l);
    // semester.setLearningGroups(learningGroups);
    // semester.save();
    // GeneralData.getInstance().setCurrentSemester(semester);
    // GeneralData.getInstance().save();
    // assertEquals(r, project.getRating(s));
    // }

    @Test
    public void testGetSemester() {
        Semester s = new Semester();
        s.save();
        List<Project> projects = new ArrayList<Project>();
        project.save();
        projects.add(project);
        s.setProjects(projects);
        s.save();
        GeneralData.getInstance().setCurrentSemester(s);
        GeneralData.getInstance().save();
        assertEquals(s, project.getSemester());
    }
}
