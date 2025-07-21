package tobyspring.splean.application.provided;

import tobyspring.splean.domain.member.Member;

public interface MemberFinder {
    Member find(Long memberId);
}
