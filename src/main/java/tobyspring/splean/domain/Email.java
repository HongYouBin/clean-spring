package tobyspring.splean.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.context.annotation.Fallback;

import java.util.regex.Pattern;

@Embeddable
@Fallback
public record Email(@Column(name = "email_address", length = 150, nullable = false) String address) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
}
