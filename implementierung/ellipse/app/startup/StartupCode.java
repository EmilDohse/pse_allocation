package startup;

import javax.inject.Singleton;

import com.avaje.ebean.Ebean;

import data.Administrator;
import security.BlowfishPasswordEncoder;

@Singleton
public class StartupCode implements StartupInterface {

    public StartupCode() {
        onStartup();
    }

    @Override
    public void onStartup() {
        System.out.println("---- ONSTARTUP -----");
        if (Ebean.getServer(null) != null) {
            if (Administrator.getAdministrators().isEmpty()) {
                Administrator admin = new Administrator("admin",
                        new BlowfishPasswordEncoder()
                                .encode(Administrator.START_PASSWORD),
                        "myemail@kit.edu", "Administrator", "Admin");
                admin.save();
            }
        }
    }

}
