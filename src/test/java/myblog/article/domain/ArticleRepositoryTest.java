package myblog.article.domain;

import myblog.article.Article;
import myblog.article.ArticleCreatedDto;
import myblog.article.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleRepositoryTest {

    static Stream articleCreatedDtoProvider() {
        return Stream.of(
                Arguments.of("title1", "content1", "writer1"),
                Arguments.of("제목1", "본문1", "작성자1")
        );
    }

    @DisplayName("아티클을 추가한다.")
    @ParameterizedTest(name = "title: {0}, content: {1}, writer: {2}")
    @MethodSource("articleCreatedDtoProvider")
    void shouldReturnAddedArticle_WhenAdd(final String title, final String content, final String writer) {
        final ArticleCreatedDto createdDto = new ArticleCreatedDto(title, content);
        final Article actual = ArticleRepository.add(createdDto, writer);
        assertThat(actual)
                .hasNoNullFieldsOrPropertiesExcept("id")
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("content", content)
                .hasFieldOrPropertyWithValue("writer", writer);
    }

    @DisplayName("추가한 아티클을 조회한다.")
    @ParameterizedTest(name = "title: {0}, content: {1}, writer: {2}")
    @MethodSource("articleCreatedDtoProvider")
    void shouldFindCreatedArticle(final String title, final String content, final String writer) {
        final ArticleCreatedDto createdDto = new ArticleCreatedDto(title, content);
        final Article article = ArticleRepository.add(createdDto, writer);
        final Article expected = new Article(article.getId(), title, content, writer);
        final Optional<Article> actual = ArticleRepository.findById(article.getId());
        assertThat(actual)
                .isPresent()
                .usingFieldByFieldValueComparator().contains(expected);
    }
}
