package trends.Utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Vector;

public class WorkerThread implements Runnable {

    private String link;
    private Vector<String> vector;
    WorkerThread(String s,Vector<String> v){
        link = s;
        vector = v;
    }
    @Override
    public void run() {
        try {
            DocumentBuilderFactory inputFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = inputFactory.newDocumentBuilder();
            Document document = builder.parse(link);
            NodeList nodes = document.getElementsByTagName("title");
            for(int i=0;i<nodes.getLength();i++){
                Element element = (Element)nodes.item(i);
                if(element.getParentNode().getNodeName().equals("item")){
                    String s = element.getTextContent();
                    if(s.length()>30)
                        vector.add(s);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(link);
        }
    }
}