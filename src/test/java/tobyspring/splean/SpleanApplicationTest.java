package tobyspring.splean;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class SpleanApplicationTest {
    @Test
    void runTeset() {
        try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            SpleanApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(SpleanApplication.class, new String[0]));
        }
    }
}