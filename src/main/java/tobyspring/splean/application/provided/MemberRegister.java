package tobyspring.splean.application.provided;

import tobyspring.splean.domain.Member;
import tobyspring.splean.domain.MemberRegisterRequest;

public interface MemberRegister {
    Member register(MemberRegisterRequest registerRequest);
}
