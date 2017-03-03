package controllers;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import play.Application;
import play.api.mvc.RequestHeader;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Http;
import play.test.Helpers;
import data.DataTest;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest extends DataTest {

    @Mock
    FormFactory           formFactory;

    DynamicForm           form;

    private Http.Request  request;

    private RequestHeader header;

    private Application   app;

    @Override
    @Before
    public void before() {

        app = Helpers.fakeApplication();
        Helpers.start(app);

        super.before();

        request = Mockito.mock(Http.Request.class);
        header = Mockito.mock(RequestHeader.class);

        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> flashObject = Collections.emptyMap();

        Http.Context context = new Http.Context(0l, header, request, flashData,
                flashData, flashObject);
        Http.Context.current.set(context);

        form = Mockito.mock(DynamicForm.class);
        Mockito.when(formFactory.form()).thenReturn(form);
        Mockito.when(form.bindFromRequest()).thenReturn(form);
    }

    // Braucht Mockito
    @Test
    public void test() {
        assertTrue(true);
    }

    @Override
    @After
    public void after() {
        Helpers.stop(app);
    }
}
