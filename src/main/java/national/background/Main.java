package national.background;

import Utilities.DatabaseHandler;
import Utilities.FetchArticles;
import Utilities.FetchRssFeeds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import static constants.Constants.stopWordsSet;
import static constants.Constants.verbSet;

public class Main {

    public static void main(String args[]){
        new Main().init();
    }

    public void init(){
        Thread t1 = new Thread(new BackgroundArticles());
        Thread t2 = new Thread(new BackGroundNews());
        t1.start();
        t2.start();
    }

    class BackgroundArticles implements Runnable{

        @Override
        public void run() {
            while(true) {
                new FetchArticles().storeArticles();
                try{
                    Thread.sleep(1000*60*60*6);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class BackGroundNews implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    fetchNews();
                    Runtime rut = Runtime.getRuntime();
                    Process p = rut.exec(new String[]{"Rscript", "national.r"});
                    p.waitFor();
                    preprocessKeywords();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    Thread.sleep(1000*60*10);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void fetchNews() throws Exception{
        new FetchRssFeeds().fetchAllRSS(7,"news1.txt");
    }

    private void preprocessKeywords() throws Exception{
        HashSet<String>trendSet = new HashSet<>();
        HashSet<String> doubleWordSet = new HashSet<>();
        HashSet<String> tripleWordSet = new HashSet<>();
        HashSet<String> temp1 = new HashSet<>();
        HashSet<String> temp2 = new HashSet<>();
        HashSet<String> quadWordSet = new HashSet<>();

        FileReader fr = new FileReader("trendsNational.txt");
        BufferedReader reader = new BufferedReader(fr);
        String trend;
        while((trend=reader.readLine())!=null) {
            String words[] = trend.split(" ");
            if(words.length==1){
                if(isStopWord(trend)||isVerb(trend));
                else if(isInteger(trend));
                else trendSet.add(trend);
            }
            else if(words.length==2)
                doubleWordSet.add(trend);
            else if(words.length==3)
                tripleWordSet.add(trend);
            else quadWordSet.add(trend);
        }

        for(String s:doubleWordSet){
            String[] words = s.split(" ");
            trendSet.remove(words[1]);
            trendSet.remove(words[0]);
        }

        for(String s:quadWordSet){
            String words[] = s.split(" ");
            String s1 = words[0]+" "+words[1]+" "+words[2];
            String s2 = words[1]+" "+words[2]+" "+words[3];
            if(isStopWord(words[0])||isVerb(words[0])||isStopWord(words[3])||isVerb(words[3]));
            else if(isInteger(words[0])||isInteger(words[1])||isInteger(words[2])||isInteger(words[3]));
            else if(tripleWordSet.contains(s1)&&tripleWordSet.contains(s2)){
                temp1.add(s1);
                temp1.add(s2);
                trendSet.add(s);
            }
            else if(tripleWordSet.contains(s1)||tripleWordSet.contains(s2));
            else trendSet.add(s);
        }

        for(String s:tripleWordSet){
            String words[] = s.split(" ");
            String s1 = words[0]+" "+words[1];
            String s2 = words[1]+" "+words[2];
            if(isStopWord(words[0])||isVerb(words[0])||isStopWord(words[2])||isVerb(words[2]));
            else if(isInteger(words[0])||isInteger(words[1])||isInteger(words[2]));
            else if(doubleWordSet.contains(s1)&&doubleWordSet.contains(s2)){
                temp2.add(s1);
                temp2.add(s2);
                trendSet.add(s);
            }
            else if(doubleWordSet.contains(s1)||doubleWordSet.contains(s2));
            else trendSet.add(s);
        }

        for(String s:temp2) {
            doubleWordSet.remove(s);
        }

        for(String s:temp1) {
            tripleWordSet.remove(s);
            trendSet.remove(s);
        }

        for(String s:doubleWordSet){
            String words[] = s.split(" ");
            if(isStopWord(words[0])||isVerb(words[0])||isStopWord(words[1])||isVerb(words[1]));
            else if(isInteger(words[0])||isInteger(words[1]));
            else trendSet.add(s);
        }

        trendSet.remove("latest national news");
        trendSet.remove("political");
        trendSet.remove("india");
        trendSet.remove("ministers");
        trendSet.remove("minister");
        trendSet.remove("live updates");
        trendSet.remove("government");

        DatabaseHandler.writeToNational(trendSet);
    }

    private boolean isStopWord(String word){
        return stopWordsSet.contains(word);
    }

    private boolean isVerb(String word){
        return verbSet.contains(word);
    }

    private boolean isInteger(String s){
        try{
            int i = Integer.parseInt(s);
        }catch (NumberFormatException e){
            return  false;
        }
        return true;
    }

}
