package Utilities;

import Models.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;

public class ArticleReader implements Runnable {

    String title;
    Date date;
    String link;
    public ArticleReader(String title, Date date, String link) {
        this.title = title;
        this.date = date;
        this.link = link;
    }

    @Override
    public void run() {
        try {
            String content;
            if (link.contains("hindustantimes"))
                content = getByClass(link, "story-details");
            else if (link.contains("timesofindia"))
                content = getByClass(link, "Normal");
            else if (link.contains("thehindubusinessline"))
                content = getByMultipleClass(link, "body");
            else if(link.contains("sify"))
                content = getByClass(link,"fullstory-txt-wrap");
            else
                content = getByClass(link, "right-story-container");

            title = title.replaceAll("'s","");
            title = title.replaceAll("'","");
            content = content.replaceAll("'s","");
            content = content.replaceAll("'","");
            DatabaseHandler.writeArticle(new Article(title,date,content,link));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getByMultipleClass(String link, String className) throws Exception{
        Document doc = Jsoup.connect(link).get();
        Elements elements = doc.getElementsByClass(className);
        StringBuilder sb = new StringBuilder();
        for(Element element:elements){
            if(element.is("p.body")) {
                sb.append(element.text());
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String getByClass(String link,String className) throws Exception {
        Document doc = Jsoup.connect(link).get();
        if(link.contains("subscription.html")) return "";
        Elements elements = doc.getElementsByClass(className);
        Element element = elements.first();
        return element.text();
    }
}
