package tobyspring.splean.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import tobyspring.splean.domain.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail_fail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRequest_valid_fail() {
        extracted(new MemberRegisterRequest("toby.com", "toby", "secrets"));
        extracted(new MemberRegisterRequest("toby.com", "toby000000000000000000", "secrets"));

    }

    private void extracted(MemberRegisterRequest memberRegisterRequest) {
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
