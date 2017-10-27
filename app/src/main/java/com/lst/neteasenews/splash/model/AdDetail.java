package com.lst.neteasenews.splash.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lisongtao on 2017/10/27.
 */

public class AdDetail implements Serializable{
    Action action_params;
    List<String> res_url;

    public Action getAction_params() {
        return action_params;
    }

    public void setAction_params(Action action_params) {
        this.action_params = action_params;
    }

    public List<String> getRes_url() {
        return res_url;
    }

    public void setRes_url(List<String> res_url) {
        this.res_url = res_url;
    }

    @Override
    public String toString() {
        return "AdDetail{" +
                "action_params=" + action_params +
                ", res_url=" + res_url +
                '}';
    }
}
