package tobyspring.splean.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    @Test
    void profile() {
        new Profile("toby");
        new Profile("toby100");
        new Profile("12345");
    }

    @Test
    void profile_fail() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("kadsjfkajsd;fklajdfkajsd;fjasd;kfjaskdfj")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void url() {
        Profile profile = new Profile("toby");

        assertThat(profile.url()).isEqualTo("@toby");
    }
}