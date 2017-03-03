package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import data.Adviser;
import data.ElipseModel;
import data.GeneralData;
import data.Project;
import data.Semester;

public class AdminProjectControllerTest extends ControllerTest {

    @InjectMocks
    AdminProjectController controller;

    private Project        project;
    private Adviser        firstAdviser;
    private Adviser        secondAdviser;

    @Override
    @Before
    public void before() {
        super.before();

        firstAdviser = new Adviser();
        firstAdviser.save();

        secondAdviser = new Adviser();
        secondAdviser.save();

        project = new Project();
        project.save();

        project.doTransaction(() -> {
            project.setInstitute("testInstitut");
            project.setMaxTeamSize(2);
            project.setMinTeamSize(1);
            project.setName("testProject");
            project.setNumberOfTeams(1);
            project.setProjectInfo("testInfo");
            project.setProjectURL("testUrl");
            project.addAdviser(firstAdviser);
        });

        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addProject(project);
        });

        project.refresh();
        firstAdviser.refresh();
        secondAdviser.refresh();
    }

    @Test
    public void addProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("name")).thenReturn("projectName");

        controller.addProject();

        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        assertEquals(semester.getProjects().size(), 2);

        int temp = semester.getProjects().indexOf(project);
        Project newProject = semester.getProjects().get(1 - temp);

        assertEquals(newProject.getName(), "projectName");
    }

    @Test
    public void editProjectTest() {
        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.data()).thenReturn(map);
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectURL");
        Mockito.when(form.get("institute")).thenReturn("projectInstitut");
        Mockito.when(form.get("description")).thenReturn("projectDescription");
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");

        controller.editProject();

        project.refresh();

        assertEquals(project.getName(), "projectName");
        assertEquals(project.getProjectURL(), "projectURL");
        assertEquals(project.getInstitute(), "projectInstitut");
        assertEquals(project.getProjectInfo(), "projectDescription");
        assertEquals(project.getNumberOfTeams(), 2);
        assertEquals(project.getMinTeamSize(), 3);
        assertEquals(project.getMaxTeamSize(), 4);
        assertEquals(project.getAdvisers().size(), 1);
        assertTrue(project.getAdvisers().contains(secondAdviser));
    }

    // TODO Testdatenbank austauschen
    @Ignore
    @Test
    public void removeProjectTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("id"))
                .thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.removeProject();

        assertEquals(GeneralData.loadInstance().getCurrentSemester()
                .getProjects().isEmpty(), true);
        assertNull(ElipseModel.getById(Project.class, project.getId()));
    }
}
