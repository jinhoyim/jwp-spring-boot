package myblog.article;

public class ArticleCreatedDto {
    private String title;
    private String content;

    private ArticleCreatedDto() {
    }

    public ArticleCreatedDto(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "ArticleCreatedDto{" +
                "title='" + this.title + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }
}
