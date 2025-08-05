package tobyspring.splean.domain.member;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splean.application.member.required.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splean.domain.shared.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splean.domain.shared.MemberFixture.createPasswordEncoder;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void createMember() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();
        memberRepository.save(member);

        entityManager.flush();
        assertThat(member.getId()).isNotNull();

        entityManager.clear();

        var found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicationEmailFail() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member);
        entityManager.flush();

        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}
