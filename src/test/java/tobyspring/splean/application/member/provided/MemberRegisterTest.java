package tobyspring.splean.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splean.domain.member.*;
import tobyspring.splean.domain.shared.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
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
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

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
        Member member = registerMember();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        entityManager.flush();
        entityManager.clear();

        Member activatedMember = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        assertThat(activatedMember.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
        assertThat(activatedMember.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate_success() {
        Member member = registerMember();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        entityManager.flush();
        entityManager.clear();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        Member activatedMember = memberRegister.deactivate(member.getId());

        assertThat(activatedMember.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(activatedMember.getDetail().getDeactivatedAt()).isNotNull();
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();


        var request = new MemberUpdateRequest("james", "address", "hi");

        Member result = memberRegister.updateInfo(member.getId(), request);

        assertThat(result.getNickname()).isEqualTo("james");
        assertThat(result.getDetail().getIntroduction()).isEqualTo("hi");
        assertThat(result.getDetail().getProfile().address()).isEqualTo("address");
    }

    @Test
    void updateInfo_fail() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        var request = new MemberUpdateRequest("james", "toby100", "hi");
        Member result = memberRegister.updateInfo(member.getId(), request);

        Member member2 = memberRegister.register(new MemberRegisterRequest("toby2@emial", "names", "1234"));
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        //member2는 member와 profile address가 겹칠 수 없다.
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member2.getId(), new MemberUpdateRequest("names", "toby100", "intro"));
        }).isInstanceOf(DuplicateProfileException.class);

        //다른 프로필 주소고 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberUpdateRequest("names", "toby101", "intro"));

        //기존 프로필 주소로 변경 가능
        memberRegister.updateInfo(member.getId(), new MemberUpdateRequest("names", "toby100", "intro"));

        // 프로필 주소 제거 가능
        memberRegister.updateInfo(member.getId(), new MemberUpdateRequest("names", "", "intro"));

        // 프로필 주소 중복 허용하지 않는다.
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member.getId(), new MemberUpdateRequest("names", "toby101", "intro"));
        }).isInstanceOf(DuplicateProfileException.class);

    }
}
