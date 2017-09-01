package trends.background;

import Utilities.FetchRssFeeds;

import java.io.File;

public class Main {
    public static void main(String args[]) {
        Main main = new Main();
        main.init();
    }

    public void init() {
        Thread t1 = new Thread(new BackgroundAllKeywords());
        Thread t2 = new Thread(new BackgroundRss());
        t1.start();
        t2.start();
    }

    class BackgroundAllKeywords implements Runnable {

        @Override
        public void run() {
            while (true) {
                for (int i = 0; i < 7; i++) {
                    try {
                        File f = new File("trends.txt");
                        if (f.exists()) f.delete();
                        new FetchRssFeeds().fetchAllRSS(i);
                        Runtime rut = Runtime.getRuntime();
                        Process p = rut.exec(new String[]{"Rscript", "trends2.r"});
                        p.waitFor();
                        p = rut.exec(new String[]{"Rscript", "trends2.r"});
                        p.waitFor();
                        p = rut.exec(new String[]{"Rscript", "trends2.r"});
                        p.waitFor();
                        p = rut.exec(new String[]{"Rscript", "trends2.r"});
                        p.waitFor();
                        p = rut.exec(new String[]{"Rscript", "trends2.r"});
                        p.waitFor();
                        KeywordsSort.independentSort();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    System.out.println(KeywordsSort.trendWords.size());
                    KeywordsSort.finalSort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    Thread.sleep(1000*60*60*6);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    class BackgroundRss implements Runnable{

        @Override
        public void run() {
            while(true){
                try {
                    for (int i = 0; i < 7; i++)
                        new FetchRssFeeds().fetchRSS(i);
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    Thread.sleep(10*60*1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}