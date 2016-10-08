package com.liu.handbeauty.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Liu on 2016-06-10.
 */
public class Pictures implements Parcelable{

    /**
     * count : 4001
     * fcount : 0
     * galleryclass : 1
     * id : 717
     * img : /ext/160408/1d3a87c0a012024db834eeba9f28d891.jpg
     * list : [{"gallery":717,"id":12425,"src":"/ext/160408/1d3a87c0a012024db834eeba9f28d891.jpg"},{"gallery":717,"id":12426,"src":"/ext/160408/765f3b493b160e7a3c9dfc3271c2e7e1.jpg"},{"gallery":717,"id":12427,"src":"/ext/160408/afc5412963ccd6ddd5de4bc94501c16c.jpg"},{"gallery":717,"id":12428,"src":"/ext/160408/7251e01b49f03a4d620f86ba4582be20.jpg"},{"gallery":717,"id":12429,"src":"/ext/160408/0e38818006747241c4d50f849cce3cab.jpg"},{"gallery":717,"id":12430,"src":"/ext/160408/3538a8cf5b23c7372bf6c8d74202c042.jpg"},{"gallery":717,"id":12431,"src":"/ext/160408/0c110518ab795a977cedc5e09e307bfb.jpg"},{"gallery":717,"id":12432,"src":"/ext/160408/75567432ffa42f8900f79f848e0de85c.jpg"},{"gallery":717,"id":12433,"src":"/ext/160408/cb901ee8c1d2fd5c48f4f13504c4a1c1.jpg"}]
     * rcount : 0
     * size : 9
     * status : true
     * time : 1460114220000
     * title : 清纯美女明星李七喜
     * url : http://www.tngou.net/tnfs/show/717
     */

    private int count;
    private int fcount;
    private int galleryclass;
    private int id;
    private String img;
    private int rcount;
    private int size;
    private boolean status;
    private long time;
    private String title;
    private String url;
    /**
     * gallery : 717
     * id : 12425
     * src : /ext/160408/1d3a87c0a012024db834eeba9f28d891.jpg
     */

    private List<Picture> list;

    protected Pictures(Parcel in) {
        count = in.readInt();
        fcount = in.readInt();
        galleryclass = in.readInt();
        id = in.readInt();
        img = in.readString();
        rcount = in.readInt();
        size = in.readInt();
        status = in.readByte() != 0;
        time = in.readLong();
        title = in.readString();
        url = in.readString();
        list = in.createTypedArrayList(Picture.CREATOR);
    }

    public static final Creator<Pictures> CREATOR = new Creator<Pictures>() {
        @Override
        public Pictures createFromParcel(Parcel in) {
            return new Pictures(in);
        }

        @Override
        public Pictures[] newArray(int size) {
            return new Pictures[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getGalleryclass() {
        return galleryclass;
    }

    public void setGalleryclass(int galleryclass) {
        this.galleryclass = galleryclass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Picture> getList() {
        return list;
    }

    public void setList(List<Picture> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(count);
        dest.writeInt(fcount);
        dest.writeInt(galleryclass);
        dest.writeInt(id);
        dest.writeString(img);
        dest.writeInt(rcount);
        dest.writeInt(size);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeLong(time);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeTypedList(list);
    }
}
