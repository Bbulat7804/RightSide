package com.example.rightside;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.LinkedList;

public class Manager {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static Fragment userHomePage = new UserHomePage();
    public static Fragment adminHomePage = new AdminHomePage();
    public static Fragment userProfilePage = new UserProfilePage();
    public static Fragment adminProfilePage = new AdminProfilePage();
    public static Fragment faqPage = new FAQPage();
    public static Fragment eventsPage = new EventsPage();
    public static Fragment dataInsigtsPage = new DataInsightsPage();
    public static Fragment userRequestPage = new DisplayRequestPage();
    public static Fragment legalConsultationForm = new LegalConsultationForm();
    public static Fragment mentalConsultationForm = new MentalConsultationForm();
    public static Fragment articlePage = new DiscoverPage();
    

    public static String userType;
    public static LinkedList<Fragment> stack = new LinkedList();
    public static void goToPage(Fragment fragment, FragmentManager fm){
        int containerId = R.id.fragment_container;
        fm.beginTransaction().replace(containerId,fragment).commit();
        stack.addFirst(fragment);
    }
}
