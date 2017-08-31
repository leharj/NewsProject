package Models;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NewsDisplay {
    String location;
    ArrayList<String> relatedNews;
    int times[];

    public NewsDisplay(ArrayList<String> news){
        relatedNews = news;
        times = new int[8];
    }

    public void add(String title){
        relatedNews.add(title);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void incrementTime(Date date){
        Date d = Calendar.getInstance().getTime();
        long diff = d.getTime()-date.getTime();
        diff = TimeUnit.MILLISECONDS.toHours(diff);
        diff = diff/6;
        times[(int)diff]++;
    }

    public ArrayList<Integer> getTimes(){
        ArrayList<Integer> timelist = new ArrayList<>();
        for(int i=0;i<8;i++) timelist.add(times[i]);
        return timelist;
    }

    public ArrayList<String> getRelatedNews() {
        return relatedNews;
    }
}
