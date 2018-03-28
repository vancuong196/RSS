package com.cuong.rss;

import java.io.Serializable;

/**
 * Created by Cuong on 3/28/2018.
 */

public class RSS implements Serializable {
    String id;
    String mTitle;
    String mLink;

    public RSS() {
    }

    public String getId() {
        return id;
    }

    public String getmTitle() {

        return mTitle;
    }

    public String getmLink() {
        return mLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public RSS(String id, String mTitle, String mLink) {
        this.id = id;
        this.mTitle = mTitle;

        this.mLink = mLink;
    }
}
