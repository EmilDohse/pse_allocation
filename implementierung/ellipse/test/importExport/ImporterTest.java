package importExport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.Semester;
import play.Application;
import play.test.Helpers;

public class ImporterTest {
	private static Importer importerExporter;
	private static Application app;

	@BeforeClass
	public static void beforeClass() {
		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);
	}

	@Before
	public void before() {
		importerExporter = new Importer();
	}

	@Test
	public void testImportTestData() {
		importerExporter.importTestData("students.training.csv", "topics.training.csv");
		assertTrue(Semester.getSemesters().size() == 1);
		assertEquals(227, Semester.getSemester("TestSemester").getStudents().size());
	}

	@After
	public void after() {
		importerExporter = null;
	}

	@AfterClass
	public static void afterClass() {
		Helpers.stop(app);
	}
}
