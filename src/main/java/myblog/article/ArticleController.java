package myblog.article;

import myblog.user.dto.SessionedUser;
import myblog.user.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import support.web.argumentresolver.LoginUser;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<Void> create(
            @LoginUser final SessionedUser loginUser,
            @RequestBody final ArticleCreatedDto articleCreatedDto) {
        logger.debug("Created User : {}", articleCreatedDto);
        final Article article = ArticleRepository.add(articleCreatedDto, loginUser.getUserId());
        return ResponseEntity
                .created(URI.create("/articles/" + article.getId()))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> show(@PathVariable final int id) {
        final Optional<Article> optionalArticle = ArticleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            return ResponseEntity.ok(optionalArticle.get());
        }
        return ResponseEntity.notFound().build();
    }
}
