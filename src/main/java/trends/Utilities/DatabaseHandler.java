package trends.Utilities;

import trends.Models.NewsItem;
import trends.Models.TrendsOccurance;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseHandler {
    static Connection con;
    public static void connectDb() throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trends","root","password");

    }
    public static void writeKeywords(Set<String> trendSet) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM keywords");
        for(String s:trendSet)
            statement.executeUpdate("INSERT INTO keywords VALUES ('"+s+"')");
        con.close();
    }

    public static void writeToNews(Vector<NewsItem> news) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM news");
        for(NewsItem item:news){
            Timestamp timestamp = getTimeStamp(item.getTimeStamp());
            statement.executeUpdate("INSERT INTO news VALUES('" + item.getTitle() + "','" + timestamp + "')");
        }
        con.close();
    }

    private static Timestamp getTimeStamp(Date date){
        return new Timestamp(date.getTime());
    }

    public static ArrayList<String> getTrends() throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM keywords");
        ArrayList<String> trends = new ArrayList<String>();
        while(resultSet.next())
            trends.add(resultSet.getString(1));
        con.close();
        return  trends;
    }

    public static ArrayList<NewsItem> getNews() throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM news");
        ArrayList<NewsItem> news = new ArrayList<NewsItem>();
        while(resultSet.next()){
            String title = resultSet.getString(1);
            Timestamp ts = resultSet.getTimestamp(2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ts.getTime());
            Date date = calendar.getTime();
            news.add(new NewsItem(title,date));
        }
        return news;
    }

    public static void writeToTrends(HashMap<String, TrendsOccurance> map) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM trends");
        for (String trend:map.keySet()){
            TrendsOccurance occurance = map.get(trend);
            statement.executeUpdate("INSERT INTO trends VALUES ('"+occurance.getTrend()+"',"+occurance.getLast_6_hrs()+
                    ","+occurance.getLast_12_hrs()+","+occurance.getLast_1_day()+","+occurance.getLast_2_days()+")");
        }
        con.close();
    }

    public static ArrayList<String> getNewsForTitle(String trend) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        ResultSet set = statement.executeQuery("SELECT DISTINCT title FROM news WHERE title LIKE \"%"+trend+"%\"");
        ArrayList<String> titles = new ArrayList<String>();
        while(set.next())
            titles.add(set.getString(1));
        return titles;
    }
}
