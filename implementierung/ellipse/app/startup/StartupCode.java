package startup;

import javax.inject.Singleton;

import com.avaje.ebean.Ebean;

import data.Administrator;
import exception.DataException;

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
                try {
                    admin = new Administrator("admin",
                            Administrator.START_PASSWORD, "myemail@kit.edu",
                            "Administrator", "Admin");
                    admin.save();
                } catch (DataException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}
