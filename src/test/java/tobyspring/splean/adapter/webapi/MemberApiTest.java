package tobyspring.splean.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tobyspring.splean.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splean.application.member.provided.MemberRegister;
import tobyspring.splean.application.member.required.MemberRepository;
import tobyspring.splean.domain.member.Member;
import tobyspring.splean.domain.member.MemberRegisterRequest;
import tobyspring.splean.domain.member.MemberStatus;
import tobyspring.splean.domain.shared.MemberFixture;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static tobyspring.splean.adapter.AssertThatUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class MemberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/member").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notnull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() throws JsonProcessingException {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/member").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }
}