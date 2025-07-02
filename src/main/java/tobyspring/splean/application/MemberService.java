package tobyspring.splean.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.splean.application.provided.MemberRegister;
import tobyspring.splean.application.required.EmailSender;
import tobyspring.splean.application.required.MemberRepository;
import tobyspring.splean.domain.Member;
import tobyspring.splean.domain.MemberRegisterRequest;
import tobyspring.splean.domain.PasswordEncoder;

@Service
@AllArgsConstructor
public class MemberService implements MemberRegister {
    private final EmailSender emailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 통해서 등록을 완료해 주세요");
        return member;
    }
}
