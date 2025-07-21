package tobyspring.splean.application.member.required;

import tobyspring.splean.domain.shared.Email;

public interface EmailSender {
    void send(Email email, String subject, String body);
}
