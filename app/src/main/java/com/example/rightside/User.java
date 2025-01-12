package com.example.rightside;

import static com.example.rightside.Manager.USERLIBRARY;
import static com.example.rightside.Manager.db;

public class User {
    String name;
    int userId = 0;
    String username;
    String email;
    int reportNo = 0;
    String stressLevel;
    int dailyQuizScore = -1;
    String phoneNo;
    int adminId = 0;
    String password;
    String profilePhotoUrl;
    int supportGroupNo;
    int stressScore;

    public User(String name, int userId, String username, String email, int reportNo, String stressLevel, int dailyQuizScore, String phoneNo, int adminId, String password, String profilePhotoUrl, int supportGroupNo, int stressScore) {
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.reportNo = reportNo;
        this.stressLevel = stressLevel;
        this.dailyQuizScore = dailyQuizScore;
        this.phoneNo = phoneNo;
        this.adminId = adminId;
        this.password = password;
        this.profilePhotoUrl = profilePhotoUrl;
        this.supportGroupNo = supportGroupNo;
        this.stressScore = stressScore;
    }


}
