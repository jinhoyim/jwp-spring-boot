package myblog.article.web;

import myblog.article.Article;
import myblog.article.ArticleCreatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import support.test.AcceptanceTest;
import support.test.NsWebTestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleAcceptanceTest extends AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(ArticleAcceptanceTest.class);
    private static final String ARTICLE_RESOURCE_URL = "/articles";

    private static URI createArticle(final NsWebTestClient nsWebTestClient, final ArticleCreatedDto createdDto) {
        final URI location = nsWebTestClient.createResource(ARTICLE_RESOURCE_URL, createdDto, ArticleCreatedDto.class);
        logger.debug("location : {}", location);
        return location;
    }

    @DisplayName("아티클 작성")
    @Test
    void crud() {
        final ArticleCreatedDto createdDto = new ArticleCreatedDto("title1", "content1");
        final String writer = "myUserId";
        final String password = "myPassword";

        // 인증 없이 글 생성 시도
        this.webClient.requestResource(ARTICLE_RESOURCE_URL, HttpMethod.POST)
                .expectStatus().isForbidden();

        // 인증 후 글 생성
        this.createDefaultUser(writer, password);
        final NsWebTestClient authenticatedWebClient = this.webClient.basicAuth(writer, password);
        final URI location = createArticle(authenticatedWebClient, createdDto);

        // 글 조회
        final Article actual = this.webClient.getResource(location, Article.class);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", createdDto.getTitle())
                .hasFieldOrPropertyWithValue("content", createdDto.getContent())
                .hasFieldOrPropertyWithValue("writer", writer);
    }

    private URI createArticle(final ArticleCreatedDto createdDto) {
        return createArticle(this.webClient, createdDto);
    }
}
