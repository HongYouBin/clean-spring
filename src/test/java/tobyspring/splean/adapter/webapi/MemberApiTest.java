package tobyspring.splean.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyspring.splean.application.member.provided.MemberRegister;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;
import tobyspring.splean.domain.shared.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException {
        Member member = MemberFixture.createMember(1L);

        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(
        mvcTester.post().uri("/api/member").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(any());
    }

    @Test
    void register_fail() throws JsonProcessingException {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest("invalid email");

        String requestJson = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(
                mvcTester.post().uri("/api/member").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

}