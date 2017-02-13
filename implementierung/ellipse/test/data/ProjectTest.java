package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests für die Klasse Project.
 *
 */
public class ProjectTest extends DataTest {

    private Project project;

    /**
     * Das Projekt wird initialisiert.
     */
    @Before
    public void beforeTest() {
        project = new Project();
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für den Namen des
     * Projekts.
     */
    @Test
    public void testName() {
        String n = "testname";
        project.setName(n);
        assertEquals(n, project.getName());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für die minimale
     * Teamgröße des Projekts.
     */
    @Test
    public void testMinTeamSize() {
        int s = 11;
        project.setMinTeamSize(s);
        assertEquals(s, project.getMinTeamSize());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für die maximale
     * Teamgröße des Projekts.
     */
    @Test
    public void testMaxTeamSize() {
        int s = 11;
        project.setMaxTeamSize(s);
        assertEquals(s, project.getMaxTeamSize());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für die Anzahl an
     * Teams.
     */
    @Test
    public void testNumberOfTeams() {
        int s = 11;
        project.setNumberOfTeams(s);
        assertEquals(s, project.getNumberOfTeams());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für die Information des
     * Projekts.
     */
    @Test
    public void testProjectInfo() {
        String n = "testinfo";
        project.setProjectInfo(n);
        assertEquals(n, project.getProjectInfo());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für die URL des
     * Projektes.
     */
    @Test
    public void testProjectURL() {
        String n = "testurl";
        project.setProjectURL(n);
        assertEquals(n, project.getProjectURL());
    }

    /**
     * Diese Methode testet sowohl getter alsauch setter für das Institut des
     * Projekts.
     */
    @Test
    public void testInstitut() {
        String n = "testinstitut";
        project.setInstitute(n);
        assertEquals(n, project.getInstitute());
    }

    /**
     * Diese Methode testet, ob man einwandfrei Betreuer hinzufügen und
     * entfernen kann.
     */
    @Test
    public void testAdvisers() {
        Adviser firstA = new Adviser();
        Adviser secondA = new Adviser();
        List<Adviser> advisers = new ArrayList<Adviser>();
        advisers.add(firstA);
        project.setAdvisers(advisers);
        assertEquals(project.getAdvisers().size(), 1);
        assertTrue(project.getAdvisers().contains(firstA));

        project.addAdviser(secondA);
        assertEquals(project.getAdvisers().size(), 2);
        assertTrue(project.getAdvisers().contains(firstA));
        assertTrue(project.getAdvisers().contains(secondA));

        project.removeAdviser(firstA);
        assertEquals(project.getAdvisers().size(), 1);
        assertTrue(project.getAdvisers().contains(secondA));
    }

    /**
     * Diese Methode testet, ob die setter und getter für das semester
     * einwandfrei funktionieren.
     */
    @Test
    public void testGetSemester() {
        Semester s = new Semester();
        s.save();
        List<Project> projects = new ArrayList<Project>();
        project.save();
        projects.add(project);
        s.setProjects(projects);
        s.save();
        GeneralData.loadInstance().setCurrentSemester(s);
        GeneralData.loadInstance().save();
        assertEquals(s, project.getSemester());
    }

    /**
     * Diese Methode testet, ob die statische Methode, die alle Projekte aus der
     * Datenbank zurückgobt, einwandfrei funktioniert.
     */
    @Test
    public void testGetProjects() {
        Project.getProjects().forEach(p -> p.delete());
        Project one = new Project();
        Project two = new Project();
        one.save();
        two.save();

        assertEquals(2, Project.getProjects().size());
    }
}
