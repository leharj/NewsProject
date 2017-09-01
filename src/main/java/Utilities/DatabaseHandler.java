package Utilities;

import Models.Article;
import Models.NewsItem;
import Models.TrendsOccurance;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static constants.PersonalConstants.DBNAME;
import static constants.PersonalConstants.DBPASS;
import static constants.PersonalConstants.DBUSER;

public class DatabaseHandler {
    static Connection con;
    public static void connectDb() throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DBNAME,DBUSER,DBPASS);

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
        con.close();
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
        con.close();
        return titles;
    }

    public static void writeToNational(HashSet<String> keywords) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM national");
        for(String keyWord:keywords)
            statement.executeUpdate("INSERT INTO national VALUES ('"+keyWord+"')");
        con.close();
    }

    public static HashSet<String> getNationalTrends() throws Exception{
        connectDb();
        HashSet<String> trendSet = new HashSet<>();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM national");
        while(rs.next())
            trendSet.add(rs.getString(1));
        return trendSet;
    }

    public synchronized static void writeArticle(Article article) throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        if(compareDate(article.getDate())) {
        Timestamp ts = getTimeStamp(article.getDate());
            statement.executeUpdate("INSERT INTO article VALUES ('" + article.getTitle() + "', '" + ts + "', '" + article.getContent()
                    + "', '"+article.getLink()+"')");
        }
        con.close();
    }

    private static boolean compareDate(Date date){
        long current = Calendar.getInstance().getTime().getTime();
        long article = date.getTime();
        long diff = current - article;
        diff = TimeUnit.MILLISECONDS.toHours(diff);
        return diff<6;
    }

    public static HashSet<Article> getArticles() throws Exception{
        connectDb();
        Statement statement = con.createStatement();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-2);
        Date date = calendar.getTime();
        Timestamp ts = getTimeStamp(date);
        HashSet<Article> articles = new HashSet<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM article WHERE time > '"+ts+"'");
        while(rs.next()){
            Timestamp timestamp = rs.getTimestamp(2);
            Date d = new Date(timestamp.getTime());
            Article article = new Article(rs.getString(1),d,rs.getString(3),rs.getString(4));
            articles.add(article);
        }
        return articles;
    }
}
