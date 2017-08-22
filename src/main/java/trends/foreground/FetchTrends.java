package trends.foreground;

import trends.Models.NewsItem;
import trends.Models.TrendsOccurance;
import trends.Utilities.DatabaseHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class FetchTrends {

    public String getTrends() throws Exception{
        ArrayList<String> trends = new ArrayList<String>();
        ArrayList<NewsItem> news = new ArrayList<NewsItem>();
        ArrayList<TrendsOccurance> collection = new ArrayList<TrendsOccurance>();
        HashMap<String,TrendsOccurance> map = new HashMap<String, TrendsOccurance>();
        Calendar today = Calendar.getInstance();
        Date date = today.getTime();
        trends = DatabaseHandler.getTrends();
        news = DatabaseHandler.getNews();
        for(String trend:trends){
            for(NewsItem newsItem:news){
                boolean flag = Pattern.compile(Pattern.quote(trend),Pattern.CASE_INSENSITIVE).matcher(newsItem.getTitle()).find();
                long diff = date.getTime()-newsItem.getTimeStamp().getTime();
                diff = TimeUnit.MILLISECONDS.toHours(diff);
                if(flag){
                    if(map.get(trend)==null){
                        TrendsOccurance occurance = new TrendsOccurance(trend);
                        if(diff<6) occurance.setLast_6_hrs(1);
                        else if(diff<12) occurance.setLast_12_hrs(1);
                        else if(diff<24) occurance.setLast_1_day(1);
                        else occurance.setLast_2_days(1);
                        map.put(trend,occurance);
                    }
                    else{
                        TrendsOccurance occurance = map.get(trend);
                        if(diff<6) occurance.setLast_6_hrs(occurance.getLast_6_hrs()+1);
                        else if(diff<12) occurance.setLast_12_hrs(occurance.getLast_12_hrs()+1);
                        else if(diff<24) occurance.setLast_1_day(occurance.getLast_1_day()+1);
                        else occurance.setLast_2_days(occurance.getLast_2_days()+1);
                        map.put(trend,occurance);
                    }
                }
            }
        }
        DatabaseHandler.writeToTrends(map);
        for(String trend:map.keySet()) {
            if(map.get(trend).getSum()>=2)
                collection.add(map.get(trend));
        }
        Collections.sort(collection);
        ArrayList<String> titleNews;
        StringBuilder sb = new StringBuilder();
        String str = "<div class=\"container col-md-6\">\n";
        sb.append(str);

        for(int i=0;i<(collection.size()+1)/2;i++) {
            TrendsOccurance occurance = collection.get(2 * i);
            titleNews = DatabaseHandler.getNewsForTitle(occurance.getTrend());
            listBuilder(sb, occurance, titleNews);
        }
        sb.append("</div>\n");
        str = "<div class=\"container col-md-6\">\n";
        sb.append(str);

        for(int i=0;i<collection.size()/2;i++){
            TrendsOccurance occurance = collection.get(2*i+1);
            titleNews = DatabaseHandler.getNewsForTitle(occurance.getTrend());
            listBuilder(sb, occurance, titleNews);
        }
        sb.append("</div>\n");

        return sb.toString();
    }

    private void listBuilder(StringBuilder sb, TrendsOccurance occurance, ArrayList<String> titleNews) {
        String str;
        if(titleNews.size()>=2) {
            str = "<button class = \"accordion\">" + occurance.getTrend() + "</button>\n"
                    + "<div class=\"panel\">\n"
                    + "<ul class=\"list-group\">\n";
            sb.append(str);

            for (String relatedNews : titleNews)
                sb.append("     <li class=\"list-group-item\">" + relatedNews + "</li>\n");
            sb.append(" </ul>\n");
            sb.append("</div>\n");
        }
    }
}