package tobyspring.splean.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyspring.splean.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splean.application.member.provided.MemberRegister;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/member")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
