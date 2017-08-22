package trends.background;

import trends.Utilities.FetchRssFeeds;

import java.io.File;

public class Main {
    public static void main(String args[]){
        for(int i=0;i<7;i++) {
            try {
                System.out.println("Lehar");
                File f = new File("trends.txt");
                if(f.exists()) f.delete();
                new FetchRssFeeds().fetchAllRSS(i);
                Runtime rut = Runtime.getRuntime();
                Process p = rut.exec(new String[]{"Rscript","trends2.r"});
                p.waitFor();
                p = rut.exec(new String[]{"Rscript","trends2.r"});
                p.waitFor();
                p = rut.exec(new String[]{"Rscript","trends2.r"});
                p.waitFor();
                p = rut.exec(new String[]{"Rscript","trends2.r"});
                p.waitFor();
                KeywordsSort.independentSort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println(KeywordsSort.trendWords.size());
            KeywordsSort.finalSort();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}