package form;

public class Forms {

    public class Admin {

        public class EditAccount extends Forms {

            public static final String OLD_PASSWORD    = "oldPassword";
            public static final String NEW_PASSWORD    = "newPassowrd";
            public static final String NEW_PASSWORD_RP = "newPasswordRepeat";

        }

        public class CreateAdviser extends Forms {

            public static final String FIRST_NAME = "firstName";
            public static final String LAST_NAME  = "lastName";
            public static final String EMAIL      = "email";
            public static final String PASSWORD   = "password";

        }
    }
}
