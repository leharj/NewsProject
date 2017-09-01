package trends.background;

import Utilities.DatabaseHandler;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static constants.Constants.stopWordsSet;

public class KeywordsSort {

    public static HashSet<String> trendWords = new HashSet<String>();
    public static void finalSort() throws Exception{

        HashSet<String> trendSet = new HashSet<String>();
        HashSet<String> tripletTrend = new HashSet<String>();
        HashSet<String> quadTrend = new HashSet<String>();
        for(String trend:trendWords){
            trend = trend.replace("’s","");
            trend = trend.replace("’","");
            String split[] = trend.split(" ");
            if(split.length==1)
                trendSet.add(trend);
            else if(split.length==2){
                if(isStopWord(split[1])||isStopWord(split[0]));
                else if(trendSet.contains(split[0])||trendSet.contains(split[1])){
                    trendSet.remove(split[0]);
                    trendSet.remove(split[1]);
                    trendSet.add(trend);
                }
                else trendSet.add(trend);
            }
            else if(split.length==3){
                if(isStopWord(split[0])||isStopWord(split[2]));
                else tripletTrend.add(trend);
            }
            else{
                if(isStopWord(split[0])||isStopWord(split[3]));
                else quadTrend.add(trend);
            }
        }
        for(String s:tripletTrend){
            String words[] = s.split(" ");
            String s1 = words[0]+" "+words[1];
            String s2 = words[1]+" "+words[2];
            if(trendSet.contains(s1)) trendSet.remove(s1);
            if(trendSet.contains(s2)) trendSet.remove(s2);
            trendSet.add(s);
        }

        for(String s:quadTrend){
            String words[] = s.split(" ");
            String s1 = words[0]+" "+words[1]+" "+words[2];
            String s2 = words[1]+" "+words[2]+" "+words[3];
            if(trendSet.contains(s1)) trendSet.remove(s1);
            if(trendSet.contains(s2)) trendSet.remove(s2);
            trendSet.add(s);
        }

        removeWords(trendSet,"august 2017");
        removeWords(trendSet,"august");
        removeWords(trendSet,"see photo");
        removeWords(trendSet,"see photos");
        removeWords(trendSet,"world");
        removeWords(trendSet,"launch");
        removeWords(trendSet,"actor");
        removeWords(trendSet,"watch video");
        removeWords(trendSet,"video");
        removeWords(trendSet,"india");
        removeWords(trendSet,"match");
        removeWords(trendSet,"national news");
        removeWords(trendSet,"movie review");
        removeWords(trendSet,"happy birthday");
        removeWords(trendSet,"latest entertainment news");
        removeWords(trendSet,"latest national news");
        removeWords(trendSet,"latest sports news");
        removeWords(trendSet,"study");
        DatabaseHandler.writeKeywords(trendSet);


    }

    public static void independentSort() throws Exception {
        BufferedReader reader = null;
        FileReader fr = null;
        HashMap<String,Integer> trendsMap= new HashMap<String, Integer>();
        fr = new FileReader("trends.txt");
        reader = new BufferedReader(fr);
        String trend;
        while((trend=reader.readLine())!=null){
                if(trendsMap.get(trend)==null)
                    trendsMap.put(trend,1);
                else trendsMap.put(trend,2);
        }
        fr.close();
        reader.close();
        for(String s:trendsMap.keySet()){
            if(trendsMap.get(s)==2)
                trendWords.add(s);
        }
    }

    private static void removeWords(HashSet<String> keywords,String word){
        if(keywords.contains(word))
            keywords.remove(word);
    }

    private static boolean isStopWord(String word){
        return stopWordsSet.contains(word);
    }
}
