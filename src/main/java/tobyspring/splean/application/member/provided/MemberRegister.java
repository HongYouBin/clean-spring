package tobyspring.splean.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;
import tobyspring.splean.domain.member.MemberUpdateRequest;

public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);

    Member deactivate(Long memberId);

    Member updateInfo(Long memberId, @Valid MemberUpdateRequest updateRequest);
}
