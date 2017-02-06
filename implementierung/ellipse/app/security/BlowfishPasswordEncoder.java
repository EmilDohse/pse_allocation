package security;

import org.pac4j.core.credentials.password.PasswordEncoder;

public class BlowfishPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {

        // TODO
        return password;
        // return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String plainPassword, String encodedPassword) {
        return false;
        // BCrypt.checkpw(plainPassword, encodedPassword);
    }

}
