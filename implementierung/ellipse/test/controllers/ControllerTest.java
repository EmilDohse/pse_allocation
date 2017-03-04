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

import data.DataTest;
import play.Application;
import play.api.mvc.RequestHeader;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Http;
import play.test.Helpers;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest extends DataTest {

    @Mock
    FormFactory           formFactory;

    DynamicForm           form;

    Messages              messages;

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

        Http.Context realContext = new Http.Context(0l, header, request,
                flashData,
                flashData, flashObject);
        Http.Context spyContext = Mockito.spy(realContext);
        messages = Mockito.mock(Messages.class);
        Mockito.doReturn(messages).when(spyContext).messages();
        Http.Context.current.set(spyContext);

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
        super.after();
        Helpers.stop(app);
    }
}
