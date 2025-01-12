package com.example.rightside;

public class Admin extends User{
    int requestManaged = 0;
    public Admin(String name, int userId, String username, String email, int reportNo, String stressLevel, int dailyQuizScore, String phoneNo, int adminId, String password, String profilePhotoUrl, int support_group_no,int requestManaged, int stressScore) {
        super(name, userId, username, email, reportNo, stressLevel, dailyQuizScore, phoneNo, adminId, password, profilePhotoUrl, support_group_no, stressScore);
        this.requestManaged = requestManaged;
    }
}
