package tobyspring.splean.application.provided;

import jakarta.validation.Valid;
import tobyspring.splean.domain.Member;
import tobyspring.splean.domain.MemberRegisterRequest;

public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);
}
