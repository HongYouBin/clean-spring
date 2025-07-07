package tobyspring.splean.application.provided;

import tobyspring.splean.domain.Member;

public interface MemberFinder {
    Member find(Long memberId);
}
