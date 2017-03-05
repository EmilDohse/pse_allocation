package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Allocation.
 */
public class AllocationTest extends DataTest {

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für die ID der
     * Allocation.
     */
    @Test
    public void testID() {
        int id = 42;
        Allocation a = new Allocation();
        a.setId(id);
        assertEquals(id, a.getId());
    }

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für die
     * Parameter der Allocation.
     */
    @Test
    public void testParameters() {
        List<AllocationParameter> para = new ArrayList<AllocationParameter>();
        Allocation a = new Allocation();
        a.setParameters(para);
        assertEquals(para, a.getParameters());
    }

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für den Namen
     * der Allocation.
     */
    @Test
    public void testName() {
        String name = "Name";
        Allocation a = new Allocation();
        a.setName(name);
        assertEquals(name, a.getName());
    }

    /**
     * Diese Methode testet sowohl den getter alsauch den setter für die Teams
     * der Allocation. Außerem werden Zuweisung und Abfrage des Teams eines
     * Studenten getestet.
     */
    @Test
    public void testTeams() {
        List<Team> teams = new ArrayList<Team>();
        // Get Team
        Student student = new Student();
        student.save();
        Team team = new Team();
        team.addMember(student);
        teams.add(team);
        // GetterSetter
        Allocation a = new Allocation();
        a.save();
        team.setAllocation(a);
        team.save();
        a.setTeams(teams);
        // a.save();
        assertEquals(teams, a.getTeams());
        assertEquals(team, a.getTeam(student));
        // set Students Team
        Team newTeam = new Team();
        newTeam.save();
        a.setStudentsTeam(student, newTeam);
        a.save();
        assertEquals(newTeam, a.getTeam(student));
    }

    /**
     * Diese Methode testet die statischen Datenbankabfragen. Zum einen wird die
     * Methode getestet, die alle Allocations zurückgibt, zum anderen die
     * Methode, die eine spezifische Allocation zurückgibt.
     */
    @Test
    public void testGetAllocation() {
        Allocation.getAllocations().forEach(a -> a.delete());
        Allocation one = new Allocation(new ArrayList<>(), "one", new ArrayList<>());
        Allocation two = new Allocation(new ArrayList<>(), "two", new ArrayList<>());
        one.save();
        two.save();
        assertEquals(2, Allocation.getAllocations().size());
        assertEquals(one, Allocation.getAllocation("one"));
    }

    /**
     * Testet die Methode getTeamsByadviser.
     */
    @Test
    public void testGetTeamsByAdviser() {
        Adviser adviser = new Adviser();
        adviser.save();
        Project one = new Project();
        Project two = new Project();

        one.doTransaction(() -> {
            one.addAdviser(adviser);
            one.setNumberOfTeams(2);
        });
        two.doTransaction(() -> {
            two.setNumberOfTeams(1);
        });

        Team firstTeam = new Team();
        Team secondTeam = new Team();
        Team thirdTeam = new Team();

        firstTeam.setProject(one);
        secondTeam.setProject(one);
        thirdTeam.setProject(two);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(firstTeam);
        teams.add(secondTeam);
        teams.add(thirdTeam);

        Allocation alloc = new Allocation(teams, "test", new ArrayList<>());
        alloc.save();

        List<Team> adviserTeams = alloc.getTeamsByAdviser(adviser);
        assertEquals(2, adviserTeams.size());
        assertTrue(adviserTeams.contains(firstTeam));
        assertTrue(adviserTeams.contains(secondTeam));
        assertFalse(adviserTeams.contains(thirdTeam));
    }

    /**
     * Testet die Methode getTeamsByProject.
     */
    @Test
    public void testGetTeamsByProject() {
        Project one = new Project();
        one.save();
        Project two = new Project();
        two.save();

        Team firstTeam = new Team();
        Team secondTeam = new Team();
        Team thirdTeam = new Team();

        firstTeam.setProject(one);
        secondTeam.setProject(one);
        thirdTeam.setProject(two);

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(firstTeam);
        teams.add(secondTeam);
        teams.add(thirdTeam);

        Allocation alloc = new Allocation(teams, "test", new ArrayList<>());
        alloc.save();

        List<Team> teamsByProject = alloc.getTeamsByProject(one);

        assertEquals(2, teamsByProject.size());
        assertTrue(teamsByProject.contains(firstTeam));
        assertTrue(teamsByProject.contains(secondTeam));
    }
}
