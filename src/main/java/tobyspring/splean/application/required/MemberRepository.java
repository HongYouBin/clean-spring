package tobyspring.splean.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splean.domain.Member;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
}
