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
        String loc[] = { "Andaman and Nicobar","Andaman","Nicobar","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chhattisgarh","Dadra and Nagar Haveli","Daman and Diu","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jammu","Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Puducherry","Pondicherry", "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal","Mumbai","Delhi","Chennai","Bangalore","Hyderabad","Ahmedabad","Kolkata","Surat","Pune","Jaipur","Cochin","Lucknow","Kanpur","Nagpur","Indore","Thane","Bhopal","Visakhapatnam","Patna","Vadodara","Ghaziabad","Ludhiana","Agra","Nashik","Faridabad","Meerut","Rajkot","Solapur","Varanasi","Srinagar","Aurangabad","Dhanbad","Amritsar", "Navi Mumbai","Bengaluru","Gurugram","Gurgaon","Noida","Shimla","Gorakhpur"};
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
            HashSet<String> titleSet = new HashSet<>();
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
                //System.out.println(count+" "+location);
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