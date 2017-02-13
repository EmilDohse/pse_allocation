package qualityCriteria;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * Diese Klasse beinhaltet Unit-Tests zur Klasse QualityCriteriaLoader.
 */
public class QualityCriteriaLoaderTester {

    /**
     * Diese Methode testet, ob alle Gütekriterien vom Loader geladen werden.
     */
    @Test
    public void QualityCriteriaLoaderTest() {
        List<QualityCriterion> crits = QualityCriteriaLoader.getAllQualityCriteria();
        assertEquals(3, crits.size());
    }
}
