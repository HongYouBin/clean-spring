package tobyspring.splean.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tobyspring.splean.application.MemberService;
import tobyspring.splean.application.provided.MemberRegister;
import tobyspring.splean.application.required.EmailSender;
import tobyspring.splean.application.required.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tobyspring.splean.domain.MemberFixture.*;

public class MemberRegisterTest {

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
