package Utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticleFetcher implements Runnable {

    String link;
    public ArticleFetcher(String link){
        this.link = link;
    }
    @Override
    public void run() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(link);
            NodeList linkList = document.getElementsByTagName("link");
            NodeList titleList = document.getElementsByTagName("title");
            NodeList dateList = document.getElementsByTagName("pubDate");
            removeUnwantedNodes(linkList);
            removeUnwantedNodes(titleList);
            removeUnwantedNodes(dateList);

            ExecutorService executor = Executors.newFixedThreadPool(8);
            for(int i=0;i<linkList.getLength();i++){
                String title = titleList.item(i).getTextContent();
                Date date = null;
                try {
                    date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(dateList.item(i).getTextContent());
                }catch (Exception e){
                    e.printStackTrace();
                }
                String articleLink = linkList.item(i).getTextContent();
                Runnable storing = new ArticleReader(title,date,articleLink);
                executor.execute(storing);
            }
            executor.shutdown();
            while (!executor.isTerminated());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void removeUnwantedNodes(NodeList list){
        for(int i=list.getLength()-1;i>=0;i--){
            Element element = (Element)list.item(i);
            if(element.getParentNode().getNodeName().equals("item"));
            else element.getParentNode().removeChild(element);
        }
    }
}
