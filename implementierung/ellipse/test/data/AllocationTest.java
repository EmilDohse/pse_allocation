package data;

import static org.junit.Assert.assertEquals;

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
}
