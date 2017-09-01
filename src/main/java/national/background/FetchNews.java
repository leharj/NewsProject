package national.background;

import Models.Article;
import Utilities.DatabaseHandler;
import Utilities.FetchArticles;
import Utilities.FetchRssFeeds;
import Models.NewsDisplay;
import Utilities.NewsEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchNews {

    HashSet<String> stopWordsSet;
    HashSet<String> verbSet;
    Vector<String> news;
    HashSet<String> trendSet;
    HashMap<String,Double> weights;
    HashSet<String> locationSet;
    ArrayList<Integer>chartParams;

    public FetchNews(){
        String loc[] = { "Andaman and Nicobar","Andaman","Nicobar","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chhattisgarh","Dadra and Nagar Haveli","Daman and Diu","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jammu","Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Puducherry","Pondicherry", "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal","Mumbai","Delhi","Chennai","Bangalore","Hyderabad","Ahmedabad","Kolkata","Surat","Pune","Jaipur","Cochin","Lucknow","Kanpur","Nagpur","Indore","Thane","Bhopal","Visakhapatnam","Patna","Vadodara","Ghaziabad","Ludhiana","Agra","Nashik","Faridabad","Meerut","Rajkot","Solapur","Varanasi","Srinagar","Aurangabad","Dhanbad","Amritsar", "Navi Mumbai","Bengaluru","Gurugram","Gurgaon","Noida"};
        String stopwords[] = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ahead","ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "amid", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asks", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "visit", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
        String commonVerbs[] = {"ask","killed","today","arrest","arrested","improves","improve","dismisses","dismiss","asks","alleged","offers","walks","attend","offer","alleges","declare","declares","declared","strikes","strike","visit","say","says","meet","meets","announce","welcomes","welcome","planned","plans","seeks","seek","hear","hears","appear","appears"};
        stopWordsSet = new HashSet<>(Arrays.asList(stopwords));
        verbSet = new HashSet<>(Arrays.asList(commonVerbs));
        locationSet = new HashSet<>(Arrays.asList(loc));
        chartParams = new ArrayList<>();
    }

    public String nationalNews() throws Exception{
        fetchNews();
        new FetchArticles().storeArticles();
        Runtime rut = Runtime.getRuntime();
        Process p = rut.exec(new String[]{"Rscript","national.r"});
        p.waitFor();
        preprocessKeywords();
        String s = getClubNews();
        return s;
    }

    private void fetchNews() throws Exception{
        news = new FetchRssFeeds().fetchAllRSS(7,"news1.txt");
    }

    private void preprocessKeywords() throws Exception{
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

    private String getClubNews() throws Exception{
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
        HashMap<String,ArrayList<String>> singleNewsMap = new HashMap<>();
        HashSet<Article> articles = DatabaseHandler.getArticles();

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
                String s = sb.toString();
                s = s.substring(0,s.length()-2);
                if(map.get(s)==null){
                    ArrayList<String> titles = new ArrayList<>();
                    titles.add(title);
                    map.put(s.toString(),titles);
                }
                else{
                    ArrayList<String> titles = map.get(s);
                    titles.add(title);
                    map.put(s,titles);
                }
            }
            else if(i==1){
                String s = sb.toString();
                s = s.substring(0,s.length()-2);
                if(singleNewsMap.get(s)==null){
                    ArrayList<String> titles = new ArrayList<>();
                    titles.add(title);
                    singleNewsMap.put(s,titles);
                }
                else{
                    ArrayList<String> titles = singleNewsMap.get(s);
                    titles.add(title);
                    singleNewsMap.put(s,titles);
                }
            }
        }

        HashSet<String> oneNews = new HashSet<>();

        for(String s:map.keySet()){
            if(map.get(s).size()==1)
                oneNews.add(s);
        }

        HashMap<String,List<String>> addOns  = new HashMap<>();

        for(String s:oneNews){
            List<String> list = new ArrayList<>(map.keySet());
            Iterator<String> iterator = list.iterator();

            while(iterator.hasNext()){
                String t = iterator.next();
                List<String> intersections = completeIntersection(s,t);
                if(intersections!=null){
                    try {
                        String newAdd = map.get(s).get(0);
                        ArrayList<String> titles = map.get(t);
                        titles.add(newAdd);
                        map.put(t, titles);
                        map.remove(s);
                        if (addOns.get(t) == null)
                            addOns.put(t, intersections);
                        else {
                            List<String> additions = addOns.get(t);
                            additions.addAll(intersections);
                            addOns.put(t, additions);
                        }
                        iterator.remove();
                    }catch (Exception e){}
                }
            }
        }

        for(String s:addOns.keySet()){
            List<String> additions = addOns.get(s);
            ArrayList<String> list = map.get(s);
            map.remove(s);
            for(String addition:additions)
                s = s+", "+addition;
            map.put(s,list);
        }

        List<Map.Entry<String,ArrayList<String>>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, ArrayList<String>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<String>> o1, Map.Entry<String, ArrayList<String>> o2) {
                return o2.getValue().size()-o1.getValue().size();
            }
        });

        Iterator<Map.Entry<String,ArrayList<String>>> iterator = list.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,ArrayList<String>> entry = iterator.next();
            if(entry.getValue().size()==1)
                iterator.remove();
        }

        List<Map.Entry<String,ArrayList<String>>> singleList = new LinkedList<>(singleNewsMap.entrySet());
        Collections.sort(singleList, new Comparator<Map.Entry<String, ArrayList<String>>>() {
            @Override
            public int compare(Map.Entry<String, ArrayList<String>> o1, Map.Entry<String, ArrayList<String>> o2) {
                return o2.getValue().size()-o1.getValue().size();
            }
        });

        for(int i=0;i<(Math.min(15,singleList.size()));i++)
            list.add(singleList.get(i));

        List<Map.Entry<String, NewsDisplay>> displayList = new LinkedList<>();

        for(int i=0;i<list.size();i++){
            displayList.add(new NewsEntry(list.get(i).getKey(),list.get(i).getValue()));
            String parts[] = list.get(i).getKey().split(", ");
            HashSet<Article> articleSet = new HashSet<>();
            HashMap<String,Integer> locationTracker = new HashMap<>();
            for(Article article:articles){
                boolean x = true;
                for(String part:parts)
                    x = x && article.getContent().toLowerCase().contains(part);
                if(x) {
                    displayList.get(i).getValue().add(article.getTitle());
                    articleSet.add(article);
                    displayList.get(i).getValue().incrementTime(article.getDate());
                }
            }
            for(Article article:articleSet){
                for(String location:locationSet){
                    int count = 0;
                    Pattern pattern = Pattern.compile(location,Pattern.CASE_INSENSITIVE);
                    Matcher m = pattern.matcher(article.getContent());
                    while(m.find())
                        count++;
                    if(count>0){
                        if(locationTracker.get(location)==null)
                            locationTracker.put(location,count);
                        else
                            locationTracker.put(location,locationTracker.get(location)+count);
                    }
                }
            }
            String loc = "India";
            int max = 0;
            for(String location:locationTracker.keySet()){
                if(locationTracker.get(location)>max){
                    max = locationTracker.get(location);
                    loc = location;
                }
            }
            displayList.get(i).getValue().setLocation(loc);
            chartParams.addAll(displayList.get(i).getValue().getTimes());
        }

        StringBuilder sb = new StringBuilder("<div class=\"container col-md-6\">\n");

        int size = list.size();

        for(int i=0;i<(size+1)/2;i++)
            listBuilder(sb,displayList.get(2*i),2*i);

        sb.append("</div>\n");
        String str = "<div class=\"container col-md-6\">\n";
        sb.append(str);

        for(int i=0;i<size/2;i++)
            listBuilder(sb,displayList.get(2*i+1),2*i+1);

        sb.append("</div>\n");
        return sb.toString();
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

    private List<String> completeIntersection(String a,String b){
        boolean x = true;
        if(a.equals(b)) return null;
        List<String> aList = Arrays.asList(a.split(", "));
        List<String> bList = Arrays.asList(b.split(", "));
        Set<String> aSet = new HashSet<>(aList);
        for(String trend:bList) {
            x = x && aSet.contains(trend);
            if(x) aSet.remove(trend);
        }
        if(x) return new ArrayList<>(aSet);
        return null;
    }


    private void listBuilder(StringBuilder sb,Map.Entry<String,NewsDisplay> mapEntry,int i) {
        String str;
        String location = mapEntry.getValue().getLocation();
        str = "<button class = \"accordion\" onClick=\"initMap.call(this,'"+location+"','map"+(i+1)+"')\"><p class=\"alignleft\"><em>"
                + mapEntry.getKey() + "</em></p>" + "<p class = \" alignright \">" + location + "</p></button>\n"
                + "<div class=\"panel\">\n"
                + "<ul class=\"list-group\">\n";

        sb.append(str);

        for (String relatedNews : mapEntry.getValue().getRelatedNews())
            sb.append("     <li class=\"list-group-item\">" + relatedNews + "</li>\n");
        sb.append(" </ul>\n");
        sb.append("<div id = \"chartContainer"+(i+1)+"\" style = \"height: 300px; width: 100%;\">\n");
        sb.append("</div>\n");
        sb.append("<div id = \"map"+(i+1)+"\" style = \"height: 300px; width: 100%;\">\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
    }

    public ArrayList<Integer> getChartParams(){
        return chartParams;
    }

}