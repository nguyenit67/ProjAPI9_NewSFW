package com.example.test;

public class Noti {
    private String photo_url;
    private String title;
    private String description;
    private String pubdate;
    private String content_url;

    public Noti(String photo_url, String title, String description, String pubdate, String content_url) {
        this.photo_url = photo_url;
        this.title = title;
        this.description = description;
        this.pubdate = pubdate;
        this.content_url = content_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getContent_url() {
        return content_url;
    }
}
