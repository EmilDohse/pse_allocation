package allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import data.AllocationParameter;
import data.LearningGroup;
import data.Project;
import data.Student;
import jdk.nashorn.internal.ir.annotations.Ignore;

/**
 * tests zur klasse allocation queue
 * 
 * @author emil
 *
 */
public class AllocationQueueTest {

    AllocationQueue            allocQueue;
    Configuration              configOne;
    Configuration              configTwo;
    private static EbeanServer server;

    @BeforeClass
    public static void beforeClass() {
        ServerConfig config = new ServerConfig();
        config.setName("db");
        config.loadTestProperties();
        config.setDefaultServer(true);
        config.setRegister(true);

        server = EbeanServerFactory.create(config);
    }

    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }

    @Before
    public void init() {
        allocQueue = AllocationQueue.getInstance();
        AllocationParameter paramOne = new AllocationParameter("minSize", 1);
        AllocationParameter paramTwo = new AllocationParameter("maxSize", 2);
        List<AllocationParameter> paramList = new ArrayList<>();
        paramList.add(paramOne);
        paramList.add(paramTwo);
        configOne = new Configuration("test 1", new ArrayList<Student>(), new ArrayList<LearningGroup>(),
                new ArrayList<Project>(), paramList);
        configTwo = new Configuration("test 2", new ArrayList<Student>(), new ArrayList<LearningGroup>(),
                new ArrayList<Project>(), paramList);
    }

    @After
    public void after() {
        allocQueue.clear();
    }

    /**
     * testet ob getInstance funktioniert
     */
    @Test
    public void testSingelton() {
        assertTrue(allocQueue != null);
    }

    /**
     * fügt eine config hinzu und überprüft ob diese angezeigt wird
     * 
     * @throws AllocationException
     */
    @Test
    @Ignore
    public void testAddToQueue() {
        allocQueue.addToQueue(configOne);
        assertTrue(allocQueue.getQueue().size() == 1);

    }

    /**
     * fügt eine zwei configs hinzu und überprüft die nanem
     * 
     * @throws AllocationException
     */
    @Test
    @Ignore
    public void testAddToQueue2() {
        allocQueue.addToQueue(configOne);
        allocQueue.addToQueue(configTwo);
        List<Configuration> list = allocQueue.getQueue();
        assertTrue(list.get(0).getName().equals("test 1"));
        assertTrue(list.get(1).getName().equals("test 2"));
        assertTrue(list.size() == 2);
    }

    /**
     * test überprüft ob das entfernen der im moment berechneten allocation dazu
     * führt das diese aus der liste der allocations verschwindet
     * 
     * @throws AllocationException
     */
    @Test
    @Ignore
    public void testCancelAllocation() {
        allocQueue.addToQueue(configOne);
        allocQueue.addToQueue(configTwo);
        List<Configuration> list = allocQueue.getQueue();
        allocQueue.cancelAllocation(list.get(0).getName());
        list = allocQueue.getQueue();
        assertEquals("test 2", list.get(0).getName());
        assertEquals(1, list.size());
    }

    /**
     * test überprüft ob das entfernen einer der nicht berechneten allocations
     * dazu führt das diese aus der liste der allocations verschwindet
     * 
     * @throws AllocationException
     */
    @Test
    @Ignore
    public void testCancelAllocation2() {
        allocQueue.addToQueue(configOne);
        allocQueue.addToQueue(configTwo);
        List<Configuration> list = allocQueue.getQueue();
        allocQueue.cancelAllocation(list.get(1).getName());
        list = allocQueue.getQueue();
        assertTrue(list.get(0).getName().equals("test 1"));
        assertTrue(list.size() == 1);
    }
}
