package tobyspring.splean.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splean.domain.MemberFixture.*;

class MemberTest {
    private Member member;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest(), this.passwordEncoder);
    }

    @Test
    void construct_test() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getPasswordHash()).isEqualTo("SECRET");
    }

    @Test
    void construct_fail() {
        assertThatThrownBy(() -> Member.register(new MemberRegisterRequest(null, "chulso", "password"), passwordEncoder))
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
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
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
        assertThatThrownBy(() -> Member.register(new MemberRegisterRequest("toby", "hihi", "pwd"), passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}