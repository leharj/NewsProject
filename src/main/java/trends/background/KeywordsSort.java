package trends.background;

import Utilities.DatabaseHandler;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class KeywordsSort {

    public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "amid", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asks", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seeks","seeks", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "visit", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    public static HashSet<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
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
        System.out.println(trendSet.size());
        System.out.println(tripletTrend.size());
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
        System.out.println(trendSet.size());
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
        return stopWordSet.contains(word);
    }
}
