package foreground;

import national.background.FetchNews;
import spark.Spark;
import trends.background.FetchTrends;

import java.util.ArrayList;

import static constants.PersonalConstants.GOOGLE_API_KEY;
import static spark.Spark.get;

public class WebFlow {
    public static void main(String args[]){

        WebFlow webFlow = new WebFlow();

        Spark.staticFileLocation("");

        get("/",(req,res) -> {
           res.type("text/html");
           return webFlow.nationalTrendsPage();
        });

        get("/news/:news",(req,res) ->{
            res.type("text/html");
            int news = Integer.parseInt(req.params(":news"));
           return  webFlow.getPage(news);
        });

        get("/national",(req,res) ->{
           res.type("text/html");
           return webFlow.nationalTrendsPage();
        });

        Spark.exception(Exception.class,(exception, request, response) -> {
            exception.printStackTrace();
        });
    }

    private String getPage(int i){
        String s = getTopHtml(i);
        StringBuilder sb = new StringBuilder(s);

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
        FetchNews fetchNews = new FetchNews();
        try{
            sb.append(fetchNews.nationalNews());
        }catch (Exception e){
            e.printStackTrace();
        }
        sb.append(getChartJS(fetchNews.getChartParams()));
        sb.append("\n</body>\n<html>");
        return sb.toString();
    }

    private String getTopHtml(int i){
        String s = "<html>\n" +
                "<head>\n" +
                "<title>Whats Trending</title> <meta charset=\"utf-8\">\n" +
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
                "    var div = panel.nextElementSibling;\n"+
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

    private String getChartJS(ArrayList<Integer> chartParams){
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>\n");
        sb.append("<script type=\"text/javascript\">\n");
        String str = "function myfunction(str, val1, val2, val3, val4, val5, val6, val7, val8){\n" +
                "       if((val1+val2+val3+val4+val5+val6+val7+val8)==0){\n" +
                "           document.getElementById(str).style.height='0px'\n" +
                "           return;\n" +
                "       }\n" +
                "          var chart = new CanvasJS.Chart(str,\n" +
                "          {\n" +
                "               title:{\n" +
                "                    text: \"Trending Pattern\"\n" +
                "               },\n" +
                "               axisY:{\n" +
                "                    gridThickness: 0,\n" +
                "                    labelFontColor: \"White\"\n" +
                "               },\n" +
                "               axisX:{\n" +
                "                    gridThickness: 0,\n" +
                "                    labelFontColor: \"White\"\n" +
                "               },\n" +
                "               toolTip:{\n" +
                "                    gridThickness: 0,\n" +
                "                    enabled: false\n" +
                "               },\n" +
                "\n" +
                "               data: [\n" +
                "               {\n" +
                "                  type: \"line\",\n" +
                "               \n" +
                "                  dataPoints: [\n" +
                "                  { x: 1, y: val1},\n" +
                "                  { x: 2, y: val2},\n" +
                "                  { x: 3, y: val3},\n" +
                "                  { x: 4, y: val4},\n" +
                "                  { x: 5, y: val5},\n" +
                "                  { x: 6, y: val6},\n" +
                "                  { x: 7, y: val7},\n" +
                "                  { x: 8, y: val8},\n" +
                "                  ]\n" +
                "                }\n" +
                "                ]\n" +
                "          });\n" +
                "          chart.render();\n" +
                "    }\n";
        sb.append(str);
        for(int i=0;i<chartParams.size()/8;i++){
            sb.append("myfunction(\"chartContainer"+(i+1)+"\","+chartParams.get(8*i+7)+","+chartParams.get(8*i+6)+","+chartParams.get(8*i+5)+","+chartParams.get(8*i+4)+","+chartParams.get(8*i+3)+","+chartParams.get(8*i+2)+","+chartParams.get(8*i+1)+","+chartParams.get(8*i)+")\n");
        }
        sb.append("</script>\n");
        sb.append("<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?key="+GOOGLE_API_KEY+"&sensor=false\"></script>\n");
        sb.append("<script>\n");
        String s = "var x;\nvar y;\n      function initMap(loc,id) {\n" +
        "         fun(this);\n"+
        "         if(loc=='India'){ \n" +
                "document.getElementById(id).style.height='0px';\n" +
                "return;}\n"+
        "         var geocoder =  new google.maps.Geocoder();\n" +
        "         geocoder.geocode( { 'address': loc+', India'}, function(results, status) {\n" +
        "             if (status == google.maps.GeocoderStatus.OK) {\n" +
        "                x = results[0].geometry.location.lat(); \n" +
        "                y = results[0].geometry.location.lng(); \n" +
        "             }\n" +
        "           display(x,y);\n"+
        "         });\n"+
                "function display(x,y) {\n" +
                "\tvar uluru = {lat: parseFloat(x), lng: parseFloat(y)};\n" +
                "        var map = new google.maps.Map(document.getElementById(id), {\n" +
                "          zoom: 4,\n" +
                "          center: uluru\n" +
                "        });\n" +
                "        var marker = new google.maps.Marker({\n" +
                "          position: uluru,\n" +
                "          map: map\n" +
                "        });\n" +
                "}\n"+
        "      }\n" +
                "function fun(abc) {\n" +
                "    abc.classList.toggle(\"active\");\n" +
                "    var panel = abc.nextElementSibling;\n" +
                "    var div = panel.nextElementSibling;\n" +
                "    if (panel.style.maxHeight){\n" +
                "      panel.style.maxHeight = null;\n" +
                "    } else {\n" +
                "      panel.style.maxHeight = panel.scrollHeight + \"px\";\n" +
                "    } \n" +
                "  }\n"+
        "</script>\n";
        sb.append(s);
        return sb.toString();
    }
}