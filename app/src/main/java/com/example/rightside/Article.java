package com.example.rightside;

public class Article {
    int id;
    String url;
    String caption;
    String imageURL;
    String author;
    String date;

    String type;
    public Article(int id, String url, String caption, String imageURL, String author, String date, String type) {
        this.id = id;
        this.url = url;
        this.caption = caption;
        this.imageURL = imageURL;
        this.author = author;
        this.date = date;
        this.type = type;
    }
}
