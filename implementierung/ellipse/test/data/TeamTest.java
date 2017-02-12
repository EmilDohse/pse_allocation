package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TeamTest extends DataTest {

    private Team team;

    @Before
    public void beforeTest() {
        team = new Team();
    }

    @Test
    public void testProject() {
        Project project = new Project();
        team.setProject(project);
        assertEquals(project, team.getProject());
    }

    @Test
    public void testMembers() {
        List<Student> members = new ArrayList<Student>();
        Student firstStudent = new Student();
        Student secondStudent = new Student();
        members.add(firstStudent);
        Project p = new Project();
        p.doTransaction(() -> {
            p.setMinTeamSize(0);
            p.setMaxTeamSize(100);
        });
        team.setProject(p);
        team.setMembers(members);
        assertEquals(team.getMembers().size(), 1);
        assertTrue(team.getMembers().contains(firstStudent));

        // add Member
        team.addMember(secondStudent);
        assertEquals(team.getMembers().size(), 2);
        assertTrue(team.getMembers().contains(firstStudent));
        assertTrue(team.getMembers().contains(secondStudent));

        // remove Member
        team.removeMember(firstStudent);
        assertEquals(team.getMembers().size(), 1);
        assertTrue(team.getMembers().contains(secondStudent));
    }

    @Test
    public void testGetAdvisers() {
        List<Adviser> advisers = new ArrayList<Adviser>();
        Adviser adviser = new Adviser();
        advisers.add(adviser);
        Project project = new Project();
        project.setAdvisers(advisers);
        team.setProject(project);
        assertEquals(team.getAdvisers().size(), 1);
        assertTrue(team.getAdvisers().contains(adviser));
    }
}
