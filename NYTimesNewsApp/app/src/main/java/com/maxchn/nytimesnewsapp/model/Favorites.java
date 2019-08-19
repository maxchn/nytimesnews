package com.maxchn.nytimesnewsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorites implements Parcelable {

    private Integer id;
    private String title;
    private String source;
    private String published_date;
    private String updated;
    private String url;
    private String data;

    private Favorites(Parcel source) {
        id = source.readInt();
        title = source.readString();
        this.source = source.readString();
        published_date = source.readString();
        updated = source.readString();
        url = source.readString();
        data = source.readString();
    }

    public Favorites() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static final Creator<Favorites> CREATOR = new Creator<Favorites>() {
        @Override
        public Favorites createFromParcel(Parcel source) {
            return new Favorites(source);
        }

        @Override
        public Favorites[] newArray(int size) {
            return new Favorites[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(source);
        dest.writeString(published_date);
        dest.writeString(updated);
        dest.writeString(url);
        dest.writeString(data);
    }
}