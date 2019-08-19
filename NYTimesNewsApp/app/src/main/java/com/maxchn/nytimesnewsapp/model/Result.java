package com.maxchn.nytimesnewsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    private Integer resultId;
    private MostPopularType type;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("published_date")
    @Expose
    private String publishedDate;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("updated")
    @Expose
    private String updated;

    public Result() {
    }

    private Result(Parcel source) {
        url = source.readString();
        title = source.readString();
        publishedDate = source.readString();
        updated = source.readString();
        this.source = source.readString();
    }

    public Integer getId() {
        return resultId;
    }

    public void setId(Integer id) {
        this.resultId = id;
    }

    public MostPopularType getType() {
        return type;
    }

    public void setType(MostPopularType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(publishedDate);
        dest.writeString(updated);
        dest.writeString(source);
    }
}