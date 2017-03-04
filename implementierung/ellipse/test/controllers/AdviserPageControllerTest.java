package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import play.mvc.Http.Context;
import play.test.Helpers;
import security.BlowfishPasswordEncoder;
import security.UserManagement;

public class AdviserPageControllerTest extends ControllerTest {

    @Mock
    UserManagement        userManagement;

    @InjectMocks
    AdviserPageController controller;

    private Semester      semester;
    private Adviser       firstAdviser;
    private Adviser       secondAdviser;
    private Project       project;

    @Override
    @Before
    public void before() {

        super.before();

        firstAdviser = new Adviser();
        firstAdviser.doTransaction(() -> {
            firstAdviser.setEmailAddress("testemail");
            firstAdviser.setFirstName("firstName");
            firstAdviser.setLastName("lastName");
            firstAdviser.setPassword((new BlowfishPasswordEncoder())
                    .encode("password"));
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

        semester.refresh();
        firstAdviser.refresh();
        secondAdviser.refresh();
        project.refresh();
    }

    @Test
    public void addProjectTest() {

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
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

    // TODO flash()
    @Ignore
    @Test
    public void editAccountTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(firstAdviser);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("newpassword");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("newpassword");
        Mockito.when(form.get("emailChange")).thenReturn("NotNull");
        Mockito.when(form.get("newEmail")).thenReturn("new@email");

        controller.editAccount();

        firstAdviser.refresh();

        assertTrue((new BlowfishPasswordEncoder()).matches("newpassword",
                firstAdviser.getPassword()));
        assertEquals(firstAdviser.getEmailAddress(), "new@email");

    }

    @Test
    public void editProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(firstAdviser);
        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        controller.editProject();

        project.refresh();

        assertEquals(project.getNumberOfTeams(), 2);
        assertEquals(project.getMinTeamSize(), 3);
        assertEquals(project.getMaxTeamSize(), 4);
        assertEquals(project.getName(), "projectName");
        assertEquals(project.getProjectURL(), "projectUrl");
        assertEquals(project.getInstitute(), "projectInstitute");
        assertEquals(project.getProjectInfo(), "projectInfo");
        assertEquals(project.getAdvisers().size(), 1);
        assertEquals(project.getAdvisers().get(0), secondAdviser);

    }

    @Test
    public void joinProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(secondAdviser);

        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));

        controller.joinProject();

        project.refresh();

        assertEquals(project.getAdvisers().size(), 2);
        assertTrue(project.getAdvisers().contains(firstAdviser));
        assertTrue(project.getAdvisers().contains(secondAdviser));
    }

    @Test
    public void leaveProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(firstAdviser);

        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));

        controller.leaveProject();

        project.refresh();

        assertTrue(project.getAdvisers().isEmpty());
    }

    @Test
    public void removeProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class)))
                .thenReturn(firstAdviser);

        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));

        controller.removeProject();

        assertTrue(Project.getProjects().isEmpty());
    }

    @Test
    public void saveStudentsGradesTest() {
        // TODO
    }
}
