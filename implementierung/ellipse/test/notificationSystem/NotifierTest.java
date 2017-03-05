package notificationSystem;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.mail.EmailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.Adviser;
import data.Allocation;
import data.GeneralData;
import data.Project;
import data.Semester;
import data.Student;
import data.Team;
import play.Play;
import play.test.WithApplication;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Notifer. Außerdem wird das
 * ganze Notification-System getestet.
 */
public class NotifierTest extends WithApplication {

    @Inject
    private Notifier           notifier;

    private static EbeanServer server;

    /**
     * Setup der SMTP-Einstellungen und der allgemeinen Daten.
     */
    @Before
    public void before() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
        // Init General Data. Evolutions wollen nicht funktionieren
        GeneralData data = new GeneralData();
        data.save();
        Semester semester = new Semester();
        semester.save();
        data.setCurrentSemester(semester);
        data.save();

        // See github.com/playframework/play-mailer/issues/78
        notifier = Play.application().injector().instanceOf(Notifier.class);
        Mailbox.clearAll();
    }

    /**
     * Server shutdown.
     */
    @After
    public void after() {
        server.shutdown(false, false);
    }

    /**
     * Diese Methode testet das Benachrichtigen eines Studenten über die finale
     * Einteilung.
     * 
     * @throws EmailException
     * @throws IOException
     * @throws MessagingException
     */
    @Test
    @Ignore
    public void testNotifiyStudent()
            throws EmailException, IOException, MessagingException {
        Student student = new Student();
        student.setEmailAddress("e@mail");
        Team team = new Team();
        Project project = new Project();
        team.setProject(project);
        Allocation allocation = new Allocation();
        allocation.setStudentsTeam(student, team);

        notifier.notifyStudent(allocation, student);

        Message message = Mailbox.get(student.getEmailAddress()).get(0);
        // Email muss an den Student adressiert sein
        assertTrue(message.getContent().toString()
                .contains(student.getFirstName()));
        assertTrue(message.getContent().toString()
                .contains(student.getLastName()));
    }

    /**
     * Diese Methode testet das Benachrichtigen eines Betreuers über die finale
     * Einteilung.
     * 
     * @throws EmailException
     * @throws IOException
     * @throws MessagingException
     */
    @Test
    @Ignore
    public void testNotifyAdviser()
            throws EmailException, IOException, MessagingException {
        Adviser adviser = new Adviser();
        adviser.setEmailAddress("e@mail");
        Student student = new Student();
        Team team = new Team();
        Project project = new Project();
        project.addAdviser(adviser);
        team.setProject(project);
        Allocation allocation = new Allocation();
        allocation.setStudentsTeam(student, team);

        notifier.notifyAdviser(allocation, adviser);

        Message message = Mailbox.get(adviser.getEmailAddress()).get(0);
        // Email muss an den Betreuer adressiert sein
        assertTrue(message.getContent().toString()
                .contains(adviser.getFirstName()));
        assertTrue(message.getContent().toString()
                .contains(adviser.getLastName()));
        // Enthält Team
        assertTrue(message.getContent().toString()
                .contains(String.valueOf(team.getTeamNumber())));
    }

    /**
     * Diese Methode schickt einem Betreuer sein Passwort.
     * 
     * @throws EmailException
     * @throws IOException
     * @throws MessagingException
     */
    @Test
    @Ignore
    public void testSendAdviserPassword()
            throws EmailException, IOException, MessagingException {
        Adviser adviser = new Adviser();
        adviser.setEmailAddress("adviser@email.com");
        String password = "secret";
        notifier.sendAdviserPassword(adviser, password);

        Message message = Mailbox.get(adviser.getEmailAddress()).get(0);
        // Email muss an den Betreuer adressiert sein
        assertTrue(message.getContent().toString()
                .contains(adviser.getFirstName()));
        assertTrue(message.getContent().toString()
                .contains(adviser.getLastName()));
        // Enthält Team
        assertTrue(message.getContent().toString().contains(password));
    }

    @Test
    @Ignore
    public void testSendVerificationMail()
            throws EmailException, MessagingException, IOException {
        Student student = new Student();
        student.setEmailAddress("e@mail");
        notifier.sendVerificationMail(student, "code");

        Message message = Mailbox.get(student.getEmailAddress()).get(0);

        assertTrue(message.getContent().toString().contains(student.getName()));
        assertTrue(message.getContent().toString().contains("code"));
    }

    @Test
    @Ignore
    public void testSendVerifyNewPassword()
            throws EmailException, MessagingException, IOException {
        Student student = new Student();
        student.setEmailAddress("e@mail");
        notifier.sendVerifyNewPassword(student, "url");

        Message message = Mailbox.get(student.getEmailAddress()).get(0);

        assertTrue(message.getContent().toString().contains(student.getName()));
        assertTrue(message.getContent().toString().contains("url"));
    }

    @Test
    @Ignore
    public void testNotifyAllUsers()
            throws EmailException, MessagingException, IOException {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();

        Student student = new Student();
        student.doTransaction(() -> {
            student.setEmailAddress("e@mail");
        });

        Adviser adviser = new Adviser();
        adviser.doTransaction(() -> {
            adviser.setEmailAddress("a@b.c");
        });

        Project project = new Project();
        project.doTransaction(() -> {
            project.addAdviser(adviser);
            project.setName("name");
        });

        ArrayList<Team> teams = new ArrayList<>();
        Team team = new Team();
        team.setProject(project);
        team.addMember(student);
        teams.add(team);

        Allocation allocation = new Allocation();
        allocation.doTransaction(() -> {
            allocation.setTeams(teams);
        });

        semester.doTransaction(() -> {
            semester.addStudent(student);
            semester.addProject(project);
        });

        notifier.notifyAllUsers(allocation);

        Message studentMessage = Mailbox.get(student.getEmailAddress()).get(0);
        Message adviserMessage = Mailbox.get(adviser.getEmailAddress()).get(0);

        assertTrue(studentMessage.getContent().toString()
                .contains(student.getName()));
        assertTrue(adviserMessage.getContent().toString()
                .contains(adviser.getName()));
        assertTrue(studentMessage.getContent().toString()
                .contains(team.getProject().getName()));
        assertTrue(adviserMessage.getContent().toString()
                .contains(String.valueOf(team.getTeamNumber())));
    }
}
