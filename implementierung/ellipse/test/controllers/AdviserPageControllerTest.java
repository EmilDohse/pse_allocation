package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import data.Allocation;
import data.DataTest;
import data.GeneralData;
import data.Grade;
import data.Project;
import data.Semester;
import data.Student;
import data.Team;
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

/**
 * Diese Klasse beinhaltet Tests für den AdminPropertiesController.
 */
public class AdviserPageControllerTest extends ControllerTest {

    @Mock
    UserManagement        userManagement;

    @InjectMocks
    AdviserPageController controller;

    private Semester      semester;
    private Adviser       firstAdviser;
    private Adviser       secondAdviser;
    private Project       project;

    /**
     * Initialisierung der Testdaten.
     */
    @Override
    @Before
    public void before() {

        super.before();

        firstAdviser = new Adviser();
        firstAdviser.doTransaction(() -> {
            firstAdviser.setEmailAddress("testemail");
            firstAdviser.setFirstName("firstName");
            firstAdviser.setLastName("lastName");
            firstAdviser.setPassword((new BlowfishPasswordEncoder()).encode("password"));
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

    /**
     * Test für das Hinzufügen eines Projektes.
     */
    @Test
    public void addProjectTest() {

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        controller.addProject();

        semester.refresh();

        assertEquals(semester.getProjects().size(), 2);
        int temp = semester.getProjects().indexOf(project);
        Project newProject = semester.getProjects().get(1 - temp);
        assertNotNull(newProject);
        assertEquals(newProject.getName(),
                "new Project " + firstAdviser.getFirstName() + " " + firstAdviser.getLastName());
        assertEquals(newProject.getAdvisers().size(), 1);
        assertEquals(newProject.getAdvisers().get(0), firstAdviser);

    }

    /**
     * Test ob das Hinzufügen eines Projektes ohne aktuelles Semester
     * fehlschlägt.
     */
    @Test
    public void addProjectNoCurrentSemesterTest() {

        GeneralData d = GeneralData.loadInstance();
        d.doTransaction(() -> {
            d.setCurrentSemester(null);
        });

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.addProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Editieren des Accounts.
     */
    @Test
    public void editAccountTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("newpassword");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("newpassword");
        Mockito.when(form.get("emailChange")).thenReturn("NotNull");
        Mockito.when(form.get("newEmail")).thenReturn("new@email");

        controller.editAccount();

        firstAdviser.refresh();

        assertTrue((new BlowfishPasswordEncoder()).matches("newpassword", firstAdviser.getPassword()));
        assertEquals(firstAdviser.getEmailAddress(), "new@email");

    }

    /**
     * Test ob das Ändern des Passworts fehlschlägt, falls die
     * Passwortwiederholung falsch ist.
     */
    @Test
    public void editAccountWrongPasswordRepeatTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("newpassword");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("error");
        Mockito.when(form.get("emailChange")).thenReturn("NotNull");
        Mockito.when(form.get("newEmail")).thenReturn("new@email");

        Mockito.when(messages.at("adviser.account.error.passwords")).thenReturn("error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob das Ändern des Passworts fehlschlägt, falls das neue Passwort
     * nicht valide ist.
     */
    @Test
    public void editAccountValidationExceptionPasswordChangeTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("1234");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("1234");
        Mockito.when(form.get("emailChange")).thenReturn("NotNull");
        Mockito.when(form.get("newEmail")).thenReturn("new@email");

        Mockito.when(messages.at("general.error.minimalPasswordLength")).thenReturn("error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Ändern der Email fehlschlägt, falls die neue E-Mail nicht
     * valide ist.
     */
    @Test
    public void editAccountValidationExceptionEmailChangeTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("passwordChange")).thenReturn("NotNull");
        Mockito.when(form.get("oldPassword")).thenReturn("password");
        Mockito.when(form.get("newPassword")).thenReturn("newpassword");
        Mockito.when(form.get("newPasswordRepeat")).thenReturn("newpassword");
        Mockito.when(form.get("emailChange")).thenReturn("NotNull");
        Mockito.when(form.get("newEmail")).thenReturn("error");

        Mockito.when(messages.at("user.noValidEmail")).thenReturn("error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Editieren eines Projektes.
     */
    @Test
    public void editProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
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

    /**
     * Test ob das Editieren fehlschlägt, falls eine Eingabe nicht valide ist.
     */
    @Test
    public void editProjectValidationExceptionProjectIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn("abc");
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Editieren fehlschlägt, wenn die Projekt ID nicht bekannt ist.
     */
    @Test
    public void editProjectUnknownProjectIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId() + 1));
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("error.project.deletedConcurrently")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Editieren fehlschlägt, wenn bei der Eingabe eine falsche Zahl
     * vorkommt.
     */
    @Test
    public void editProjectValidationExceptionNumberOfTeamsTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get("teamCount")).thenReturn("abc");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Editieren fehlschlägt, wenn die minsize größer als die
     * maxsize ist.
     */
    @Test
    public void editProjectMinGreaterMaxTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("4");
        Mockito.when(form.get("maxSize")).thenReturn("3");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("error.wrongInput")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Editieren fehlschlägt, falls kein Betreuer im Projekt
     * vorhanden ist.
     */
    @Test
    public void editProjectNotAdviserOfProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", String.valueOf(secondAdviser.getId()));

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(secondAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Editieren fehlschlägt, falls die Adviser ID ein invalides
     * Format hat.
     */
    @Test
    public void editProjectValidationExceptionAdviserIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("adviser-multiselect1", "abc");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);
        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get("teamCount")).thenReturn("2");
        Mockito.when(form.get("minSize")).thenReturn("3");
        Mockito.when(form.get("maxSize")).thenReturn("4");
        Mockito.when(form.get("name")).thenReturn("projectName");
        Mockito.when(form.get("url")).thenReturn("projectUrl");
        Mockito.when(form.get("institute")).thenReturn("projectInstitute");
        Mockito.when(form.get("description")).thenReturn("projectInfo");

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.editProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Beitreten in ein Projekt.
     */
    @Test
    public void joinProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(secondAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));

