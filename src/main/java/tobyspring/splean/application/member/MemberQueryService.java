package tobyspring.splean.application.member;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splean.application.member.provided.MemberFinder;
import tobyspring.splean.application.member.required.MemberRepository;
import tobyspring.splean.domain.member.Member;

@Service
@AllArgsConstructor
@Transactional
@Validated
public class MemberQueryService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("잘못된 사용자 id입니다."));
    }
}
