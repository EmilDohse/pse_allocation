package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;

import controllers.AdminEditAllocationController;
import data.Allocation;
import data.GeneralData;
import data.Semester;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Result;

@RunWith(MockitoJUnitRunner.class)
public class AdminEditAllocationControllerTest {

    @Mock
    FormFactory                   formFactory;

    @InjectMocks
    AdminEditAllocationController controller;

    private DynamicForm           form;

    private static EbeanServer    server;

    @BeforeClass
    public static void beforeClass() {
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

        Allocation a = new Allocation();
        a.save();
        a.doTransaction(() -> {
            a.setName("test");
        });

        semester.doTransaction(() -> {
            semester.addAllocation(a);
        });
    }

    @AfterClass
    public static void afterClass() {
        server.shutdown(false, false);
    }

    @Before
    public void before() {
        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
    }

    @Test
    public void duplicateAllocationTest() {

        Map<String, String> map = new HashMap<>();
        map.put("1", "1");

        Mockito.when(form.bindFromRequest()).thenReturn(form);
        Mockito.when(form.get("allocationID")).thenReturn("1");
        Mockito.when(form.data()).thenReturn(map);

        Result r = controller.duplicateAllocation();
        System.out.println(r.toString());
        
        assertEquals(GeneralData.loadInstance().getCurrentSemester().getAllocations().size(), 2);
        assertNotNull(Allocation.getAllocation("clonedtest"));
        
        assertTrue(true);
    }
}
