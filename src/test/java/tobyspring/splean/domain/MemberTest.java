package tobyspring.splean.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private Member member;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        member = Member.create(new MemberCreateRequest("test@email.com", "chulso", "password"), passwordEncoder);
    }

    @Test
    void construct_test() {
        assertThat(member.getEmail().address()).isEqualTo("test@email.com");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getPasswordHash()).isEqualTo("PASSWORD");
    }

    @Test
    void construct_fail() {
        assertThatThrownBy(() -> Member.create(new MemberCreateRequest(null, "chulso", "password"), passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate_success() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }

    @Test
    void activate_fail() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        member.activate();
        assertThatThrownBy(() -> member.activate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate_success() {
        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivate_fail() {
        assertThatThrownBy(() -> member.deactivate()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void changeNickname_success() {
        member.changeNickname("toby");
        assertThat(member.getNickname()).isEqualTo("toby");
    }

    @Test
    void verifyPassword_success() {
        assertThat(member.verifyPassword("password", passwordEncoder)).isTrue();
    }

    @Test
    void verifyPassword_fail() {
        assertThat(member.verifyPassword("hihi", passwordEncoder)).isFalse();
    }


    @Test
    void changePassowrd_success() {
        member.changePassowrd("newpassword", passwordEncoder);

        assertThat(member.verifyPassword("newpassword", passwordEncoder)).isTrue();
    }

    @Test
    void email_exception() {
        assertThatThrownBy(() -> Member.create(new MemberCreateRequest("toby", "hihi", "pwd"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}