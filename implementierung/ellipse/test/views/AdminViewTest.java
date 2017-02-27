package views;

import org.junit.Test;

import views.pages.admin.AdminAccountPage;
import views.pages.admin.AdminAdvisersPage;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.fluentlenium.core.annotation.*;
import static org.junit.Assert.*;

import org.junit.Before;

public class AdminViewTest extends ViewTest {

    private AdminAccountPage  accountPage;
    private AdminAdvisersPage advisersPage;

    @Before
    @Override
    public void before() {
        super.before();
        accountPage = browser.createPage(AdminAccountPage.class);
        advisersPage = browser.createPage(AdminAdvisersPage.class);
        TestHelpers.createAdmin();
        login("admin", "adminadmin", accountPage);
    }

    @Test
    public void gotoAdminAdviser() {
        accountPage.gotoMenuEntry(browser, 0);
        assertEquals(advisersPage.getUrl(), browser.url());
    }
}
