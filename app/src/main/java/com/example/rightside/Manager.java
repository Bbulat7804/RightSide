package com.example.rightside;

import android.os.Build;

import androidx.annotation.RequiresApi;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

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
    public static Fragment uploadEventPage = new UploadEvent();
    public static Fragment quizIntroPage = new QuizIntroPage();
    public static Fragment quizQuestions = new QuizQuestions();
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
    public static Fragment stressAssessmentPage = new StressAssessmentPage();
    public static Fragment anonymousSupportGroupPage = new AnonymousSupportGroupPage();
    public static Fragment anonymousSupportGroupAdminPage = new AnonymousSupportGroupAdminPage();
    public static Fragment createGroupPage = new CreateSupportGroupPage();
    public static Fragment viewReportPage = new ViewReportPage();
    public static Fragment reportPage = new ReportPage();
    public static Fragment blankPage = new BlankPage();
    public static String userType;
    public static User currentUser;
    public static Admin currentAdmin;
    public static DatabaseConnection db = new DatabaseConnection();
    public static ArrayList<Event> events = new ArrayList<>();
    public static int latestRequestIndex = 0;
    public static int latestArticleIndex = 0;
    public static int latestSupportGroupIndex = 0;
    public static int latestReportIndex = 0;
    public static int latestEventIndex = 0;
    public static int dailyQuizScore = -1;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_GROUP_ICON_REQUEST = 10;
    public static final int PICK_IMAGE_EVENT_REQUEST = 1;

    public static ArrayList<Request> requests = new ArrayList<>();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
    public static ArrayList<SupportGroup> supportGroups = new ArrayList<>();
    public static ArrayList<Article> articles = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static LinkedList<Fragment> stack = new LinkedList();
    public static ArrayList<Report> reports = new ArrayList<>();


    public static void goToPage(Fragment fragment, FragmentManager fm){
        int containerId = R.id.fragment_container;
        fm.beginTransaction().replace(containerId,fragment).commit();
        stack.addFirst(fragment);
    }

    public static void goToSiblingPage(Fragment fragment, FragmentManager fm){
        int containerId = R.id.fragment_container;
        fm.beginTransaction().replace(containerId,fragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date stringToDate(String dateString){
        Date date = null;
        // Date format
        try {
            date = dateFormat.parse(dateString); // Convert string to Date
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isValidURL(String urlString){
        try {
            new URL(urlString).toURI(); // Converts to URI to ensure stricter validation
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}