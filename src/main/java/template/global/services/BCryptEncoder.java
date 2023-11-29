package template.global.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("BCryptEncoder")
public class BCryptEncoder implements StringEncoder {
    public String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String decode(String string) {
        throw new UnsupportedOperationException("BCrypt can't decode strings.");
    }

    public Boolean verify(String password, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(password, encodedPassword);
    }
}