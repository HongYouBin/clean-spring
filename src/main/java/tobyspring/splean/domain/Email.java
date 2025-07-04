package tobyspring.splean.domain;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Email(String address) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public Email {
//        if(!EMAIL_PATTERN.matcher(address).matches()) {
//            throw new IllegalArgumentException("유요하지 않은 이메일 형태입니다.");
//        }
    }
}
