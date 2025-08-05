package tobyspring.splean.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {
    @Test
    void profile() {
        new Profile("toby");
        new Profile("toby100");
        new Profile("12345");
        new Profile("");
    }

    @Test
    void profile_fail() {
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