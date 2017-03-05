package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.mock_javamail.Mailbox;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import play.mvc.Http.Context;
import data.Achievement;
import data.GeneralData;
import data.LearningGroup;
import data.Project;
import data.SPO;
import data.Semester;
import data.Student;
import notificationSystem.Notifier;
import security.BlowfishPasswordEncoder;
import security.UserManagement;

/**
 * Testklasse zum StudentPageController
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class StudentPageControllerTest extends ControllerTest {

    @Mock
    UserManagement                  userManagement;

    @Mock
    Notifier                        notifier;

    @InjectMocks
    StudentPageController           controller;

    private static final String     INTERNAL_ERROR = "error.internalError";

    private Student                 secondStudent;
    private Semester                semester;
    private SPO                     spo;
    private Achievement             achievNecessary;
    private Achievement             achievAdditional;
    private Student                 student;
    private LearningGroup           learningGroup;
    private Project                 firstProject;
    private Project                 secondProject;
    private BlowfishPasswordEncoder enc;

    /**
     * Legt alle Daten für die Tests an
     */
    @Override
    @Before
    public void before() {
        super.before();
        enc = new BlowfishPasswordEncoder();
        semester = GeneralData.loadInstance().getCurrentSemester();
        spo = new SPO();
        spo.save();
        achievNecessary = new Achievement("Necessary");
        achievNecessary.save();
        achievAdditional = new Achievement("Additional");
        achievAdditional.save();
        student = new Student();
        student.save();
        secondStudent = new Student();
        secondStudent.save();
        firstProject = new Project();
        firstProject.save();
        secondProject = new Project();
        secondProject.save();
        learningGroup = new LearningGroup();
        learningGroup.save();

        semester.doTransaction(() -> {
            semester.addStudent(student);
            semester.addStudent(secondStudent);
            semester.addSPO(spo);
            semester.addProject(firstProject);
            semester.addProject(secondProject);
            semester.addLearningGroup(learningGroup);
        });
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(achievNecessary);
            spo.addAdditionalAchievement(achievAdditional);
        });
        learningGroup.doTransaction(() -> {
            learningGroup.addMember(student);
            learningGroup.setPrivate(true);
        });

        semester.refresh();
        spo.refresh();
        achievAdditional.refresh();
        achievNecessary.refresh();
        student.refresh();
    }

    /**
     * Testet die Methode changeData
     */
    @Test
    public void changeDataTest() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-" + spo.getId() + "-multiselect",
                String.valueOf(achievNecessary.getId()));
        map.put("due-" + spo.getId() + "-multiselect",
                String.valueOf(achievAdditional.getId()));
        when(form.data()).thenReturn(map);

        student.doTransaction(() -> {
            student.setSemester(1);
        });
        int studySemester = 2;
        when(form.get("semester")).thenReturn(String.valueOf(studySemester));
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(String.valueOf(true));
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any())).thenReturn(student);

        // Change Data findet einmal am Anfang des neuen Semesters statt
        Semester newSemester = new Semester();
        newSemester.doTransaction(() -> {
            newSemester.addSPO(spo);
            newSemester.addProject(firstProject);
            newSemester.addProject(secondProject);
        });

        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(newSemester);
        });

        controller.changeData();

        student.refresh();
        spo.refresh();
        newSemester.refresh();

        assertEquals(spo, student.getSPO());
        assertEquals(2, student.getSemester());
        assertTrue(student.getCompletedAchievements().contains(achievNecessary));
        assertEquals(1, student.getCompletedAchievements().size());
        assertTrue(student.getOralTestAchievements().contains(achievAdditional));
        assertEquals(1, student.getOralTestAchievements().size());
        assertTrue(newSemester.getStudents().contains(student));
        LearningGroup lg = newSemester.getLearningGroupOf(student);
        lg.refresh();
        assertEquals(student.getUserName(), lg.getName());
        assertTrue(lg.isPrivate());

        newSemester.getProjects().forEach(p -> {
            assertEquals(3, lg.getRating(p));
        });
    }

    /**
     * Testet die Methode changeData, wenn TrueData nicht gesetzt ist
     */
    @Test
    public void changeDataNotTrueDataTest() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-" + spo.getId() + "-multiselect",
                String.valueOf(achievNecessary.getId()));
        map.put("due-" + spo.getId() + "-multiselect",
                String.valueOf(achievAdditional.getId()));
        when(form.data()).thenReturn(map);

        student.doTransaction(() -> {
            student.setSemester(1);
        });
        int studySemester = 2;
        when(form.get("semester")).thenReturn(String.valueOf(studySemester));
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(null);
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any())).thenReturn(student);

        // Change Data findet einmal am Anfang des neuen Semesters statt
        Semester newSemester = new Semester();
        newSemester.doTransaction(() -> {
            newSemester.addSPO(spo);
            newSemester.addProject(firstProject);
            newSemester.addProject(secondProject);
        });

        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(newSemester);
        });

        when(messages.at("error.registration.trueDate")).thenReturn("error");

        controller.changeData();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet, ob die Methode changeData fehlschlägt mit falschen Eingabedaten
     */
    @Test
    public void changeDataValidationExceptionSemesterIdTest() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-" + spo.getId() + "-multiselect",
                String.valueOf(achievNecessary.getId()));
        map.put("due-" + spo.getId() + "-multiselect",
                String.valueOf(achievAdditional.getId()));
        when(form.data()).thenReturn(map);

        student.doTransaction(() -> {
            student.setSemester(1);
        });
        int studySemester = 2;
        when(form.get("semester")).thenReturn("abc");
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(String.valueOf(true));
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any())).thenReturn(student);

        // Change Data findet einmal am Anfang des neuen Semesters statt
        Semester newSemester = new Semester();
        newSemester.doTransaction(() -> {
            newSemester.addSPO(spo);
            newSemester.addProject(firstProject);
            newSemester.addProject(secondProject);
        });

        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(newSemester);
        });

        when(messages.at("INTERNAL_ERROR")).thenReturn("error");

        controller.changeData();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet, ob die Methode changeData fehlschlägt mit falschen Eingabedaten
     */
    @Test
    public void changeDataValidationExceptionCompletedAchievementIdTest() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-" + spo.getId() + "-multiselect", "abc");
        map.put("due-" + spo.getId() + "-multiselect",
                String.valueOf(achievAdditional.getId()));
        when(form.data()).thenReturn(map);

        student.doTransaction(() -> {
            student.setSemester(1);
        });
        int studySemester = 2;
        when(form.get("semester")).thenReturn(String.valueOf(studySemester));
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(String.valueOf(true));
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any())).thenReturn(student);

        // Change Data findet einmal am Anfang des neuen Semesters statt
        Semester newSemester = new Semester();
        newSemester.doTransaction(() -> {
            newSemester.addSPO(spo);
            newSemester.addProject(firstProject);
            newSemester.addProject(secondProject);
        });

        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(newSemester);
        });

        when(messages.at("error.internalError")).thenReturn("error");

        controller.changeData();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet, ob die Methode changeData fehlschlägt mit falschen Eingabedaten
     */
    @Test
    public void changeDataValidationExceptionDueAchievementIdTest() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-" + spo.getId() + "-multiselect",
                String.valueOf(achievNecessary.getId()));
        map.put("due-" + spo.getId() + "-multiselect", "abc");
        when(form.data()).thenReturn(map);

        student.doTransaction(() -> {
            student.setSemester(1);
        });
        int studySemester = 2;
        when(form.get("semester")).thenReturn(String.valueOf(studySemester));
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(String.valueOf(true));
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any())).thenReturn(student);

        // Change Data findet einmal am Anfang des neuen Semesters statt
        Semester newSemester = new Semester();
        newSemester.doTransaction(() -> {
            newSemester.addSPO(spo);
            newSemester.addProject(firstProject);
            newSemester.addProject(secondProject);
        });

        GeneralData data = GeneralData.loadInstance();
        data.doTransaction(() -> {
            data.setCurrentSemester(newSemester);
        });

        when(messages.at("error.internalError")).thenReturn("error");

        controller.changeData();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode rate
     */
    @Test
    public void rateTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);

        when(form.get(String.valueOf(firstProject.getId()))).thenReturn("5");
        when(form.get(String.valueOf(secondProject.getId()))).thenReturn("1");

        controller.rate();

        LearningGroup lg = semester.getLearningGroupOf(student);
        lg.refresh();
        assertEquals(5, lg.getRating(firstProject));
        assertEquals(1, lg.getRating(secondProject));

    }

    /**
     * Testet die Methode setLearningGroup ohne Aktion zum weiterverlinken
     */
    @Test
    public void setLearningGroupNoActionTest() {

        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);

        when(messages.at("error.internalError")).thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));
    }

    /**
     * Testet die Methode joinLearningGroup
     */
    @Test
    public void joinLearningGroupTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn(goupName);
        when(form.get("learningGroupPassword")).thenReturn(password);

        controller.setLearningGroup();

        semester.refresh();

        assertEquals(lg, semester.getLearningGroupOf(student));
        assertFalse(semester.getLearningGroups().contains(learningGroup));

    }

    /**
     * Testet die Methode joinLearningGroup mit falschen Eingabedaten
     */
    @Test
    public void joinLearningGroupValidationExceptionTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn("");
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("general.error.noEmptyString")).thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode joinLearningGroup mit einem unbekannten
     * Lerngruppennamen
     */
    @Test
    public void joinLearningGroupUnknownLearningGroupNameTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn("Heinz-Karl");
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(
                messages.at("student.learningGroup.error.learningGroupDoesntExist"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode joinLearningGroup, wenn die Lerngruppe schon voll ist
     */
    @Test
    public void joinLearningGroupTooManyMembersTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
            semester.setMaxGroupSize(0);
        });
        when(form.get("learningGroupname")).thenReturn(goupName);
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.learningGroupFull"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode joinLearningGroup, wenn der Student schon in einer
     * Lerngruppe ist
     */
    @Test
    public void joinLearningGroupAlreadyInLearningGroupTest() {
        when(userManagement.getUserProfile(any())).thenReturn(secondStudent);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn(goupName);
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.alreadyInOtherGroup"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode joinLearningGroup, wenn die Lerngruppe privat ist
     */
    @Test
    public void joinLearningGroupPrivateTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
            lg.setPrivate(true);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn(goupName);
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.joinProhibited"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode joinLearningGroup mit falschem Passwort
     */
    @Test
    public void joinLearningGroupWrongPasswordTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("join")).thenReturn("");

        String goupName = "Karl-Heinz";
        String password = "eneusvbewosa83";
        LearningGroup lg = new LearningGroup();
        lg.doTransaction(() -> {
            lg.setName(goupName);
            lg.savePassword(password);
            lg.addMember(secondStudent);
        });
        semester.doTransaction(() -> {
            semester.addLearningGroup(lg);
        });
        when(form.get("learningGroupname")).thenReturn(goupName);
        when(form.get("learningGroupPassword")).thenReturn("error");

        when(messages.at("student.learningGroup.error.wrongPW")).thenReturn(
                "error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode createLearningGroup
     */
    @Test
    public void createLearningGroupTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("create")).thenReturn("");
        String name = "Karl-Heinz";
        when(form.get("learningGroupname")).thenReturn(name);
        String password = "ehushosehgesoleugvekfnkvpaoie";
        when(form.get("learningGroupPassword")).thenReturn(password);

        controller.setLearningGroup();

        semester.refresh();
        LearningGroup lg = semester.getLearningGroupOf(student);
        lg.refresh();

        assertFalse(lg.equals(learningGroup));
        assertFalse(semester.getLearningGroups().contains(learningGroup));
        assertEquals(name, lg.getName());
        assertTrue(enc.matches(password, lg.getPassword()));

    }

    /**
     * Testet die Methode createLearningGroup, wenn der Student davor schon in
     * einer Lerngruppe ist
     */
    @Test
    public void createLearningGroupNotPrivateTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        learningGroup.doTransaction(() -> {
            learningGroup.setPrivate(false);
        });

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("create")).thenReturn("");
        String name = "Karl-Heinz";
        when(form.get("learningGroupname")).thenReturn(name);
        String password = "ehushosehgesoleugvekfnkvpaoie";
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.alreadyInOtherGroup"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode createLearningGroup mit unerlaubten Lerngruppennamen
     */
    @Test
    public void createLearningGroupIllegalNameTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("create")).thenReturn("");
        String name = "1234";
        when(form.get("learningGroupname")).thenReturn(name);
        String password = "ehushosehgesoleugvekfnkvpaoie";
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.nameFormat")).thenReturn(
                "error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode createLearningGroup mit unerlaubtem
     * Lerngruppenpasswort
     */
    @Test
    public void createLearningGroupIllegalPasswordTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("create")).thenReturn("");
        String name = "Karl-Heinz";
        when(form.get("learningGroupname")).thenReturn(name);
        String password = "1234";
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("general.error.minimalPasswordLength")).thenReturn(
                "error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode createLearningGroup, wenn bereits eine Lerngruppe mit
     * dem selben Namen existiert
     */
    @Test
    public void createLearningGroupAlreadyExistsTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        learningGroup.doTransaction(() -> {
            learningGroup.setName("abc");
        });

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("create")).thenReturn("");
        String name = "abc";
        when(form.get("learningGroupname")).thenReturn(name);
        String password = "ehushosehgesoleugvekfnkvpaoie";
        when(form.get("learningGroupPassword")).thenReturn(password);

        when(messages.at("student.learningGroup.error.existsAlready"))
                .thenReturn("error");

        controller.setLearningGroup();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode editAccount
     */
    @Test
    public void editAccountTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("emailChange")).thenReturn("");
        when(form.get("passwordChange")).thenReturn("");
        String newPassword = "euveslfouigzhj876tzf";
        String oldPassword = "esvhzugjhbnjhju7";
        student.doTransaction(() -> {
            student.savePassword(oldPassword);
        });
        when(form.get("oldPassword")).thenReturn(oldPassword);
        when(form.get("newPassword")).thenReturn(newPassword);
        when(form.get("newPasswordRepeat")).thenReturn(newPassword);

        String newEmail = "a@b.de";
        when(form.get("newEmail")).thenReturn(newEmail);

        controller.editAccount();

        student.refresh();

        assertTrue(enc.matches(newPassword, student.getPassword()));
        assertEquals(newEmail, student.getEmailAddress());
    }

    /**
     * Testet die Methode editAccount mit unerlaubtem Passwort
     */
    @Test
    public void editAccountIllegalPasswordTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("emailChange")).thenReturn("");
        when(form.get("passwordChange")).thenReturn("");
        String newPassword = "1234";
        String oldPassword = "esvhzugjhbnjhju7";
        student.doTransaction(() -> {
            student.savePassword(oldPassword);
        });
        when(form.get("oldPassword")).thenReturn(oldPassword);
        when(form.get("newPassword")).thenReturn(newPassword);
        when(form.get("newPasswordRepeat")).thenReturn(newPassword);

        String newEmail = "a@b.de";
        when(form.get("newEmail")).thenReturn(newEmail);

        when(messages.at("general.error.minimalPasswordLength")).thenReturn(
                "error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode editAccount mit falschem Passwort
     */
    @Test
    public void editAccountWrongPasswordTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("emailChange")).thenReturn("");
        when(form.get("passwordChange")).thenReturn("");
        String newPassword = "euveslfouigzhj876tzf";
        String oldPassword = "esvhzugjhbnjhju7";
        student.doTransaction(() -> {
            student.savePassword(oldPassword);
        });
        when(form.get("oldPassword")).thenReturn("abc");
        when(form.get("newPassword")).thenReturn(newPassword);
        when(form.get("newPasswordRepeat")).thenReturn(newPassword);

        String newEmail = "a@b.de";
        when(form.get("newEmail")).thenReturn(newEmail);

        when(messages.at("student.account.error.pwsDontMatch")).thenReturn(
                "error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode editAccount mit falschem wiederholtem Passwort
     */
    @Test
    public void editAccountWrongPasswordRepeatTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("emailChange")).thenReturn("");
        when(form.get("passwordChange")).thenReturn("");
        String newPassword = "euveslfouigzhj876tzf";
        String oldPassword = "esvhzugjhbnjhju7";
        student.doTransaction(() -> {
            student.savePassword(oldPassword);
        });
        when(form.get("oldPassword")).thenReturn(oldPassword);
        when(form.get("newPassword")).thenReturn(newPassword);
        when(form.get("newPasswordRepeat")).thenReturn("abc");

        String newEmail = "a@b.de";
        when(form.get("newEmail")).thenReturn(newEmail);

        when(messages.at("student.account.error.wrongPW")).thenReturn("error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode editAccount mit unerlaubter Email-Adresse
     */
    @Test
    public void editAccountIllegalEmailTest() {
        when(userManagement.getUserProfile(any())).thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("emailChange")).thenReturn("");
        when(form.get("passwordChange")).thenReturn("");
        String newPassword = "euveslfouigzhj876tzf";
        String oldPassword = "esvhzugjhbnjhju7";
        student.doTransaction(() -> {
            student.savePassword(oldPassword);
        });
        when(form.get("oldPassword")).thenReturn(oldPassword);
        when(form.get("newPassword")).thenReturn(newPassword);
        when(form.get("newPasswordRepeat")).thenReturn(newPassword);

        String newEmail = "abc";
        when(form.get("newEmail")).thenReturn(newEmail);

        when(messages.at("user.noValidEmail")).thenReturn("error");

        controller.editAccount();

        assertTrue(Context.current().flash().containsValue("error"));

    }

    /**
     * Testet die Methode sendNewVerificationLink
     */
    @Test
    public void sendNewVerificationLink() throws AddressException, IOException,
            MessagingException, EmailException {
        when(userManagement.getUserProfile(any())).thenReturn(student);
        Mailbox.clearAll();
        String address = "a@b.de";
        student.doTransaction(() -> {
            student.setEmailAddress(address);
        });
        when(messages.at("student.email.verificationLinkSuccess")).thenReturn(
                "");

        controller.sendNewVerificationLink();

        Mockito.verify(notifier, Mockito.times(1)).sendVerificationMail(
                Mockito.eq(student), Mockito.matches("https?://.*/verify\\d*"));
    }

}