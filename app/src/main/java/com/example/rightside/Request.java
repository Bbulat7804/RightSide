package com.example.rightside;

import android.widget.Spinner;

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



    public Request(String reason, String desiredOutcome, String method, String urgency, String date, String time, String description, String status, int adminId, int userId, int requestId, String type) {
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
    }
}

