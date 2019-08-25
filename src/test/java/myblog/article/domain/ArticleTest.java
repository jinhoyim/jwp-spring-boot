package myblog.article.domain;

import myblog.article.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {

    static Stream articleProvider() {
        return Stream.of(
                Arguments.of(1, "title1", "content1", "writer1"),
                Arguments.of(2, "제목1", "본문1", "작성자1")
        );
    }

    static Stream matchArticleProvider() {
        return Stream.of(
                Arguments.of(1, 1, true),
                Arguments.of(2, 2, true),
                Arguments.of(1, 2, false)
        );
    }

    @DisplayName("Article 도메인 객체를 생성")
    @ParameterizedTest(name = "id: {0}, title: {1}, content: {2}, writer: {3}")
    @MethodSource("articleProvider")
    void createArticle(final int id, final String title, final String content, final String writer) {
        final Article article = new Article(id, title, content, writer);
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("content", content)
                .hasFieldOrPropertyWithValue("writer", writer);
    }

    @DisplayName("Article의 id를 값이 같은지 비교")
    @ParameterizedTest(name = "id: {0}, targetId: {1}, matchResult: {2}")
    @MethodSource("matchArticleProvider")
    void matchArticleId(final int id, final int targetId, final boolean matchResult) {
        final Article article = new Article(id, "dummy", "dummy", "dummy");
        final boolean actual = article.matchId(targetId);
        assertThat(actual).isEqualTo(matchResult);
    }
}
