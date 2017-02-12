package controller;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import controllers.AdminEditAllocationController;
import play.test.WithApplication;

public class AdminEditAllocationTest extends WithApplication {

    private AdminEditAllocationController controller;
    // private Http.Context context;

    @Before
    public void before() {
        // try { // Hypothese: Der seltsame Hikari Fehler wird durch warten vorm
        // // Injecten behoben
        // Thread.sleep(100);
        // } catch (InterruptedException e) {
        // }
        // context = mock(Http.Context.class);
        // controller = Play.application().injector()
        // .instanceOf(AdminEditAllocationController.class);
    }

    @Ignore
    @Test
    public void testIndex() throws NoSuchAlgorithmException {
        System.out.println(
                KeyGenerator.getInstance("DES").generateKey().getEncoded());
        // RequestBuilder request = Helpers.fakeRequest("POST",
        // "/admin/swapStudents");
        // System.out.println(request.body());
        // Result result = Helpers
        // .route(request);
        // assertEquals(OK, result.status());
        // assertEquals("text/html", result.contentType().get());
        // assertEquals("utf-8", result.charset().get());
        // String registeredUserName = "bob";
        // String registeredUserEmail = "bob@gmail.ac.uk";
        // String registeredUserPass = "secret";
        // String registeredUserPassConfirm = "secret";
        //
        // Map<String, String> userData = new HashMap<String, String>();
        // userData.put("name", registeredUserName);
        // userData.put("email", registeredUserEmail);
        // userData.put("password", registeredUserPass);
        // userData.put("passwordconfirm", registeredUserPassConfirm);
        //
        // Result r = callAction(routes.ref.UserController.add(),
        // (Helpers.fakeRequest())
        // .withFormUrlEncodedBody(Form.form().bind(userData).data().ge));
        //
        // assertEquals(r.status(), 200);
    }

}