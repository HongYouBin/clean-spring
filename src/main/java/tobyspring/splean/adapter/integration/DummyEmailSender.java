package tobyspring.splean.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splean.application.member.required.EmailSender;
import tobyspring.splean.domain.shared.Email;

@Component
@Fallback
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("dummy email send : " + email);
    }
}
