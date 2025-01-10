package com.example.rightside;

public class Event {
    int id;
     String url;
    String title;
    String description;
    String organizer;
    String imageURL;
    String date;

    //eventImage

    public Event(int id, String url, String title, String description, String imageURL, String organizer, String date) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.organizer = organizer;
        this.date = date;
    }
}
