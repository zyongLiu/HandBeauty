package com.liu.handbeauty.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Liu on 2016-06-10.
 */
public class Picture implements Parcelable{

    /**
     * gallery : 717
     * id : 12425
     * src : /ext/160408/1d3a87c0a012024db834eeba9f28d891.jpg
     */

        private int gallery;
        private int id;
        private String src;

    protected Picture(Parcel in) {
        gallery = in.readInt();
        id = in.readInt();
        src = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public int getGallery() {
            return gallery;
        }

    public void setGallery(int gallery) {
            this.gallery = gallery;
        }

    public int getId() {
            return id;
        }

    public void setId(int id) {
            this.id = id;
        }

    public String getSrc() {
            return src;
        }

    public void setSrc(String src) {
            this.src = src;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(gallery);
        dest.writeInt(id);
        dest.writeString(src);
    }
}
