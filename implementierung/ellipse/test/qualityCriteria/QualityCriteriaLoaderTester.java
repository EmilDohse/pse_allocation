package qualityCriteria;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QualityCriteriaLoaderTester {

    @Test
    public void QualityCriteriaLoaderTest() {
        List<QualityCriterion> crits = QualityCriteriaLoader.getAllQualityCriteria();
        assertEquals(3, crits.size());
    }
}
