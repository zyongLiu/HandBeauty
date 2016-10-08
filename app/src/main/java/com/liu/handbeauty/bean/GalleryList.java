package com.liu.handbeauty.bean;

import java.util.List;

/**
 * Created by Liu on 2016-05-25.
 */
public class GalleryList {
    private boolean status;
    private int total;
    private List<Gallery> tngou;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Gallery> getTngou() {
        return tngou;
    }

    public void setTngou(List<Gallery> tngou) {
        this.tngou = tngou;
    }
}
