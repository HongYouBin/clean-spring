package tobyspring.splean.domain.shared;

import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;
import tobyspring.splean.domain.member.PasswordEncoder;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "toby", "secret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("toby@splean.app");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

    }

    public static Member createMember(long id) {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}
