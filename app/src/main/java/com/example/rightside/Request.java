package com.example.rightside;

import android.widget.Spinner;

import java.util.ArrayList;

public class Request {
    String reason;
    String desiredOutcome;
    String method;
    String urgency;
    String date;
    String time;
    String description;
    String status;
    String type;
    int adminId;
    int userId;
    int requestId;
    ArrayList<String> attachmentPaths = new ArrayList<>();


    public Request(String reason, String desiredOutcome, String method, String urgency, String date, String time, String description, String status, int adminId, int userId, int requestId, String type, ArrayList<String> attachmentPaths) {
        this.reason = reason;
        this.desiredOutcome = desiredOutcome;
        this.type = type;
        this.urgency = urgency;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
        this.adminId = adminId;
        this.userId = userId;
        this.requestId = requestId;
        this.method = method;
        this.attachmentPaths = attachmentPaths;
    }
}

