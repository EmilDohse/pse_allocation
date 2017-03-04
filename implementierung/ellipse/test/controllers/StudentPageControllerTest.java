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

@RunWith(MockitoJUnitRunner.class)
public class StudentPageControllerTest extends ControllerTest {

    @Mock
    UserManagement        userManagement;
    
    @Mock
    Notifier                        notifier;

    @InjectMocks
    StudentPageController controller;

    private static final String     INTERNAL_ERROR = "error.internalError";

    private Student       secondStudent;
    private Semester      semester;
    private SPO           spo;
    private Achievement   achievNecessary;
    private Achievement   achievAdditional;
    private Student       student;
    private LearningGroup learningGroup;
    private Project       firstProject;
    private Project       secondProject;
    private BlowfishPasswordEncoder enc;

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
        when(form.get("semester"))
                .thenReturn(String.valueOf(studySemester));
        when(form.get("spo")).thenReturn(String.valueOf(spo.getId()));
        when(form.get("trueData")).thenReturn(String.valueOf(true));
        when(form.data()).thenReturn(map);

        when(userManagement.getUserProfile(any()))
                .thenReturn(student);
        
        // Chnage Data findet einmal am Anfang des neuen Semesters statt
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
        assertTrue(
                student.getCompletedAchievements().contains(achievNecessary));
        assertEquals(1, student.getCompletedAchievements().size());
        assertTrue(
                student.getOralTestAchievements().contains(achievAdditional));
        assertEquals(1, student.getOralTestAchievements().size());
        assertTrue(newSemester.getStudents().contains(student));
        LearningGroup lg = newSemester.getLearningGroupOf(student);
        lg.refresh();
        assertEquals(student.getUserName(),
                lg.getName());
        assertTrue(lg.isPrivate());

        newSemester.getProjects().forEach(p -> {
            assertEquals(3, lg.getRating(p));
        });
    }

    @Test
    public void rateTest() {
        when(userManagement.getUserProfile(any()))
                .thenReturn(student);

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);

        when(form.get(String.valueOf(firstProject.getId())))
                .thenReturn("5");
        when(form.get(String.valueOf(secondProject.getId())))
                .thenReturn("1");

        controller.rate();

        LearningGroup lg = semester.getLearningGroupOf(student);
        lg.refresh();
        assertEquals(5, lg.getRating(firstProject));
        assertEquals(1, lg.getRating(secondProject));

    }

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

    @Test
    public void createLearningGroupTest() {
        when(userManagement.getUserProfile(any()))
                .thenReturn(student);

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
        
        when(messages.at(INTERNAL_ERROR)).thenReturn("");

        controller.editAccount();
        
        student.refresh();

        assertTrue(enc.matches(newPassword, student.getPassword()));
        assertEquals(newEmail, student.getEmailAddress());
    }

    @Test
    public void sendNewVerificationLink()
            throws AddressException, IOException, MessagingException,
            EmailException {
        when(userManagement.getUserProfile(any())).thenReturn(student);
        Mailbox.clearAll();
        String address = "a@b.de";
        student.doTransaction(() -> {
            student.setEmailAddress(address);
        });
        when(messages.at("student.email.verificationLinkSuccess"))
                .thenReturn("");

        controller.sendNewVerificationLink();

        Mockito.verify(notifier, Mockito.times(1)).sendVerificationMail(
                Mockito.eq(student),
                Mockito.matches("https?://.*/verify\\d*"));
    }

}