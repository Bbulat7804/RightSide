package com.example.rightside;

import java.util.ArrayList;

public class SupportGroup {
    String name;
    String description;
    int id;
    String iconUrl;
    ArrayList<Integer> participantId;

    public SupportGroup(String name, String description, int id, String iconUrl, ArrayList<Integer> participantId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.iconUrl = iconUrl;
        this.participantId = participantId;
    }
}
