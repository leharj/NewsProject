
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchRssFeeds {

    Vector<String> vector;
    public FetchRssFeeds(){
        vector = new Vector<String>();
    }

    public void fetchRSS(int i) throws IOException{
        ArrayList<String> links = new ArrayList<String>();
        mapLinks(links,i);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for(String link:links){
            Runnable worker = new WorkerThread(link,vector);
            executor.execute(worker);
        }
        executor.shutdown();
        while(!executor.isTerminated()){}
        FileWriter writer = new FileWriter("news.txt");
        for(String news:vector){
            writer.write(news);
            writer.write("\n");
        }
        writer.close();
    }

    public void mapLinks(ArrayList<String> links,int i){
        String toi = "http://timesofindia.indiatimes.com/rssfeeds";
        String hindu = "http://www.thehindubusinessline.com/news/";
        String ht = "http://www.hindustantimes.com/rss/";
        String indiaToday = "http://indiatoday.intoday.in/rss/article.jsp?sid=";
        String zee = "http://zeenews.india.com/rss/";
        String deccan = "http://www.deccanherald.com/rss-internal/";
        String indianExpress = "http://indianexpress.com/section/";
        String telegraph = "https://www.telegraphindia.com/feeds/rss.jsp?id=";
        String dailyBhaskar="http://daily.bhaskar.com/rss/";
        String google = "https://news.google.com/news/rss";
        switch (i){
            case 0:
                toi = toi+"topstories.cms";
                hindu = hindu+"?service=rss";
                ht = ht+"topnews/rssfeed.xml";
                indiaToday = indiaToday+"30";
                deccan = deccan+"top-stories.rss";
                dailyBhaskar = dailyBhaskar+"news";
                links.addAll(Arrays.asList(new String[]{toi,google, hindu, ht, indiaToday, deccan, dailyBhaskar}));
                break;
            case 1:
                toi = toi+"/-2128936835.cms";
                google = google+"/headlines/section/topic/NATION.en_in/India";
                hindu = hindu+"national/?service=rss";
                ht = ht+"india/rssfeed.xml";
                indiaToday = indiaToday+"36";
                zee = zee+"india-national-news.xml";
                deccan = deccan+"national.rss";
                indianExpress = indianExpress+"india/feed";
                links.addAll(Arrays.asList(new String[]{toi,hindu,ht,indiaToday,google,zee,deccan,indianExpress}));
                break;
            case 2:
                toi = toi+"/296589292.cms";
                hindu = hindu+"world/?service=rss";
                ht = ht+"world/rssfeed.xml";
                indiaToday = indiaToday+"61";
                google = google+"/headlines/section/topic/WORLD.en_in/World";
                zee = zee+"world-news.xml";
                deccan = deccan+"international.rss";
                indianExpress = indianExpress+"world/feed";
                links.addAll(Arrays.asList(new String[]{toi,hindu,ht,indiaToday,google,zee,deccan,indianExpress}));
                break;
            case 3:
                toi = toi+"/1898055.cms";
                google = google+"/headlines/section/topic/BUSINESS.en_in/Business";
                hindu = "http://www.thehindubusinessline.com/";
                String hindu1 = hindu+"companies/?service=rss";
                String hindu2 = hindu+"money-and-banking/?service=rss";
                String hindu3 = hindu+"economy/?service=rss";
                hindu = hindu+"markets/?service=rss";
                ht = ht+"business/rssfeed.xml";
                indiaToday = indiaToday+"34";
                zee = zee+"business-news.xml";
                deccan = deccan+"business.rss";
                telegraph = telegraph+"9";
                links.addAll(Arrays.asList(new String[]{toi,hindu,google,hindu1,hindu2,hindu3,ht,indiaToday,zee,deccan,telegraph}));
                break;
            case 4:
                toi = toi+"/5880659.cms";
                hindu = "http://www.thehindubusinessline.com/info-tech/?service=rss";
                ht = ht+"tech/rssfeed.xml";
                zee = zee+"technology-news.xml";
                String google1 = google+"/headlines/section/topic/TECHNOLOGY.en_in/Technology";
                google=google+"/headlines/section/topic/SCIENCE.en_in/Science";
                indianExpress = indianExpress+"technology/feed";
                telegraph = telegraph+"9";
                links.addAll(Arrays.asList(new String[]{toi,hindu,ht,zee,google,google1,indianExpress,telegraph}));
                break;
            case 5:
                toi = toi+"/1081479906.cms";
                ht = ht+"entertainment/rssfeed.xml";
                indiaToday = indiaToday+"85";
                zee = zee+"entertainment-news.xml";
                google = google+"/headlines/section/topic/ENTERTAINMENT.en_in/Entertainment";
                deccan = deccan+"entertainment.rss";
                String indianExpress1 = indianExpress+"entertainment/movie-review/feed";
                String indianExpress2 = indianExpress+"entertainment/hollywood/feed";
                String indianExpress3 = indianExpress+"entertainment/bollywood/feed";
                indianExpress = indianExpress+"entertainment/feed";
                dailyBhaskar = dailyBhaskar+"entertainment";
                links.addAll(Arrays.asList
                        (new String[]{toi,ht,indiaToday,indianExpress,google,indianExpress1,indianExpress2,indianExpress3,dailyBhaskar,zee,deccan}));
                break;

            case 6:
                toi = toi+"/4719148.cms";
                hindu = hindu+"sports/?service=rss";
                ht = ht+"sports/rssfeed.xml";
                indiaToday = indiaToday+"41";
                zee = zee+"sports-news.xml";
                deccan = deccan+"sports.rss";
                google = google+"/headlines/section/topic/SPORTS.en_in/Sport";
                indianExpress = indianExpress+"sports/feed";
                indianExpress1 = indianExpress+"sports/cricket/feed";
                indianExpress2 = indianExpress+"sports/football/feed";
                telegraph = telegraph+"7";
                links.addAll(Arrays.asList
                        (new String[]{toi,hindu,ht,indiaToday,google,zee,deccan,indianExpress,indianExpress1,indianExpress2,telegraph}));
                break;
        }
    }
}
