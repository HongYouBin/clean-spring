package tobyspring.splean.application.provided;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splean.application.required.EmailSender;
import tobyspring.splean.domain.MemberFixture;
import tobyspring.splean.domain.member.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    EmailSender getEmailSender() {
        return (email, subject, body) -> System.out.println("Send email : " + email);
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }

}
