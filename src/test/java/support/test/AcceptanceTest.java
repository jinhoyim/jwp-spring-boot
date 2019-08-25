package support.test;

import myblog.article.web.ArticleAcceptanceTest;
import myblog.user.dto.UserCreatedDto;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import support.NsProfile;

import java.net.URI;

@ActiveProfiles(NsProfile.TEST)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(ArticleAcceptanceTest.class);

    @Value("${local.server.port}")
    protected int port;
    protected NsWebTestClient webClient;

    @BeforeEach
    void setUp() {
        this.webClient = NsWebTestClient.of(this.port);
    }

    protected URI createDefaultUser(final String userId, final String password) {
        final UserCreatedDto createdDto = new UserCreatedDto(userId, "default@domain.com", password);
        final URI location = this.webClient.createResource("/users", createdDto, UserCreatedDto.class);
        logger.debug("location : {}", location);
        return location;
    }
}
