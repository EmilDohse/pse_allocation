package data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import exception.DataException;

public class AllocationTest extends DataTest {

    @Test
    public void testID() throws DataException {
        int id = 42;
        Allocation a = new Allocation();
        a.setId(id);
        assertEquals(id, a.getId());
    }

    @Test
    public void testParameters() throws DataException {
        List<AllocationParameter> para = new ArrayList<AllocationParameter>();
        Allocation a = new Allocation();
        a.setParameters(para);
        assertEquals(para, a.getParameters());
    }

    @Test
    public void testName() throws DataException {
        String name = "Name";
        Allocation a = new Allocation();
        a.setName(name);
        assertEquals(name, a.getName());
    }

    @Test
    public void testTeams() throws DataException {
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
}
