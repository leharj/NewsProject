package foreground;

import Utilities.FetchRssFeeds;
import national.background.FetchNews;
import spark.Spark;
import trends.background.FetchTrends;

import static spark.Spark.get;

public class WebFlow {
    public static void main(String args[]){

        WebFlow webFlow = new WebFlow();

        Spark.staticFileLocation("");

        get("/",(req,res) -> {
           res.type("text/html");
           return webFlow.nationalTrendsPage();
        });

        get("/:news",(req,res) ->{
            res.type("text/html");
            int news = Integer.parseInt(req.params(":news"));
           return  webFlow.getPage(news);
        });

        get("/national",(req,res) ->{
           res.type("text/html");
           return webFlow.nationalTrendsPage();
        });
    }

    private String getPage(int i){
        String s = getTopHtml(i);
        StringBuilder sb = new StringBuilder(s);
        try {
            new FetchRssFeeds().fetchRSS(i);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            sb.append(new FetchTrends().getTrends(i));
        }catch (Exception e){
            e.printStackTrace();
        }

        String str = getJavascript();
        sb.append(str);
        sb.append("\n</body>\n</html>");
        return sb.toString();
    }

    private String nationalTrendsPage(){
        String s = getTopHtml(100);
        StringBuilder sb = new StringBuilder(s);
        try{
            sb.append(new FetchNews().nationalNews());
        }catch (Exception e){
            e.printStackTrace();
        }

        sb.append(getJavascript());
        sb.append("\n</body>\n<html>");
        return sb.toString();
    }

    private String getTopHtml(int i){
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
                "<div class=\"container-fluid bg-1 text-center\">\n"+
                "<img src=\"trends.png\" class=\"img-circle\" alt=\"Trends\" width=\"350\" height=\"350\">\n";
        StringBuilder sb = new StringBuilder(s);
        if(i!=100)
            sb.append(getForm(i));
        String t = "</div>\n" +
                "<br>\n"+
                "<br>\n";
        sb.append(t);
        return sb.toString();
    }

    private String getForm(int i){
        String s = "  <div class=\"container\">\n" +
                "  <form class=\"temp\">\n" +
                "      <label for=\"sel1\" style=\"color:white\">Select list (select one):</label>\n" +
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
                "  </div>\n";
        return s;
    }

    private String getJavascript(){
        String str = "<script>\n" +
                "var acc = document.getElementsByClassName(\"accordion\");\n" +
                "var i;\n" +
                "for (i = 0; i < acc.length; i++) {\n" +
                "  acc[i].onclick = function() {\n" +
                "    this.classList.toggle(\"active\");\n" +
                "    var panel = this.nextElementSibling;\n" +
                "    if (panel.style.maxHeight){\n" +
                "      panel.style.maxHeight = null;\n" +
                "    } else {\n" +
                "      panel.style.maxHeight = panel.scrollHeight + \"px\";\n" +
                "    } \n" +
                "  }\n" +
                "}\n" +
                "</script>";
        return str;
    }
}