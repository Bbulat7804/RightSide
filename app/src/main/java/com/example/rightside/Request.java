package com.example.rightside;

public class Request {
    String title;
    String desiredOutcome;
    String status;

    public Request(String title, String desiredOutcome, String status) {
        this.title = title;
        this.desiredOutcome = desiredOutcome;
        this.status = status;
    }
}
