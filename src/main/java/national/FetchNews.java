package national;

import trends.Utilities.FetchRssFeeds;

public class FetchNews {

    public static void main(String args[]){
        try {
            new FetchNews().fetchNews();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void fetchNews() throws Exception{
        new FetchRssFeeds().fetchAllRSS(1);
    }
}
