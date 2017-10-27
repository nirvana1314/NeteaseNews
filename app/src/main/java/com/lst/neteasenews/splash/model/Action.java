package com.lst.neteasenews.splash.model;

import java.io.Serializable;

/**
 * Created by lisongtao on 2017/10/27.
 */

public class Action implements Serializable{
    String link_url;

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    @Override
    public String toString() {
        return "Action{" +
                "link_url='" + link_url + '\'' +
                '}';
    }
}
