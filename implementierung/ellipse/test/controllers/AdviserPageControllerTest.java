package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import data.Adviser;
import data.DataTest;
import data.GeneralData;
import data.Project;
import data.Semester;
import play.Application;
import play.api.mvc.RequestHeader;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.test.Helpers;
import security.UserManagement;

@RunWith(MockitoJUnitRunner.class)
public class AdviserPageControllerTest extends DataTest {

    @Mock
    FormFactory               formFactory;

    @Mock
    UserManagement            userManagement;

    @InjectMocks
    AdviserPageController     controller;

    public static Application app;

    @Mock
    private Http.Request      request;

    @Mock
    private RequestHeader     header;

    private DynamicForm       form;

    private Semester          semester;
    private Adviser           firstAdviser;
    private Adviser           secondAdviser;
    private Project           project;

    @BeforeClass
    public static void startApp() {
        app = Helpers.fakeApplication();
        Helpers.start(app);
    }

    @Override
    @Before
    public void before() {
        super.before();

        MockitoAnnotations.initMocks(this);

        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> flashObject = Collections.emptyMap();

        Http.Context context = new Http.Context(0l, header, request, flashData,
                flashData, flashObject);
        Http.Context.current.set(context);

        firstAdviser = new Adviser();
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

    @Test
    public void addProjectTest() {

        Mockito.when(userManagement.getUserProfile(Controller.ctx()))
                .thenReturn(firstAdviser);

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

    @AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }
}
