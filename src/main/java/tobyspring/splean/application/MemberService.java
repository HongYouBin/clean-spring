package tobyspring.splean.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splean.application.provided.MemberRegister;
import tobyspring.splean.application.required.EmailSender;
import tobyspring.splean.application.required.MemberRepository;
import tobyspring.splean.domain.*;

@Service
@AllArgsConstructor
@Transactional
@Validated
public class MemberService implements MemberRegister {
    private final EmailSender emailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(@Valid MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomEmail(member);

        return member;
    }

    private void sendWelcomEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 통해서 등록을 완료해 주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("중복된 이메일이 존재합니다.");
        }
    }
}
