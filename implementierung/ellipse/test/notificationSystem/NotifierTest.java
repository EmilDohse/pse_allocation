package notificationSystem;

import javax.inject.Inject;

import play.test.WithApplication;

public class NotifierTest extends WithApplication {

    @Inject
    private Notifier notifier;

    /*
     * @Before public void before() { // See
     * github.com/playframework/play-mailer/issues/78 notifier =
     * Play.application().injector().instanceOf(Notifier.class);
     * Mailbox.clearAll(); }
     * 
     * // TODO NPE
     * 
     * @Ignore
     * 
     * @Test public void testNotifiyStudent() throws AddressException { Student
     * student = new Student(); Team team = new Team(); Project project = new
     * Project(); team.setProject(project); Allocation allocation = new
     * Allocation(); allocation.setStudentsTeam(student, team);
     * 
     * notifier.notifyStudent(allocation, student);
     * Mailbox.get(student.getEmailAddress()).forEach(System.out::println); //
     * assertEquals(expected, actual);); }
     */
}
