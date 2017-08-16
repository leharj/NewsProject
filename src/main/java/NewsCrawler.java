import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsCrawler {

    public static void main(String args[]){
        new NewsCrawler().getTrends("https://news.google.com/news/?ned=in&hl=en-IN");
    }

    public void getTrends(String url){
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("Q3vG6d kzAuJ");
            for(Element element:elements){
                String s = element.text();
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
