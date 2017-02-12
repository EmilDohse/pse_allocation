package notificationSystem;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;

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
import exception.DataException;
import play.Play;
import play.test.WithApplication;

public class NotifierTest extends WithApplication {

    @Inject
    private Notifier notifier;

    private static EbeanServer server;

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

    @After
    public void after() {
        server.shutdown(false, false);
    }

    @Test
    public void testNotifiyStudent() throws EmailException, IOException, DataException, MessagingException {
        Student student = new Student();
        Team team = new Team();
        Project project = new Project();
        team.setProject(project);
        Allocation allocation = new Allocation();
        allocation.setStudentsTeam(student, team);

        notifier.notifyStudent(allocation, student);

        Message message = Mailbox.get(student.getEmailAddress()).get(0);
        // Email muss an den Student adressiert sein
        assertTrue(message.getContent().toString().contains(student.getFirstName()));
        assertTrue(message.getContent().toString().contains(student.getLastName()));
    }

    // TODO provoziert fast immer den Startup Fehler
    @Ignore
    @Test
    public void testNotifyAdviser() throws EmailException, IOException, MessagingException, DataException {
        Adviser adviser = new Adviser();
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
        assertTrue(message.getContent().toString().contains(adviser.getFirstName()));
        assertTrue(message.getContent().toString().contains(adviser.getLastName()));
        // Enthält Team
        assertTrue(message.getContent().toString().contains(String.valueOf(team.getTeamNumber())));
    }

    @Test
    public void testSendAdviserPassword() throws EmailException, IOException, MessagingException, DataException {
        Adviser adviser = new Adviser();
        adviser.setEmailAddress("adviser@email.com");
        String password = "secret";
        notifier.sendAdviserPassword(adviser, password);

        Message message = Mailbox.get(adviser.getEmailAddress()).get(0);
        // Email muss an den Betreuer adressiert sein
        assertTrue(message.getContent().toString().contains(adviser.getFirstName()));
        assertTrue(message.getContent().toString().contains(adviser.getLastName()));
        // Enthält Team
        assertTrue(message.getContent().toString().contains(password));
    }
}
