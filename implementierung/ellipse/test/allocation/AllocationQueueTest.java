import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import data.AllocationParameter;
import data.LearningGroup;
import data.Student;
import data.Team;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 * tests zur klasse allocation queue
 * 
 * @author emil
 *
 */
public class AllocationQueueTest {

    AllocationQueue allocQueue;
    Configuration   config1;
    Configuration   config2;

    @Before
    public void init() {
        allocQueue = AllocationQueue.getInstance();
        config1 = new Configuration("test 1", new ArrayList<Student>(), new ArrayList<LearningGroup>(),
                new ArrayList<Team>(), new ArrayList<AllocationParameter>());
        config1 = new Configuration("test 2", new ArrayList<Student>(), new ArrayList<LearningGroup>(),
                new ArrayList<Team>(), new ArrayList<AllocationParameter>());
    }

    /**
     * testet ob getInstance funktioniert
     */
    @Test
    public void testSingelton() {
        assert (allocQueue != null);
    }

    /**
     * fügt eine config hinzu und überprüft ob diese angezeigt wird
     */
    @Test
    @Ignore
    private void testAddToQueue() {
        allocQueue.addToQueue(config1);
        assert (allocQueue.getQueue().size() == 1);

    }

    /**
     * fügt eine zwei configs hinzu und überprüft die nanem
     */
    @Test
    @Ignore
    private void testAddToQueue2() {
        allocQueue.addToQueue(config1);
        allocQueue.addToQueue(config2);
        List<Configuration> list = allocQueue.getQueue();
        assert (list.get(0).getName().equals("test 1"));
        assert (list.get(1).getName().equals("test 2"));
        assert(list.size() == 2);
    }

    /**
     * test überprüft ob das entfernen der im moment berechneten allocation dazu
     * führt das diese aus der liste der allocations verschwindet
     */
    @Test
    @Ignore
    private void testCancelAllocation() {
        allocQueue.addToQueue(config1);
        allocQueue.addToQueue(config2);
        List<Configuration> list = allocQueue.getQueue();
        allocQueue.cancelAllocation(list.get(0));
        list = allocQueue.getQueue();
        assert (list.get(0).getName().equals("test 2"));
        assert (list.size() == 1);
    }
    /**
     * test überprüft ob das entfernen einer der nicht berechneten allocations dazu
     * führt das diese aus der liste der allocations verschwindet
     */
    @Test
    @Ignore
    private void testCancelAllocation2() {
        allocQueue.addToQueue(config1);
        allocQueue.addToQueue(config2);
        List<Configuration> list = allocQueue.getQueue();
        allocQueue.cancelAllocation(list.get(1));
        list = allocQueue.getQueue();
        assert (list.get(0).getName().equals("test 1"));
        assert (list.size() == 1);
    }
}
