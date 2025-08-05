package tobyspring.splean.application.member;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splean.application.member.provided.MemberFinder;
import tobyspring.splean.application.member.provided.MemberRegister;
import tobyspring.splean.application.member.required.EmailSender;
import tobyspring.splean.application.member.required.MemberRepository;
import tobyspring.splean.domain.member.*;
import tobyspring.splean.domain.shared.Email;

@Service
@AllArgsConstructor
@Transactional
@Validated
public class MemberModifyService implements MemberRegister {
    private final EmailSender emailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberFinder memberFinder;

    @Override
    public Member register(@Valid MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberUpdateRequest updateRequest) {
        Member member = memberFinder.find(memberId);

        checkProfileDuplicate(member, updateRequest.profileAddress());

        member.updateInfo(updateRequest);

        return memberRepository.save(member);
    }

    private void checkProfileDuplicate(Member member, String profileAddress) {
        if(profileAddress.isEmpty()) return;
        Profile profile = member.getDetail().getProfile();
        if(profile != null && profile.address().equals(profileAddress)) return;

        if(memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미 존재하는 프로필 주소입니다.");
        }
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
