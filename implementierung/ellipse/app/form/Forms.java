package form;

public class Forms {

    public class Admin {

        public class EditAccount {

            public static final String OLD_PASSWORD    = "oldPassword";
            public static final String NEW_PASSWORD    = "newPassowrd";
            public static final String NEW_PASSWORD_RP = "newPasswordRepeat";

        }

        public class CreateAdviser {

            public static final String FIRST_NAME = "firstName";
            public static final String LAST_NAME  = "lastName";
            public static final String EMAIL      = "email";
            public static final String PASSWORD   = "password";

        }

        public class AddAllocation {

            public static final String NAME                = "name";
            public static final String MIN_TEAM_SIZE       = "minTeamSize";
            public static final String MAX_TEAM_SIZE       = "maxTeamSize";
            public static final String PREFFERED_TEAM_SIZE = "prefferedTeamSize";

        }

        public class Import {

            public static final String FILE       = "file";
            public static final String ALLOCATION = "allocation";
            public static final String SPO        = "spo";
            public static final String STUDENTS   = "students";
            public static final String PROJECTS   = "projects";

        }

        public class AddProject {

            public static final String NAME = "name";
        }

        public class ChangeSPO {

            public static final String ID   = "id";
            public static final String NAME = "nameSPO";
        }

        public class AddAchievement {

            public static final String ID   = "id";
            public static final String NAME = "nameAchiev";
        }
    }

    static StringValidator getPasswordValidator() {
        return new StringValidator(6); // TODO: Als Konstante
    }

    static StringValidator getEmailValidator() {
        return new StringValidator(1, Integer.MAX_VALUE, ".+@.+");
    }

    static StringValidator getNonEmptyStringValidator() {
        return new StringValidator(1);
    }
}
