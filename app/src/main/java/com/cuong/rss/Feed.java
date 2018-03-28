package com.cuong.rss;

/**
 * Created by Cuong on 3/28/2018.
 */
public class Feed {

    public String title;
    public String link;
    public String description;
    public String time;
    public Feed(String title, String link, String description, String time) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.time =time;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImagelink() {
        return time;
    }
}