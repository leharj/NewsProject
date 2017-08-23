package Utilities;

import Models.NewsItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class FetcherThread implements Runnable {

    private String link;
    private Vector<NewsItem> news;

    public FetcherThread(String link, Vector<NewsItem> news) {
        this.link = link;
        this.news = news;
    }

    @Override
    public void run() {
        try{
            DocumentBuilderFactory inputFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = inputFactory.newDocumentBuilder();
            Document document = builder.parse(link);
            Calendar today = Calendar.getInstance();
            Date d = today.getTime();
            NodeList nodes = document.getElementsByTagName("title");
            NodeList nd = document.getElementsByTagName("pubDate");
            int k = 0;
            if(link.equals("http://zeenews.india.com/rss/india-national-news.xml")) {
                nd = document.getElementsByTagName("pubdate");
            }
            else if(link.contains("zeenews")||link.contains("cnbc")) k++;
            else if(link.equals("https://www.telegraphindia.com/feeds/rss.jsp?id=9")||link.equals("https://www.telegraphindia.com/feeds/rss.jsp?id=7"))k++;
            for(int i=0;i<nodes.getLength();i++){
                Element element = (Element)nodes.item(i);
                if(element.getParentNode().getNodeName().equals("item")){
                    String s = element.getTextContent();
                    if(s.length()>30) {
                        s = s.replace("'","");
                        Element dateElement = (Element) nd.item(k);
                        Date date = getDate(dateElement.getTextContent(), link);
                        long diff = d.getTime() - date.getTime();
                        long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
                        if (daysDiff < 2)
                            news.add(new NewsItem(s,date));
                        k++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(link);
        }
    }

    public Date getDate(String date,String link) throws Exception{
        if(link.equals("http://www.deccanherald.com/rss-internal/national.rss")||link.equals("http://www.deccanherald.com/rss-internal/sports.rss")||
                link.equals("http://www.deccanherald.com/rss-internal/business.rss"))
            return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(date);
        if(link.contains("zeenews"))
            return new SimpleDateFormat("EEEE, MMMM dd, yyyy, HH:mm z").parse(date);
        if(link.contains("deccanherald"))
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        if(link.contains("bhaskar"))
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(date);
        if(link.contains("news18"))
            return new SimpleDateFormat("EEEE,MMMM dd,yyyy h:mm a").parse(date);
        if(link.contains("cnbc"))
            return new SimpleDateFormat("EEE, dd MMM yyy HH:mm z").parse(date);
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(date);
    }
}