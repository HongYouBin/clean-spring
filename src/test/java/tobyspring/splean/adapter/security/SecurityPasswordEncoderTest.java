package tobyspring.splean.adapter.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SecurityPasswordEncoderTest {
    @Test
    void passwordSecurityEncoder() {
        SecurityPasswordEncoder securityPasswordEncoder = new SecurityPasswordEncoder();

        String passwordHash = securityPasswordEncoder.encode("secret");

        assertThat(securityPasswordEncoder.matches("secret", passwordHash)).isTrue();
        assertThat(securityPasswordEncoder.matches("fail", passwordHash)).isFalse();
    }
}