package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.avaje.ebean.EbeanServer;

import controllers.AdminEditAllocationController;
import data.Allocation;
import data.AllocationParameter;
import data.DataTest;
import data.GeneralData;
import data.Semester;
import data.Student;
import data.Team;
import play.data.DynamicForm;
import play.data.FormFactory;

@RunWith(MockitoJUnitRunner.class)
public class AdminEditAllocationControllerTest extends DataTest {

    @Mock
    FormFactory                   formFactory;

    @InjectMocks
    AdminEditAllocationController controller;

    private DynamicForm           form;

    private static EbeanServer    server;

    @Override
    @Before
    public void before() {
        super.before();

        Student firstStudent = new Student();
        Student secondStudent = new Student();
        Student thirdStudent = new Student();

        firstStudent.doTransaction(() -> {
            firstStudent.setMatriculationNumber(1);
        });
        secondStudent.doTransaction(() -> {
            secondStudent.setMatriculationNumber(2);
        });
        thirdStudent.doTransaction(() -> {
            thirdStudent.setMatriculationNumber(3);
        });

        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        semester.doTransaction(() -> {
            semester.addStudent(firstStudent);
            semester.addStudent(secondStudent);
            semester.addStudent(thirdStudent);
        });

        Team firstTeam = new Team();
        Team secondTeam = new Team();

        firstTeam.addMember(firstStudent);
        firstTeam.addMember(secondStudent);

        List<Team> teams = new ArrayList<>();
        teams.add(firstTeam);
        teams.add(secondTeam);

        AllocationParameter p = new AllocationParameter("test", 1234);
        p.save();
        List<AllocationParameter> parameters = new ArrayList<>();
        parameters.add(p);

        Allocation a = new Allocation();
        a.doTransaction(() -> {
            a.setTeams(teams);
            a.setParameters(parameters);
            a.setName("test");
        });

        semester.doTransaction(() -> {
            semester.addAllocation(a);
        });

        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
    }

    @Test
    public void duplicateAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.bindFromRequest()).thenReturn(form);
        Mockito.when(form.get("allocationID")).thenReturn("1");
        Mockito.when(form.data()).thenReturn(map);

        controller.duplicateAllocation();

        assertEquals(2, GeneralData.loadInstance().getCurrentSemester()
                .getAllocations().size());

        Allocation a = Allocation.getAllocation("clonedtest");
        assertNotNull(a);

        assertEquals(2, a.getTeams().size());
        assertNotNull(a.getTeam(Student.getStudent(1)));
        assertEquals(a.getTeam(Student.getStudent(1)),
                a.getTeam(Student.getStudent(2)));
        assertNull(a.getTeam(Student.getStudent(3)));

        assertEquals(1, a.getParameters().size());
        assertEquals("test", a.getParameters().get(0).getName());
        assertEquals(1234, a.getParameters().get(0).getValue());
    }
}
