package com.liu.handbeauty.bean;

import java.util.List;

/**
 * Created by Liu on 2016-05-25.
 */
public class Classify {
    private String status;
    private List<Galleryclass> tngou;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Galleryclass> getTngou() {
        return tngou;
    }

    public void setTngou(List<Galleryclass> tngou) {
        this.tngou = tngou;
    }
}
