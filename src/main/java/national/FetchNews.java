package national;

import Utilities.DatabaseHandler;
import Utilities.FetchRssFeeds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class FetchNews {

    HashSet<String> stopWordsSet;
    HashSet<String> verbSet;
    Vector<String> news;
    HashSet<String> trendSet;
    HashMap<String,Double> weights;

    public FetchNews(){
        String stopwords[] = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ahead","ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "amid", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asks", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "visit", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
        String commonVerbs[] = {"ask","killed","today","arrest","arrested","improves","improve","dismisses","dismiss","asks","alleged","offers","walks","attend","offer","alleges","declare","declares","declared","strikes","strike","visit","say","says","meet","meets","announce","welcomes","welcome","planned","plans","seeks","seek","hear","hears","appear","appears"};
        stopWordsSet = new HashSet<>(Arrays.asList(stopwords));
        verbSet = new HashSet<>(Arrays.asList(commonVerbs));
    }

    public static void main(String args[]){
        try {
            FetchNews fetchNews = new FetchNews();
            fetchNews.fetchNews();
            System.out.println("LEHAR");
            Runtime rut = Runtime.getRuntime();
            Process p = rut.exec(new String[]{"Rscript","national.r"});
            p.waitFor();
            System.out.println("JAIN");
            fetchNews.preprocessKeywords();
            fetchNews.clubKeywords();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchNews() throws Exception{
        news = new FetchRssFeeds().fetchAllRSS(7);
    }

    public void preprocessKeywords() throws Exception{
        trendSet = new HashSet<>();
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

    private void clubKeywords() throws Exception{

        weights = new HashMap<>();
        FileReader fr = new FileReader("weights.txt");
        BufferedReader reader = new BufferedReader(fr);
        String line;
        while((line=reader.readLine())!=null){
            String split[] = line.split(" ");
            weights.put(split[0],Double.parseDouble(split[1]));
        }

        for(String s:stopWordsSet)
            weights.put(s,0.0);

        HashMap<String,ArrayList<String>> map = new HashMap<>();
        for(String title:news){
            StringBuilder sb = new StringBuilder();
            int i=0;
            for(String trend:trendSet){
                if(intersects(title,trend)) {
                    sb.append(trend + ", ");
                    i++;
                }
            }
            if(i>=2){
                if(map.get(sb.toString())==null){
                    ArrayList<String> titles = new ArrayList<>();
                    titles.add(title);
                    map.put(sb.toString(),titles);
                }
                else{
                    ArrayList<String> titles = map.get(sb.toString());
                    titles.add(title);
                    map.put(sb.toString(),titles);
                }
            }
        }
        for(String s: map.keySet()) {
            System.out.println(s);
            for(String title:map.get(s))
                System.out.println(title);
            System.out.println();
            System.out.println();
        }
    }

    private boolean intersects(String a,String b){
        Double weight = 0.0;
        int i=0;
        a = a.toLowerCase();
        a = a.replaceAll("[^A-Za-z ]","");
        List<String> aList = Arrays.asList(a.split(" "));
        List<String> bList = Arrays.asList(b.split(" "));
        for(String word:bList) {
            if (aList.contains(word)) {
                weight = weight + (weights.get(word) == null ? 0.0 : weights.get(word));
                i++;
            }
        }
        if(bList.size()<=2&&i==bList.size())
            if(weight>0.05) return true;
        else if(i>=2)
            if(weight>0.05) return true;
        return false;
    }
}
