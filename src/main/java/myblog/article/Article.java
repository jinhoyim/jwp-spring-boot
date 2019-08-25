package myblog.article;

public class Article {

    private int id;
    private String title;
    private String content;
    private String writer;

    private Article() {
    }

    public Article(final int id, final String title, final String content, final String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getWriter() {
        return this.writer;
    }

    public boolean matchId(final int id) {
        return this.id == id;
    }
}
