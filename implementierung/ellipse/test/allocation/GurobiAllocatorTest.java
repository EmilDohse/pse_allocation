package allocation;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GurobiAllocatorTest {

	private static GurobiAllocator ga;

	@Before
	public void before() {
		ga = new GurobiAllocator();
	}

	@Test
	public void testServiceLoader() {
		List<GurobiCriterion> gc = GurobiAllocator.getAllCriteria();
		assertTrue(gc.size() == 9);

	}

	@After
	public void after() {
		ga = null;
	}
}
