package tobyspring.splean.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tobyspring.splean.domain.member.DuplicateEmailException;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;
import tobyspring.splean.domain.member.MemberStatus;
import tobyspring.splean.domain.shared.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
//@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmail_fail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRequest_valid_fail() {
        checkValidation(new MemberRegisterRequest("toby.com", "toby", "secrets"));
        checkValidation(new MemberRegisterRequest("toby.com", "toby000000000000000000", "secrets"));

    }

    private void checkValidation(MemberRegisterRequest memberRegisterRequest) {
        assertThatThrownBy(() -> memberRegister.register(memberRegisterRequest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate_success() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        entityManager.flush();
        entityManager.clear();

        Member activatedMember = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(activatedMember.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
    }
}
