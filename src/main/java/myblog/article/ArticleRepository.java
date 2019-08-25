package myblog.article;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepository {

    private static final List<Article> articles = new ArrayList<>();

    public static Optional<Article> findById(final int id) {
        return articles.stream()
                .filter(article -> article.matchId(id))
                .findFirst();
    }

    public static Article add(final ArticleCreatedDto createdDto, final String loginUserId) {
        final Article article = new Article(articles.size() + 1,
                createdDto.getTitle(),
                createdDto.getContent(),
                loginUserId);
        articles.add(article);
        return article;
    }
}
