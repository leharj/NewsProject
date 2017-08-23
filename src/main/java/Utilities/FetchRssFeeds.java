package Utilities;

import Models.NewsItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchRssFeeds {

    Vector<String> vector;
    Vector<NewsItem> news;
    public FetchRssFeeds(){
        vector = new Vector<String>();
        news = new Vector<NewsItem>();
    }

    public void fetchAllRSS(int i) throws IOException{
        ArrayList<String> links = new ArrayList<String>();
        mapAllLinks(links,i);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for(String link:links){
            Runnable worker = new WorkerThread(link,vector);
            executor.execute(worker);
        }
        executor.shutdown();
        while(!executor.isTerminated()){}
        File f = new File("news.txt");
        FileWriter writer = new FileWriter(f);
        for(String news:vector){
            writer.write(news);
            writer.write("\n");
        }
        writer.close();
    }

    public void fetchRSS(int i) throws Exception{
        String table[] = {"general","political","world","business","technology","entertainment","sports"};
        ArrayList<String> links = new ArrayList<String>();
        mapLinks(links,i);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for(String link:links){
            Runnable fetcher = new FetcherThread(link,news);
            executor.execute(fetcher);
        }
        executor.shutdown();
        while(!executor.isTerminated());
        DatabaseHandler.writeToNews(news,table[i]);
    }

    private void mapLinks(ArrayList<String> links, int i){
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
        String sify = "http://www.sify.com/rss2/news/article/category/";
        String oneIndia = "http://www.oneindia.com/rss/news-";
        String businessStandard = "http://www.business-standard.com/rss/";
        String et = "http://economictimes.indiatimes.com/";
        String cnbc = "https://www.cnbc.com/id/";
        switch (i){
            case 0:
                toi = toi+"topstories.cms";
                hindu = hindu+"?service=rss";
                ht = ht+"topnews/rssfeed.xml";
                indiaToday = indiaToday+"30";
                google= google+"/headlines?ned=in&hl=en-IN";
                deccan = deccan+"top-stories.rss";
                dailyBhaskar = dailyBhaskar+"news";
                links.addAll(Arrays.asList(new String[]{toi,google, hindu, ht, indiaToday, deccan, dailyBhaskar}));
                break;
            case 1:
                toi = toi+"/-2128936835.cms";
                sify = sify+"national";
                google = google+"/headlines/section/topic/NATION.en_in/India?ned=in&hl=en-IN";
                hindu = hindu+"national/?service=rss";
                String news181 = news18+"politics.xml";
                oneIndia = oneIndia+"india-fb.xml";
                news18 = news18+"india.xml";
                ht = ht+"india/rssfeed.xml";
                indiaToday = indiaToday+"36";
                zee = zee+"india-national-news.xml";
                deccan = deccan+"national.rss";
                indianExpress = indianExpress+"india/feed";
                links.addAll(Arrays.asList(new String[]{toi,hindu,sify,oneIndia,news18,news181,ht,indiaToday,google,zee,deccan,indianExpress}));
                break;
            case 2:
                oneIndia = oneIndia+"international-fb.xml";
                toi = toi+"/296589292.cms";
                hindu = hindu+"world/?service=rss";
                sify = sify+"international";
                ht = ht+"world/rssfeed.xml";
                indiaToday = indiaToday+"61";
                google = google+"/headlines/section/topic/WORLD.en_in/World?ned=in&hl=en-IN";
                zee = zee+"world-news.xml";
                deccan = deccan+"international.rss";
                indianExpress = indianExpress+"world/feed";
                news18 = news18+"world.xml";
                links.addAll(Arrays.asList(new String[]{toi,sify,hindu,ht,news18,oneIndia,indiaToday,google,zee,deccan,indianExpress}));
                break;
            case 3:
                toi = toi+"/1898055.cms";
                google = google+"/headlines/section/topic/BUSINESS.en_in/Business?ned=in&hl=en-IN";
                hindu = "http://www.thehindubusinessline.com/";
                String hindu1 = hindu+"companies/?service=rss";
                String hindu2 = hindu+"money-and-banking/?service=rss";
                String hindu3 = hindu+"economy/?service=rss";
                hindu = hindu+"markets/?service=rss";
                ht = ht+"business/rssfeed.xml";
                indiaToday = indiaToday+"34";
                zee = zee + "business-news.xml";
                oneIndia = oneIndia+"business-fb.xml";
                deccan = deccan+"business.rss";
                news18 = news18+"business.xml";
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
                links.addAll(Arrays.asList(new String[]{toi,hindu,oneIndia,cnbc1, cnbc2, et1,et2,et3,et4,
                        businessStandard0,businessStandard1,businessStandard2,businessStandard3,businessStandard4,businessStandard5,
                        news18,google,hindu1,hindu2,hindu3,ht,indiaToday,zee,deccan,telegraph}));
                break;
            case 4:
                toi = toi+"/5880659.cms";
                hindu = "http://www.thehindubusinessline.com/info-tech/?service=rss";
                ht = ht+"tech/rssfeed.xml";
                zee = zee+"technology-news.xml";
                String google1 = google+"/headlines/section/topic/TECHNOLOGY.en_in/Technology?ned=in&hl=en-IN";
                google=google+"/headlines/section/topic/SCIENCE.en_in/Science?ned=in&hl=en-IN";
                indianExpress = indianExpress+"technology/feed";
                news18 = news18+"tech.xml";
                String sify1= sify+"technology";
                sify = sify+"science";
                telegraph = telegraph+"9";
                links.addAll(Arrays.asList(new String[]{toi,news18,hindu,sify1,sify,ht,zee,google,google1,indianExpress,telegraph}));
                break;
            case 5:
                toi = toi+"/1081479906.cms";
                ht = ht+"entertainment/rssfeed.xml";
                indiaToday = indiaToday+"85";
                zee = zee+"entertainment-news.xml";
                google = google+"/headlines/section/topic/ENTERTAINMENT.en_in/Entertainment?ned=in&hl=en-IN";
                deccan = deccan+"entertainment.rss";
                String indianExpress1 = indianExpress+"entertainment/movie-review/feed";
                String indianExpress2 = indianExpress+"entertainment/hollywood/feed";
                news18 = news18+"movies.xml";
                String indianExpress3 = indianExpress+"entertainment/bollywood/feed";
                indianExpress = indianExpress+"entertainment/feed";
                dailyBhaskar = dailyBhaskar+"entertainment";
                links.addAll(Arrays.asList
                        (new String[]{toi,ht,indiaToday,indianExpress,news18,google,indianExpress1,indianExpress2,indianExpress3,dailyBhaskar,zee,deccan}));
                break;

            case 6:
                toi = toi+"/4719148.cms";
                hindu = hindu+"sports/?service=rss";
                ht = ht+"sports/rssfeed.xml";
                indiaToday = indiaToday+"41";
                zee = zee+"sports-news.xml";
                deccan = deccan+"sports.rss";
                oneIndia = oneIndia+"sports-fb.xml";
                google = google+"/headlines/section/topic/SPORTS.en_in/Sport?ned=in&hl=en-IN";
                indianExpress = indianExpress+"sports/feed";
                indianExpress1 = indianExpress+"sports/cricket/feed";
                indianExpress2 = indianExpress+"sports/football/feed";
                telegraph = telegraph+"7";
                news181 = news18+"cricketnext.xml";
                news18 = news18+"sports.xml";
                links.addAll(Arrays.asList
                        (new String[]{toi,hindu,ht,news181,news18,indiaToday,oneIndia,google,zee,deccan,indianExpress,indianExpress1,indianExpress2,telegraph}));
                break;
        }
    }
    private void mapAllLinks(ArrayList<String> links,int i){
        mapLinks(links,i);
        String rediff = "http://www.rediff.com/rss/";
        String allIndia = "http://www.allindianewspapers.com/Feeds/";
        switch (i){
            case 0:
                String rediff1 = rediff+"inrss.xml";
                rediff = rediff+"newsrss.xml";
                allIndia = allIndia+"homepage.xml";
                links.addAll(Arrays.asList(new String[]{rediff1,rediff,allIndia}));
                break;
            case 1:
                allIndia = allIndia+"nation.xml";
                links.add(allIndia);
                break;
            case 2:
                rediff = rediff+"intnewsrss.xml";
                links.add(rediff);
                break;
            case 3:
                allIndia = allIndia+"business.xml";
                rediff = rediff+"moneyrss.xml";
                links.addAll(Arrays.asList(new String[]{allIndia,rediff}));
                break;
            case 4:
                allIndia = allIndia+"technology.xml";
                links.add(allIndia);
                break;
            case 5:
                allIndia = allIndia+"entertainment.xml";
                links.add(allIndia);
                break;
            case 6:
                allIndia = allIndia+"sports.xml";
                rediff = rediff+"sportsrss.xml";
                links.addAll(Arrays.asList(new String[]{allIndia,rediff}));
                break;
        }
    }
}
