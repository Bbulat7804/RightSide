package com.example.rightside;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

public class Manager {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String USERLIBRARY = "Users";
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
    public static Fragment articlePage = new ArticleDiscoverPage();
    public static Fragment articleAdminPage = new ArticleDiscoverAdminPage();
    public static Fragment uploadArticlePage = new UploadArticlePage();
    public static Fragment legalDocTemplate = new LegalDocTemplate();
    public static Fragment adminRequestPage = new ManageRequestPage();
    public static Fragment manageLegalDocTemplate = new ManageLegalDocTemplate();
    public static Fragment viewRequestPage = new ViewRequestPage();
    public static Fragment viewRequestAdminPage = new ViewRequestAdminPage();
    public static Fragment contactAdminPage = new ContactAdminPage();
    public static Fragment contactUserPage = new ContactUserPage();
    public static Fragment modifyLegalConsultationPage = new ModifyLegalConsultation();
    public static Fragment modifyMentalConsultationPage = new ModifyMentalConsultation();
    public static Fragment groupChatPage = new GroupChatPage();
    public static Fragment stressTestPage = new StressTestPage();
    public static Fragment anonymousSupportGroupPage = new AnonymousSupportGroupPage();
    public static Fragment anonymousSupportGroupAdminPage = new AnonymousSupportGroupAdminPage();
    public static Fragment createGroupPage = new CreateSupportGroupPage();
    public static Fragment blankPage = new BlankPage();
    public static String userType;
    public static User currentUser;
    public static Admin currentAdmin;
    public static DatabaseConnection db = new DatabaseConnection();
    public static int latestRequestIndex = 0;
    public static int latestArticleIndex = 0;
    public static int latestSupportGroupIndex = 0;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_GROUP_ICON_REQUEST = 10;
    public static ArrayList<Request> requests = new ArrayList<>();
    public static ArrayList<SupportGroup> supportGroups = new ArrayList<>();
    public static ArrayList<Article> articles = new ArrayList<>();
    public static LinkedList<Fragment> stack = new LinkedList();


    public static void goToPage(Fragment fragment, FragmentManager fm){
        int containerId = R.id.fragment_container;
        fm.beginTransaction().replace(containerId,fragment).commit();
        stack.addFirst(fragment);
    }

    public static void goToSiblingPage(Fragment fragment, FragmentManager fm){
        int containerId = R.id.fragment_container;
        fm.beginTransaction().replace(containerId,fragment).commit();
    }
}
