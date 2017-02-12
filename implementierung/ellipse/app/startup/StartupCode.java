package startup;

import javax.inject.Singleton;

import com.avaje.ebean.Ebean;

import data.Administrator;

@Singleton
public class StartupCode implements StartupInterface {

    public StartupCode() {
        onStartup();
    }

    @Override
    public void onStartup() {
        if (Ebean.getServer(null) != null) {
            if (Administrator.getAdministrators().isEmpty()) {
                Administrator admin;

                admin = new Administrator("admin", Administrator.START_PASSWORD,
                        "myemail@kit.edu", "Administrator", "Admin");
                admin.save();

            }
        }
    }

}
