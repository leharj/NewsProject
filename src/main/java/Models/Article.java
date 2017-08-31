package Models;

import java.util.Date;

public class Article {

    private String title;
    private Date date;
    private String content;
    private String link;

    public Article(String title,Date date,String content,String link){
        this.content = content;
        this.date = date;
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }
}
