package Models;

import java.util.Date;

public class NewsItem {
    private String title;
    private Date timeStamp;


    public NewsItem(String title,Date date){
        this.timeStamp = date;
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public Date getTimeStamp(){
        return timeStamp;
    }
}
