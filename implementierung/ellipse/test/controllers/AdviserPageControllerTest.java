package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Http.Context;
import security.UserManagement;
import data.Adviser;
import data.DataTest;
import data.GeneralData;
import data.Project;
import data.Semester;

@RunWith(MockitoJUnitRunner.class)
public class AdviserPageControllerTest extends DataTest {

    @Mock
    FormFactory           formFactory;

    @Mock
    UserManagement        userManagement;

    @InjectMocks
    AdviserPageController controller;

    private DynamicForm   form;

    private Semester      semester;
    private Adviser       firstAdviser;
    private Adviser       secondAdviser;
    private Project       project;

    @Override
    @Before
    public void before() {
        super.before();

        firstAdviser = new Adviser();
        firstAdviser.save();

        firstAdviser.doTransaction(() -> {
            firstAdviser.setEmailAddress("testemail");
            firstAdviser.setFirstName("firstName");
            firstAdviser.setLastName("lastName");
            firstAdviser.setPassword("passord");
            firstAdviser.setUserName("testname");
        });

        secondAdviser = new Adviser();
        secondAdviser.save();

        project = new Project();
        project.save();

        project.doTransaction(() -> {
            project.setInstitute("testinstitute");
            project.setMaxTeamSize(2);
            project.setMinTeamSize(1);
            project.setName("testname");
            project.setNumberOfTeams(1);
            project.setProjectInfo("testinfo");
            project.setProjectURL("testurl");
            project.addAdviser(firstAdviser);
        });

        semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });

        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
        Mockito.when(form.bindFromRequest()).thenReturn(form);

        semester.refresh();
        firstAdviser.refresh();
        secondAdviser.refresh();
        project.refresh();
    }

    // TODO ctx() mocken
    @Ignore
    @Test
    public void addProjectTest() {

        Context c = Mockito.mock(Context.class);
        controller = Mockito.spy(AdviserPageController.class);

        Mockito.when(controller.ctx()).thenReturn(c);

        Mockito.when(userManagement.getUserProfile(c)).thenReturn(firstAdviser);

        controller.addProject();

        semester.refresh();

        assertEquals(semester.getProjects().size(), 2);
        int temp = semester.getProjects().indexOf(project);
        Project newProject = semester.getProjects().get(1 - temp);
        assertNotNull(newProject);
        assertEquals(newProject.getName(),
                "new Project " + firstAdviser.getFirstName() + " "
                        + firstAdviser.getLastName());
        assertEquals(newProject.getAdvisers().size(), 1);
        assertEquals(newProject.getAdvisers().get(0), firstAdviser);

    }
}
