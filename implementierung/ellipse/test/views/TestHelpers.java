package views;

import data.Administrator;
import data.GeneralData;
import data.Semester;
import deadline.StateStorage;

public class TestHelpers {

    public static final String ADMIN_USERNAME = "admin";

    public static void initStateChange() {
        Semester semester = GeneralData.loadInstance().getCurrentSemester();
        StateStorage.getInstance().initStateChanging(
                semester.getRegistrationStart(), semester.getRegistrationEnd());
        try {
            Thread.sleep(100); // TODO: Besser??? Warten auf StateChange
        } catch (InterruptedException e) {
        }
    }

    public static void createAdmin() {
        Administrator admin = new Administrator(ADMIN_USERNAME, "", "a@kit.edu",
                "admin", "admin");
        admin.save();
        admin.doTransaction(() -> {
            admin.savePassword(Administrator.START_PASSWORD);
        });
    }
}
