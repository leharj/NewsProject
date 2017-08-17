
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
        String news18 = "http://www.news18.com/rss/";
        String rediff = "http://www.rediff.com/rss/";
        String sify = "http://www.sify.com/rss2/news/article/category/";
        String oneIndia = "http://www.oneindia.com/rss/news-";
        String allIndia = "http://www.allindianewspapers.com/Feeds/";
        String businessStandard = "http://www.business-standard.com/rss/";
        String et = "http://economictimes.indiatimes.com/";
        String cnbc = "https://www.cnbc.com/id/";
        switch (i){
            case 0:
                toi = toi+"topstories.cms";
                hindu = hindu+"?service=rss";
                ht = ht+"topnews/rssfeed.xml";
                indiaToday = indiaToday+"30";
                deccan = deccan+"top-stories.rss";
                String rediff1 = rediff+"inrss.xml";
                rediff = rediff+"newsrss.xml";
                dailyBhaskar = dailyBhaskar+"news";
                allIndia = allIndia+"homepage.xml";
                links.addAll(Arrays.asList(new String[]{toi,allIndia,rediff,rediff1,google, hindu, ht, indiaToday, deccan, dailyBhaskar}));
                break;
            case 1:
                toi = toi+"/-2128936835.cms";
                sify = sify+"national";
                google = google+"/headlines/section/topic/NATION.en_in/India";
                hindu = hindu+"national/?service=rss";
                String news181 = news18+"politics.xml";
                oneIndia = oneIndia+"india-fb.xml";
                news18 = news18+"india.xml";
                ht = ht+"india/rssfeed.xml";
                indiaToday = indiaToday+"36";
                zee = zee+"india-national-news.xml";
                deccan = deccan+"national.rss";
                indianExpress = indianExpress+"india/feed";
                allIndia = allIndia+"nation.xml";
                links.addAll(Arrays.asList(new String[]{toi,hindu,sify,oneIndia,news18,allIndia,news181,ht,indiaToday,google,zee,deccan,indianExpress}));
                break;
            case 2:
                oneIndia = oneIndia+"international-fb.xml";
                toi = toi+"/296589292.cms";
                rediff = rediff+"intnewsrss.xml";
                hindu = hindu+"world/?service=rss";
                sify = sify+"international";
                ht = ht+"world/rssfeed.xml";
                indiaToday = indiaToday+"61";
                google = google+"/headlines/section/topic/WORLD.en_in/World";
                zee = zee+"world-news.xml";
                deccan = deccan+"international.rss";
                indianExpress = indianExpress+"world/feed";
                news18 = news18+"world.xml";
                links.addAll(Arrays.asList(new String[]{toi,sify,hindu,ht,news18,oneIndia,rediff,indiaToday,google,zee,deccan,indianExpress}));
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
                allIndia = allIndia+"business.xml";
                indiaToday = indiaToday+"34";
                zee = zee + "business-news.xml";
                oneIndia = oneIndia+"business-fb.xml";
                deccan = deccan+"business.rss";
                news18 = news18+"business.xml";
                rediff = rediff+"moneyrss.xml";
                String businessStandard0 = businessStandard + "companies-101.rss";
                String businessStandard1 = businessStandard + "management-107.rss";
                String businessStandard2 = businessStandard + "economy-policy-102.rss";
                String businessStandard3 = businessStandard + "finance-103.rss";
                String businessStandard4 = businessStandard + "markets-106.rss";
                String businessStandard5 = businessStandard + "home_page_top_stories.rss";
                String et1 = et + "markets/rssfeeds/1977021501.cms";
                String et2 = et + "industry/rssfeeds/13352306.cms";
                String et3 = et + "small-biz/rssfeeds/5575607.cms";
                String et4 = et + "wealth/rssfeeds/837555174.cms";
                String cnbc1 = cnbc + "20910258/device/rss/rss.html";
                String cnbc2 = cnbc + "10001147/device/rss/rss.html";
                telegraph = telegraph+"9";
                links.addAll(Arrays.asList(new String[]{toi,hindu,oneIndia,allIndia,cnbc1, cnbc2, et1,et2,et3,et4,businessStandard0,businessStandard1,businessStandard2,businessStandard3,businessStandard4,businessStandard5,rediff,news18,google,hindu1,hindu2,hindu3,ht,indiaToday,zee,deccan,telegraph}));
                break;
            case 4:
                toi = toi+"/5880659.cms";
                hindu = "http://www.thehindubusinessline.com/info-tech/?service=rss";
                ht = ht+"tech/rssfeed.xml";
                allIndia = allIndia+"technology.xml";
                zee = zee+"technology-news.xml";
                String google1 = google+"/headlines/section/topic/TECHNOLOGY.en_in/Technology";
                google=google+"/headlines/section/topic/SCIENCE.en_in/Science";
                indianExpress = indianExpress+"technology/feed";
                news18 = news18+"tech.xml";
                String sify1= sify+"technology";
                sify = sify+"science";
                telegraph = telegraph+"9";
                links.addAll(Arrays.asList(new String[]{toi,news18,allIndia,hindu,sify1,sify,ht,zee,google,google1,indianExpress,telegraph}));
                break;
            case 5:
                toi = toi+"/1081479906.cms";
                ht = ht+"entertainment/rssfeed.xml";
                indiaToday = indiaToday+"85";
                zee = zee+"entertainment-news.xml";
                allIndia = allIndia+"entertainment.xml";
                google = google+"/headlines/section/topic/ENTERTAINMENT.en_in/Entertainment";
                deccan = deccan+"entertainment.rss";
                String indianExpress1 = indianExpress+"entertainment/movie-review/feed";
                String indianExpress2 = indianExpress+"entertainment/hollywood/feed";
                news18 = news18+"movies.xml";
                String indianExpress3 = indianExpress+"entertainment/bollywood/feed";
                indianExpress = indianExpress+"entertainment/feed";
                dailyBhaskar = dailyBhaskar+"entertainment";
                links.addAll(Arrays.asList
                        (new String[]{toi,ht,indiaToday,indianExpress,allIndia,news18,google,indianExpress1,indianExpress2,indianExpress3,dailyBhaskar,zee,deccan}));
                break;

            case 6:
                toi = toi+"/4719148.cms";
                allIndia = allIndia+"sports.xml";
                hindu = hindu+"sports/?service=rss";
                ht = ht+"sports/rssfeed.xml";
                indiaToday = indiaToday+"41";
                zee = zee+"sports-news.xml";
                deccan = deccan+"sports.rss";
                oneIndia = oneIndia+"sports-fb.xml";
                //rediff1 = rediff+"cricketrss.xml";
                rediff = rediff+"sportsrss.xml";
                google = google+"/headlines/section/topic/SPORTS.en_in/Sport";
                indianExpress = indianExpress+"sports/feed";
                indianExpress1 = indianExpress+"sports/cricket/feed";
                indianExpress2 = indianExpress+"sports/football/feed";
                telegraph = telegraph+"7";
                news181 = news18+"cricketnext.xml";
                news18 = news18+"sports.xml";
                links.addAll(Arrays.asList
                        (new String[]{toi,hindu,ht,news181,news18,indiaToday,allIndia,oneIndia,rediff,google,zee,deccan,indianExpress,indianExpress1,indianExpress2,telegraph}));
                break;
        }
    }
}
