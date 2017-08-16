import spark.Spark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static spark.Spark.get;

public class WebFlow {
    public static void main(String args[]){

        Spark.staticFileLocation("");

        get("/",(req,res) -> {
           res.type("text/html");
           return getPage(0);
        });

        get("/:news",(req,res) ->{
            int news = Integer.parseInt(req.params(":news"));
           return  getPage(news);
        });
    }

    private static String getPage(int i){
        String s = "<html>\n" +
                "<head>\n" +
                "<title>Welcome</title> <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n" +
                "  <link rel=\"stylesheet\" href=\"style.css\">\n" +
                "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>\n" +
                "  <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "  <form class=\"temp\">\n" +
                "      <label for=\"sel1\">Select list (select one):</label>\n" +
                "      <select id=\"sel1\" onchange = \" location=this.value;\">\n" +
                "        <option value=\"0\""+(i==0?"selected=\"true\"":"")+">General</option>\n" +
                "        <option value=\"1\""+(i==1?"selected=\"true\"":"")+">National</option>\n" +
                "        <option value=\"2\""+(i==2?"selected=\"true\"":"")+">World</option>\n" +
                "        <option value=\"3\""+(i==3?"selected=\"true\"":"")+">Business</option>\n" +
                "        <option value=\"4\""+(i==4?"selected=\"true\"":"")+">Technology</option>\n" +
                "        <option value=\"5\""+(i==5?"selected=\"true\"":"")+">Entertainment</option>\n" +
                "        <option value=\"6\""+(i==6?"selected=\"true\"":"")+">Sports</option>\n" +
                "      </select>\n" +
                "    </form>\n" +
                "  </div>\n" +
                "  <div class=\"page-header\"></div>\n";
        StringBuilder sb = new StringBuilder(s);
        try {
            new FetchRssFeeds().fetchRSS(i);
        }catch (IOException e){
            e.printStackTrace();
        }
        Runtime rut = Runtime.getRuntime();
        try{
            Process p = rut.exec(new String[]{"Rscript","trends2.r"});
            p.waitFor();
            System.out.println("Hello");
        }catch(Exception e){
            e.printStackTrace();
        }
        sb.append(new FetchTrends().getTrends(i));
        sb.append("\n</body>\n</html>");
        return sb.toString();
    }
}
