package tobyspring.splean.application.provided;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tobyspring.splean.application.MemberService;
import tobyspring.splean.application.required.EmailSender;
import tobyspring.splean.application.required.MemberRepository;
import tobyspring.splean.domain.Member;
import tobyspring.splean.domain.MemberStatus;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tobyspring.splean.domain.MemberFixture.*;

public class MemberRegisterManualTest {

    @Test
    void memberRegisterTest_success() {
        EmailSender emailSender = mock(EmailSender.class);
        MemberRepository memberRepository = mock(MemberRepository.class);

        MemberRegister memberRegister = new MemberService(emailSender, memberRepository, createPasswordEncoder());
        Member member = memberRegister.register(createMemberRegisterRequest());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        Mockito.verify(emailSender).send(eq(member.getEmail()), any(), any());

    }
}
