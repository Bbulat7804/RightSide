package com.example.rightside;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.dateFormat;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.latestReportIndex;
import static com.example.rightside.Manager.reports;
import static com.example.rightside.Manager.stringToDate;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDataSingleton {
    private static final UserDataSingleton instance = new UserDataSingleton();

    // Variables to store user data
    public String discriminationType;
    public String location;
    public String date;
    public String description; // For the "Describe More" section
    public String phoneNumber;
    public String email;
    public String witness;
    public String extraInfo;

    // Variables to store checkbox states for page 2
    public Boolean page2CheckBoxOnlyMe;
    public Boolean page2CheckBoxMeAndOthers;
    public Boolean page2CheckBoxPreferNotToDisclose;
    public Boolean page2CheckBoxInjured;
    public Boolean page2CheckBoxNotInjured;
    public Boolean page2CheckBoxAnonymitySubmission; // Added Anonymity Submission checkbox

    // Variables to store checkbox states for page 4
    public Boolean page4Impact1;
    public Boolean page4Impact2;
    public Boolean page4Impact3;
    public Boolean page4Impact4;
    public Boolean page4Action1;
    public Boolean page4Action2;
    public Boolean page4Action3;

    // Private constructor to prevent instantiation
    private UserDataSingleton() {}

    public static UserDataSingleton getInstance() {
        return instance;
    }

    // Reset method to clear all data if needed
    public void resetData() {
        // Reset Page 1 data
        discriminationType = null;

        // Reset Page 2 data
        location = null;
        date = null;
        description = null;
        page2CheckBoxOnlyMe = null;
        page2CheckBoxMeAndOthers = null;
        page2CheckBoxPreferNotToDisclose = null;
        page2CheckBoxInjured = null;
        page2CheckBoxNotInjured = null;
        page2CheckBoxAnonymitySubmission = null; // Reset Anonymity Submission checkbox

        // Reset Page 3 data
        phoneNumber = null;
        email = null;
        witness = null;
        extraInfo = null;

        // Reset Page 4 data
        page4Impact1 = null;
        page4Impact2 = null;
        page4Impact3 = null;
        page4Impact4 = null;
        page4Action1 = null;
        page4Action2 = null;
        page4Action3 = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void uploadReport(){
        String personInvolved = "";
        String injury = "";
        boolean isAnonymous = false;
        ArrayList<String> impacts = new ArrayList<>();
        ArrayList<String> actions = new ArrayList<>();

        if(page2CheckBoxOnlyMe)
            personInvolved = "Only me";
        else if(page2CheckBoxMeAndOthers)
            personInvolved = "Me and others too";
        else if(page2CheckBoxPreferNotToDisclose)
            personInvolved = "Prefer not to disclosed";

        if(page2CheckBoxInjured)
            injury = "Injured";
        else if(page2CheckBoxNotInjured)
            injury = "Not injured";

        if(page4Impact1)
            impacts.add("Emotional Distress or trauma");
        if(page4Impact2)
            impacts.add("Career or education-related harm");
        if(page4Impact3)
            impacts.add("Violation of rights or dignity");
        if(page4Impact4)
            impacts.add("Provide legal assistance to the victim(s)");

        if(page4Action1)
            actions.add("Issue a warning or take disciplinary action");
        if(page4Action2)
            actions.add("Raise awarness or educate");
        if(page4Action3)
            actions.add("Refer to meditation");

        Report report = new Report(currentUser.userId, 1, discriminationType, location, stringToDate(date), description, phoneNumber, email, witness, extraInfo, personInvolved, injury, isAnonymous, impacts, actions);
        reports.add(report);
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_id", Integer.toString(report.userId));
        map.put("admin_id", Integer.toString(report.adminId)); // Assuming it's an integer
        map.put("discrimination_type", report.discriminationType);
        map.put("location", report.location);
        map.put("date", dateFormat.format(report.date)); // Convert Date to String if required
        map.put("description", report.description);
        map.put("phone_number", report.phoneNumber);
        map.put("email", report.email);
        map.put("witness", report.witness);
        map.put("extra_info", report.extraInfo);
        map.put("person_involved", report.personInvolved);
        map.put("injury", report.injury);
        map.put("is_anonymous", report.isAnonymous); // Assuming it's a boolean
        map.put("impacts", report.impacts); // Adjust if it's a list or custom object
        map.put("actions", report.actions); // Adjust if it's a list or custom object;
        db.addDocument("Reports", map, Integer.toString(++latestReportIndex));
    }
}
