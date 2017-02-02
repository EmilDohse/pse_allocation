package startup;

import javax.inject.Singleton;

import data.Administrator;

@Singleton
public class StartupCode implements StartupInterface {

    public StartupCode() {
        onStartup();
    }

    @Override
    public void onStartup() {
        System.out.println("---- ONSTARTUP -----");
        if (Administrator.getAdministrators().isEmpty()) {
            Administrator admin = new Administrator("admin", "admin",
                    "myemail@kit.edu", "Administrator", "Admin");
            admin.save();
        }
    }

}
