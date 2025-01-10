package com.example.rightside;

import java.util.ArrayList;
import java.util.Date;

public class Report {
    int id;
    int userId;
    int adminId;
    String name;
    String discriminationType;
    String location;
    Date date;
    String description;
    public String phoneNumber;
    public String email;
    public String witness;
    public String extraInfo;
    String personInvolved;
    String injury;
    boolean isAnonymous;
    ArrayList<String> impacts = new ArrayList<>();
    ArrayList<String> actions = new ArrayList<>();

    public Report(int id, String name, int userId, int adminId, String discriminationType, String location, Date date, String description, String phoneNumber, String email, String witness, String extraInfo, String personInvolved, String injury, boolean isAnonymous, ArrayList<String> impacts, ArrayList<String> actions) {
        this.id = id;
        this.discriminationType = discriminationType;
        this.location = location;
        this.date = date;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.witness = witness;
        this.extraInfo = extraInfo;
        this.personInvolved = personInvolved;
        this.injury = injury;
        this.isAnonymous = isAnonymous;
        this.impacts = impacts;
        this.actions = actions;
        this.userId = userId;
        this.adminId = adminId;
        this.name = name;
    }
}
