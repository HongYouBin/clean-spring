package tobyspring.splean.application;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splean.application.provided.MemberFinder;
import tobyspring.splean.application.required.MemberRepository;
import tobyspring.splean.domain.Member;

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
