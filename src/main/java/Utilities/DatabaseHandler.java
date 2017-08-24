package Utilities;

import Models.NewsItem;
import Models.TrendsOccurance;

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

    public static void writeToNews(Vector<NewsItem> news,String tableName) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM "+tableName);
        for(NewsItem item:news){
            Timestamp timestamp = getTimeStamp(item.getTimeStamp());
            statement.executeUpdate("INSERT INTO "+tableName+" VALUES('" + item.getTitle() + "','" + timestamp + "')");
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

    public static ArrayList<NewsItem> getNews(String tableName) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+tableName);
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

    public static ArrayList<String> getNewsForTitle(String trend,String tableName) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        ResultSet set = statement.executeQuery("SELECT DISTINCT title FROM "+tableName+" WHERE title LIKE \"%"+trend+"%\"");
        ArrayList<String> titles = new ArrayList<>();
        while(set.next())
            titles.add(set.getString(1));
        return titles;
    }

    public static void writeToNational(HashSet<String> keywords) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM national");
        for(String keyWord:keywords)
            statement.executeUpdate("INSERT INTO national VALUES ('"+keyWord+"')");
    }

    public static void writeNationalNews(Vector<String> news) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM nationalnews");
        for(String title:news) {
            title = title.replace("'","");
            title.replace("’","");
            statement.executeUpdate("INSERT INTO nationalnews VALUES ('" + title + "')");
        }
    }
}
