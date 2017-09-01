package Utilities;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchArticles {

    ArrayList<String> channelLinks;

    public FetchArticles(){
        channelLinks = new ArrayList<>();
    }

    public void storeArticles(){
        getMainLinks();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for(String link:channelLinks){
            Runnable fetcher = new ArticleFetcher(link);
            executor.execute(fetcher);
        }
        executor.shutdown();
        while(!executor.isTerminated());
    }

    private void getMainLinks(){
        channelLinks.add("http://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms");
        channelLinks.add("http://www.hindustantimes.com/rss/india/rssfeed.xml");
        channelLinks.add("http://www.thehindubusinessline.com/news/national/?service=rss");
        channelLinks.add("http://indiatoday.intoday.in/rss/article.jsp?sid=36");
        channelLinks.add("http://www.sify.com/rss2/news/article/category/national");
    }
}