        controller.joinProject();

        project.refresh();

        assertEquals(project.getAdvisers().size(), 2);
        assertTrue(project.getAdvisers().contains(firstAdviser));
        assertTrue(project.getAdvisers().contains(secondAdviser));
    }

    /**
     * Test für das Beitreten zu einem Projekt, falls die Projekt Id nicht
     * bekannt ist.
     */
    @Test
    public void joinProjectUnknownProjectIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(secondAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId() + 1));

        Mockito.when(messages.at("error.project.deletedConcurrently")).thenReturn("error");

        controller.joinProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test ob das Betreuen eines Teams fehlschlägt, welches man bereits
     * betreut.
     */
    @Test
    public void joinProjectAlreadyAdviserOfProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.joinProject();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Test für das Verlassen eines Projekts.
     */
    @Test
    public void leaveProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));

        controller.leaveProject();

        project.refresh();

        assertTrue(project.getAdvisers().isEmpty());
    }

    /**
     * Test ob das Verlassen eines Projekts mit invalidem Eingabeformat
     * fehlschlägt.
     */
    @Test
    public void leaveProjectValidationExceptionTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn("abc");

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.leaveProject();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob das Verlassen eines Projekts mit unbekannter Projekt-Id
     * fehlschlägt.
     */
    @Test
    public void leaveProjectUnknownProjectIdTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId() + 1));

        Mockito.when(messages.at("error.project.deletedConcurrently")).thenReturn("error");

        controller.leaveProject();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob das Verlassen eines Projektes fehlschlägt, welches man garnicht
     * betreut.
     */
    @Test
    public void leaveProjectNotAdviserOfProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(secondAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.leaveProject();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test für das Entfernen eines Projekts.
     */
    @Test
    public void removeProjectTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));

        controller.removeProject();

        assertTrue(Project.getProjects().isEmpty());
    }

    /**
     * Test für das Sichern der Noten der Studenten.
     */
    @Test
    public void saveStudentsGradesTest() {

        Student s = new Student();
        s.save();
        s.doTransaction(() -> {
            s.setGradePSE(Grade.UNKNOWN);
            s.setGradeTSE(Grade.UNKNOWN);
        });

        Allocation a = new Allocation();
        a.save();

        Team t = new Team();
        t.addMember(s);
        t.setProject(project);

        List<Team> teams = new ArrayList<>();
        teams.add(t);

        a.doTransaction(() -> {
            a.setTeams(teams);
        });

        semester.doTransaction(() -> {
            semester.setFinalAllocation(a);
        });

        s.refresh();

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get(String.valueOf(s.getId()) + "-pseGrade")).thenReturn("500");
        Mockito.when(form.get(String.valueOf(s.getId()) + "-tseGrade")).thenReturn("230");

        controller.saveStudentsGrades();

        s.refresh();

        assertEquals(s.getGradePSE(), Grade.FIVE_ZERO);
        assertEquals(s.getGradeTSE(), Grade.TWO_THREE);

    }

    /**
     * Test ob das Eintragen von Noten in falschem Format fehlschlägt.
     */
    @Test
    public void saveStudentsGradesValidationExceptionTest() {

        Student s = new Student();
        s.save();
        s.doTransaction(() -> {
            s.setGradePSE(Grade.UNKNOWN);
            s.setGradeTSE(Grade.UNKNOWN);
        });

        Allocation a = new Allocation();
        a.save();

        Team t = new Team();
        t.addMember(s);
        t.setProject(project);

        List<Team> teams = new ArrayList<>();
        teams.add(t);

        a.doTransaction(() -> {
            a.setTeams(teams);
        });

        semester.doTransaction(() -> {
            semester.setFinalAllocation(a);
        });

        s.refresh();

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn("abc");
        Mockito.when(form.get(String.valueOf(s.getId()) + "-pseGrade")).thenReturn("500");
        Mockito.when(form.get(String.valueOf(s.getId()) + "-tseGrade")).thenReturn("230");

        Mockito.when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.saveStudentsGrades();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob das Eintragen von Noten für Studenten fehlschlägt, falls man sie
     * nicht betreut.
     */
    @Test
    public void saveStudentsGradesNotAdviserOfProjectTest() {

        Student s = new Student();
        s.save();
        s.doTransaction(() -> {
            s.setGradePSE(Grade.UNKNOWN);
            s.setGradeTSE(Grade.UNKNOWN);
        });

        Allocation a = new Allocation();
        a.save();

        Team t = new Team();
        t.addMember(s);
        t.setProject(project);

        List<Team> teams = new ArrayList<>();
        teams.add(t);

        a.doTransaction(() -> {
            a.setTeams(teams);
        });

        semester.doTransaction(() -> {
            semester.setFinalAllocation(a);
        });

        s.refresh();

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(secondAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get(String.valueOf(s.getId()) + "-pseGrade")).thenReturn("500");
        Mockito.when(form.get(String.valueOf(s.getId()) + "-tseGrade")).thenReturn("230");

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.saveStudentsGrades();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Test ob das Eintragen der Noten fehlschlägt, falls noch keine finale
     * Einteilung existiert.
     */
    @Test
    public void saveStudentsGradesNoFinalAllocationTest() {

        Student s = new Student();
        s.save();
        s.doTransaction(() -> {
            s.setGradePSE(Grade.UNKNOWN);
            s.setGradeTSE(Grade.UNKNOWN);
        });

        Allocation a = new Allocation();
        a.save();

        Team t = new Team();
        t.addMember(s);
        t.setProject(project);

        List<Team> teams = new ArrayList<>();
        teams.add(t);

        a.doTransaction(() -> {
            a.setTeams(teams);
        });

        s.refresh();

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.data()).thenReturn(map);

        Mockito.when(userManagement.getUserProfile(Mockito.any(Context.class))).thenReturn(firstAdviser);

        Mockito.when(form.get("id")).thenReturn(String.valueOf(project.getId()));
        Mockito.when(form.get(String.valueOf(s.getId()) + "-pseGrade")).thenReturn("500");
        Mockito.when(form.get(String.valueOf(s.getId()) + "-tseGrade")).thenReturn("230");

        Mockito.when(messages.at("error.internalError")).thenReturn("error");

        controller.saveStudentsGrades();

        assertTrue(Context.current().flash().containsValue("error"));

    }
}
