package com.example.rightside;

public class Request {
    String title;
    String desiredOutcome;
    String status;
    int id;

    public Request(String title, String desiredOutcome, String status, int id) {
        this.title = title;
        this.desiredOutcome = desiredOutcome;
        this.status = status;
        this.id = id;
    }
}
