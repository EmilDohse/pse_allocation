package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
import play.mvc.Http.Context;

/**
 * Diese Klasse beinhaltet Tests für den AdminProjectController.
 */
public class AdminProjectControllerTest extends ControllerTest {

    @InjectMocks
    AdminProjectController controller;

    private Project        project;
    private Adviser        firstAdviser;
    private Adviser        secondAdviser;

    /**
     * Initialisierung der Testdaten.
     */
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
    }

    /**
     * Test für das Hinzufügen eines Projektes.
     */
    @Test
    public void addProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        when(form.data()).thenReturn(map);
        when(form.get("name")).thenReturn("projectName");

        controller.addProject();

        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        assertEquals(semester.getProjects().size(), 2);

        int temp = semester.getProjects().indexOf(project);
        Project newProject = semester.getProjects().get(1 - temp);

        assertEquals(newProject.getName(), "projectName");
    }

    /**
     * Test für das Editieren eines Projektes.
     */
    @Test
    public void editProjectTest() {
        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.data()).thenReturn(map);
        when(form.get("name")).thenReturn("projectName");
        when(form.get("url")).thenReturn("projectURL");
        when(form.get("institute")).thenReturn("projectInstitut");
        when(form.get("description")).thenReturn("projectDescription");
        when(form.get("teamCount")).thenReturn("2");
        when(form.get("minSize")).thenReturn("3");
        when(form.get("maxSize")).thenReturn("4");

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

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird.
     */
    @Test
    public void testIdValidationExceptionInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn("a");
        when(messages.at("INTERNAL_ERROR")).thenReturn("Exception");

        controller.editProject();
        System.out.println(Context.current().flash());

        assertTrue(Context.current().flash().containsValue("Exception"));
    }

    /**
     * Test für den Fall, dass das Projekt null ist.
     */
    @Test
    public void testNullProject() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId() + 1));
        when(messages.at("error.project.deletedConcurrently")).thenReturn("Null Project");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Null Project"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird.
     */
    @Test
    public void testSecondValidationException() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.get("name")).thenReturn(new String());
        when(messages.at("error.wrongInput")).thenReturn("Exception");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Exception"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird. (Min Team Size)
     */
    @Test
    public void testInvalidTeamSizesXOR() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.get("name")).thenReturn(project.getName());
        when(form.get("url")).thenReturn("test");
        when(form.get("institute")).thenReturn("test");
        when(form.get("description")).thenReturn("test");
        when(form.get("teamCount")).thenReturn("1");
        when(form.get("minSize")).thenReturn("0");
        when(form.get("maxSize")).thenReturn("1");
        when(messages.at("error.wrongInput")).thenReturn("Wrong Input");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Wrong Input"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird. (Max Team Size)
     */
    @Test
    public void testInvalidTeamSizesMaxLesserMin() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.get("name")).thenReturn(project.getName());
        when(form.get("url")).thenReturn("test");
        when(form.get("institute")).thenReturn("test");
        when(form.get("description")).thenReturn("test");
        when(form.get("teamCount")).thenReturn("1");
        when(form.get("minSize")).thenReturn("2");
        when(form.get("maxSize")).thenReturn("1");
        when(messages.at("error.wrongInput")).thenReturn("Wrong Input");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Wrong Input"));
    }

    /**
     * Test ob beim Editieren mit falschen Bedingungen eine Exception geworfen
     * wird. NumberFormatException
     */
    @Test
    public void testNFEInEdit() {
        Map<String, String> data = new HashMap<>();
        data.put("adviser-multiselect", "a");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.get("name")).thenReturn(project.getName());
        when(form.get("url")).thenReturn("test");
        when(form.get("institute")).thenReturn("test");
        when(form.get("description")).thenReturn("test");
        when(form.get("teamCount")).thenReturn("1");
        when(form.get("minSize")).thenReturn("1");
        when(form.get("maxSize")).thenReturn("2");
        when(messages.at("error.internalError")).thenReturn("Exception");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Exception"));
    }

    /**
     * Test ob ein Adviser null ist.
     */
    @Test
    public void testNullAdviser() {
        Map<String, String> data = new HashMap<>();
        data.put("adviser-multiselect", "19");

        when(form.data()).thenReturn(data);
        when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        when(form.get("name")).thenReturn(project.getName());
        when(form.get("url")).thenReturn("test");
        when(form.get("institute")).thenReturn("test");
        when(form.get("description")).thenReturn("test");
        when(form.get("teamCount")).thenReturn("1");
        when(form.get("minSize")).thenReturn("1");
        when(form.get("maxSize")).thenReturn("2");
        when(messages.at("error.adviser.deletedConcurrently")).thenReturn("Null Adviser");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("Null Adviser"));
    }

    /**
     * Test für das Entfernen eines Projektes.
     * 
     * Funktioniert nicht, da EBean Probleme mit der Datenbank hat.
     */
    @Test
    @Ignore
    public void removeProjectTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.data()).thenReturn(map);

        controller.removeProject();

        assertTrue(GeneralData.loadInstance().getCurrentSemester().getProjects().isEmpty());
        assertNull(ElipseModel.getById(Project.class, project.getId()));
    }
}
