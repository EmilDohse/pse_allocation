package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import data.Achievement;
import data.GeneralData;
import data.LearningGroup;
import data.SPO;
import data.Semester;
import data.Student;
import data.User;
import notificationSystem.Notifier;
import play.mvc.Http.Context;
import security.BlowfishPasswordEncoder;
import security.EmailVerifier;
import security.PasswordResetter;

public class IndexPageControllerTest extends ControllerTest {

    @InjectMocks
    IndexPageController controller;

    @Mock
    Notifier            notifier;

    @Test
    public void testRegister() throws EmailException {
        Map<String, String> map = new HashMap<>();
        map.put("completed-1-multiselect", "1");
        map.put("due-1-multiselect", "2");
        when(form.data()).thenReturn(map);

        SPO spo = new SPO();
        Achievement one = new Achievement();
        Achievement two = new Achievement();
        one.save();
        two.save();
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(one);
            spo.addAdditionalAchievement(two);
        });

        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("student.email.verificationLinkSuccess"))
                .thenReturn("Email Success");

        controller.register();

        assertEquals(1, GeneralData.loadInstance().getCurrentSemester()
                .getStudents().size());
        Student student = Student.getStudent(123456);
        assertEquals("Hans", student.getFirstName());
        assertEquals("Peter", student.getLastName());
        assertEquals("a@b", student.getEmailAddress());
        assertTrue(new BlowfishPasswordEncoder().matches("123456",
                student.getPassword()));
        assertEquals(spo, student.getSPO());
        assertEquals(3, student.getSemester());
        assertEquals(1, GeneralData.loadInstance().getCurrentSemester()
                .getLearningGroups().size());
        LearningGroup lg = GeneralData.loadInstance().getCurrentSemester()
                .getLearningGroupOf(student);
        assertTrue(lg.isPrivate());
        assertTrue(lg.getMembers().contains(student));
        assertEquals(1, student.getCompletedAchievements().size());
        assertEquals(1, student.getOralTestAchievements().size());
        assertEquals(one, student.getCompletedAchievements().get(0));
        assertEquals(two, student.getOralTestAchievements().get(0));
        verify(notifier).sendVerificationMail(any(Student.class),
                any(String.class));
        assertTrue(Context.current().flash().containsValue("Email Success"));
    }

    @Test
    public void testValidationExceptionInRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn(new String());
        when(messages.at("general.error.noEmptyString"))
                .thenReturn("Empty String");

        controller.register();

        assertTrue(Context.current().flash().containsValue("Empty String"));
    }

    @Test
    public void testUntrueData() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn(null);
        when(messages.at("index.registration.error.FalseData"))
                .thenReturn("FalseData");

        controller.register();

        assertTrue(Context.current().flash().containsValue("FalseData"));
    }

    // NFE = NumberFormatException

    @Test
    public void testFirstNFEInRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-1-multiselect", "a");

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("error.internalError")).thenReturn("Error");

        controller.register();

        assertTrue(Context.current().flash().containsValue("Error"));
    }

    @Test
    public void testSecondNFEInRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("due-1-multiselect", "a");

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("error.internalError")).thenReturn("Error");

        controller.register();

        assertTrue(Context.current().flash().containsValue("Error"));
    }

    @Test
    public void testUnequalPasswordInRegister() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123457");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("index.registration.error.passwordUnequal"))
                .thenReturn("Unequal Password");

        controller.register();

        assertTrue(Context.current().flash().containsValue("Unequal Password"));
    }

    @Test
    public void testNotAllNecessaryAchievements() {
        Map<String, String> map = new HashMap<>();
        map.put("completed-1-multiselect", "1");

        SPO spo = new SPO();
        Achievement one = new Achievement();
        Achievement two = new Achievement();
        one.save();
        two.save();
        spo.doTransaction(() -> {
            spo.addNecessaryAchievement(one);
            spo.addNecessaryAchievement(two);
        });

        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("123456");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("error.allNecessaryAchievments"))
                .thenReturn("Not All Achievements");

        controller.register();

        assertTrue(Context.current().flash()
                .containsValue("Not All Achievements"));
    }

    @Test
    public void testAlreadyRegisteredStudent() {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setMatriculationNumber(1);
        });

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        when(form.data()).thenReturn(map);
        when(form.get("firstName")).thenReturn("Hans");
        when(form.get("lastName")).thenReturn("Peter");
        when(form.get("email")).thenReturn("a@b");
        when(form.get("pw")).thenReturn("123456");
        when(form.get("rpw")).thenReturn("123456");
        when(form.get("spo")).thenReturn("1");
        when(form.get("matrnr")).thenReturn("1");
        when(form.get("semester")).thenReturn("3");
        when(form.get("trueData")).thenReturn("true");
        when(messages.at("index.registration.error.matNrExists"))
                .thenReturn("Student already exists");

        controller.register();

        assertTrue(Context.current().flash()
                .containsValue("Student already exists"));
    }

    @Test
    public void testPasswortResetForm() throws EmailException {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        Student student = new Student();
        student.doTransaction(() -> {
            student.setEmailAddress("a@b");
            student.savePassword("123456");
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        when(form.data()).thenReturn(data);
        when(form.get("email")).thenReturn("a@b");
        when(form.get("password")).thenReturn("abcdef");
        when(form.get("pwRepeat")).thenReturn("abcdef");

        controller.passwordResetForm();

        verify(notifier).sendVerifyNewPassword(any(User.class),
                any(String.class));
    }

    @Test
    public void testValidationExceptionInResetForm() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("email")).thenReturn("ab");
        when(messages.at("user.noValidEmail")).thenReturn("InvalidEmail");

        controller.passwordResetForm();

        assertTrue(Context.current().flash().containsValue("InvalidEmail"));
    }

    @Test
    public void testWrongRepeatedPassword() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("email")).thenReturn("a@b");
        when(form.get("password")).thenReturn("123456");
        when(form.get("pwRepeat")).thenReturn("123");

        when(messages.at("index.registration.error.passwordUnequal"))
                .thenReturn("RepeatPasswordFailed");

        controller.passwordResetForm();

        assertTrue(Context.current().flash()
                .containsValue("RepeatPasswordFailed"));
    }

    @Test
    public void testNoUser() {
        Map<String, String> data = new HashMap<>();
        data.put("test", "test");

        when(form.data()).thenReturn(data);
        when(form.get("email")).thenReturn("a@b");
        when(form.get("password")).thenReturn("123456");
        when(form.get("pwRepeat")).thenReturn("123456");

        when(messages.at("index.pwReset.userNotFound")).thenReturn("NoUser");

        controller.passwordResetForm();

        assertTrue(Context.current().flash().containsValue("NoUser"));
    }

    @Test
    public void testResetPasswordSuccess() {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setEmailAddress("a@b");
            student.savePassword("123456");
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        String code = PasswordResetter.getInstance().initializeReset(student,
                new BlowfishPasswordEncoder().encode("abcdef"));

        when(messages.at("index.pwReset.success")).thenReturn("Success");

        controller.resetPassword(code);

        assertTrue(new BlowfishPasswordEncoder().matches("abcdef",
                student.getPassword()));
        assertTrue(Context.current().flash().containsValue("Success"));
    }

    @Test
    public void testResetPasswordFailure() {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setEmailAddress("a@b");
            student.savePassword("123456");
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        when(messages.at("index.pwReset.error")).thenReturn("Failure");

        controller.resetPassword(new String());

        assertTrue(Context.current().flash().containsValue("Failure"));
    }

    @Test
    public void testVerificationPageSuccess() {
        Student student = new Student();
        student.doTransaction(() -> {
            student.setEmailAddress("a@b");
            student.savePassword("123456");
        });
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        semester.doTransaction(() -> {
            semester.addStudent(student);
        });

        String code = EmailVerifier.getInstance().getVerificationCode(student);

        when(messages.at("index.verify.success")).thenReturn("Email Success");

        controller.verificationPage(code);

        assertTrue(Context.current().flash().containsValue("Email Success"));
    }

    @Test
    public void testVerificationPageFailure() {
        when(messages.at("index.verify.error")).thenReturn("Email Failure");

        controller.verificationPage(new String());

        assertTrue(Context.current().flash().containsValue("Email Failure"));
    }
}
