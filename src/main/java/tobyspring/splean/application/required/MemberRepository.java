package tobyspring.splean.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splean.domain.Email;
import tobyspring.splean.domain.Member;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);
}
