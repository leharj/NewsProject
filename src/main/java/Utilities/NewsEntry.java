package Utilities;

import Models.NewsDisplay;
import Models.NewsItem;

import java.util.ArrayList;
import java.util.Map;

public class NewsEntry implements Map.Entry<String, NewsDisplay> {

    String key;
    NewsDisplay value;

    public NewsEntry(String key,ArrayList<String> news){
        this.key = key;
        value = new NewsDisplay(news);

    }
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public NewsDisplay getValue() {
        return value;
    }

    @Override
    public NewsDisplay setValue(NewsDisplay value) {
        NewsDisplay old = this.value;
        this.value = value;
        return old;
    }
}
