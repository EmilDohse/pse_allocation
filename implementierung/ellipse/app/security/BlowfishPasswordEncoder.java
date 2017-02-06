package security;

import org.pac4j.core.credentials.password.PasswordEncoder;

public class BlowfishPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        // BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String plainPassword, String encodedPassword) {
    }

}
