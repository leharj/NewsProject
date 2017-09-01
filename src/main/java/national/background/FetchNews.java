package national.background;

import Models.Article;
import Utilities.DatabaseHandler;
import Models.NewsDisplay;
import Utilities.NewsEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.Constants.stopWordsSet;

public class FetchNews {

    HashMap<String,Double> weights;
    HashSet<String> locationSet;
    ArrayList<Integer>chartParams;

    public FetchNews(){
        String loc[] = { "andaman and nicobar","andaman","nicobar","andhra pradesh","arunachal pradesh","assam","bihar","chandigarh","chhattisgarh","dadra and nagar haveli","daman and diu","goa","gujarat","haryana","himachal pradesh","jammu and kashmir","jammu","kashmir","jharkhand","karnataka","kerala","lakshadweep","madhya pradesh","maharashtra","manipur","meghalaya","mizoram","nagaland","odisha","puducherry","pondicherry", "punjab","rajasthan","sikkim","tamil nadu","telangana","tripura","uttar pradesh","uttarakhand","west bengal","mumbai","delhi","chennai","bangalore","hyderabad","ahmedabad","kolkata","surat","pune","jaipur","cochin","lucknow","kanpur","nagpur","indore","bhopal","visakhapatnam","patna","vadodara","ghaziabad","ludhiana","agra","nasik","faridabad","meerut","rajkot","solapur","varanasi","srinagar","aurangabad","dhanbad","amritsar", "bengaluru","gurugram","gurgaon","noida","shimla","gorakhpur","darjeeling"};
        locationSet = new HashSet<>(Arrays.asList(loc));
        chartParams = new ArrayList<>();
    }

    public String nationalNews() throws Exception{
        String s = getClubNews();
        return s;
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
        reader.close();
        fr.close();

        for(String s:stopWordsSet)
            weights.put(s,0.0);

        Vector<String> news = new Vector<>();

        fr = new FileReader("news1.txt");
        reader = new BufferedReader(fr);

        while((line=reader.readLine())!=null)
            news.add(line);

        HashMap<String,ArrayList<String>> map = new HashMap<>();
        HashMap<String,ArrayList<String>> singleNewsMap = new HashMap<>();
        HashSet<Article> articles = DatabaseHandler.getArticles();
        HashSet<String> trendSet = DatabaseHandler.getNationalTrends();

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

        ArrayList<String> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            String[] words = list.get(i).getKey().split(", ");
            Reducer reducer[] = new Reducer[words.length];
            for(int j=0;j<reducer.length;j++)
                reducer[j] = new Reducer(words[j]);
            for(int j=0;j<reducer.length-1;j++){
                if(reducer[j].flag){
                    for(int k=j+1;k<reducer.length;k++){
                        similar(reducer[j],reducer[k]);
                    }
                }
            }
            String s = "";
            for(int j=0;j<words.length;j++) {
                if (reducer[j].flag) {
                    s = s+words[j]+", ";
                }
            }
            s = s.substring(0,s.length()-2);
            list1.add(s);
        }

        List<Map.Entry<String, NewsDisplay>> displayList = new LinkedList<>();

        for(int i=0;i<list.size();i++){
            displayList.add(new NewsEntry(list1.get(i),list.get(i).getValue()));
            String parts[] = list.get(i).getKey().split(", ");
            HashSet<Article> articleSet = new HashSet<>();
            HashSet<String> titleSet = new HashSet<>();
            HashMap<String,Integer> locationTracker = new HashMap<>();
            for(Article article:articles){
                boolean x = true;
                for(String part:parts) {
                    String words[] = part.split(" ");
                    String temp = part;
                    if(words.length>=2)
                        temp = words[0]+" "+words[1];
                    x = x && article.getContent().toLowerCase().contains(temp);
                }
                if(x) {
                    displayList.get(i).getValue().add(article.getTitle());
                    articleSet.add(article);
                    displayList.get(i).getValue().incrementTime(article.getDate());
                }
            }
            titleSet = new HashSet<>(list.get(i).getValue());
            for(String location:locationSet){
                int count = 0;
                for(Article article:articleSet) {
                    Pattern pattern = Pattern.compile(location, Pattern.CASE_INSENSITIVE);
                    Matcher m = pattern.matcher(article.getContent());
                    while (m.find())
                        count++;
                    if (count > 0) {
                        if (locationTracker.get(location) == null)
                            locationTracker.put(location, count);
                        else
                            locationTracker.put(location, locationTracker.get(location) + count);
                    }
                }
                for(String title :titleSet) {
                    Pattern pattern = Pattern.compile(location, Pattern.CASE_INSENSITIVE);
                    Matcher m = pattern.matcher(title);
                    while (m.find())
                        count++;
                    if (count > 0) {
                        if (locationTracker.get(location) == null)
                            locationTracker.put(location, count);
                        else
                            locationTracker.put(location, locationTracker.get(location) + count);
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
                if(!stopWordsSet.contains(word))
                    i++;
            }
        }
        if(weight>=0.5) return true;
        if(bList.size()<=2&&i==bList.size())
            return weight>0.05;
        else if(i>=2)
             return weight>0.05;
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
                + mapEntry.getKey() + "</em></p>";
        if(!location.equals("India"))
            str = str+"<p class = \" alignright \">" + location + "</p>";

        str = str + "</button>\n"
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

    private boolean isLocation(String loc){
        return locationSet.contains(loc);
    }

    private void similar(Reducer a,Reducer b){
        int count = 0;
        List<String> aList = Arrays.asList(a.trend.split(" "));
        List<String> bList = Arrays.asList(b.trend.split(" "));
        for(String word:aList){
            if(!stopWordsSet.contains(word)&&bList.contains(word))
                count++;
        }
        if(count>=2) b.flag = false;
    }

    class Reducer{
        String trend;
        int n;
        boolean flag;
        Reducer(String x){
            trend = x;
            n = trend.split(" ").length;
            flag = true;
        }
    }

}