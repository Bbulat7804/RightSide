package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoginPage extends AppCompatActivity {
    boolean hide = true;
    private String email;
    private String password;
    private int selectedId;
    DatabaseConnection db = new DatabaseConnection();
    EditText emailInput;
    EditText passwordInput;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button goToSignUpButton = findViewById(R.id.GoToSignUpButton);
        RadioGroup loginOptionGroup = findViewById(R.id.LoginOptionGroup);
        emailInput = findViewById(R.id.SignUpEmailInput);
        passwordInput = findViewById(R.id.LoginPasswordInput);
        ImageButton HideButton = findViewById(R.id.HideButton);
        Button loginButton = findViewById(R.id.LoginButton);
        font = passwordInput.getTypeface();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                selectedId = loginOptionGroup.getCheckedRadioButtonId();
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                if(email.equals("") || password.equals("")) {
                    Toast.makeText(LoginPage.this, "Fill in all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedButton = findViewById(selectedId);
                if(selectedButton!=null)
                    validateLoginData(selectedButton.getText().toString().toUpperCase());
                else
                    Toast.makeText(LoginPage.this, "Choose a login type", Toast.LENGTH_SHORT).show();
            }
        });

        goToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this,SignUpPage.class);
                startActivity(intent);
            }
        });

        HideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide = hide==false;

                if(hide==false)
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput.setTypeface(font);

            }
        });
    }

    private void login(String loginType){

        //Check login type user/admin
        Manager.userType = loginType;
        //change page based on login type
        if(userType.equalsIgnoreCase(USER)){
            Intent intent = new Intent(LoginPage.this, UserMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(LoginPage.this, AdminMainActivity.class);
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validateLoginData(String loginType){
        currentUser = null;
        currentAdmin = null;
        if(loginType==null)
            return;
        db.getCollection(USERLIBRARY).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        if(password.equals(document.getString("password")) && email.equals(document.getString("email"))) {
                            if(loginType.equals(Manager.ADMIN) && document.getString("admin_id").equals("0")) {
                                Toast.makeText(LoginPage.this, "You are not an admin", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            fetchUserData(document.getId(),loginType);
                            return;
                        }
                    }
                    Toast.makeText(LoginPage.this, "incorrect password or email", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    public void fetchDocumentTemplate() {
        documentPaths.clear();
        db.getDocument("Document Template","1").addOnSuccessListener(document ->{
            if (document.exists()) {
                documentPaths.addAll(   (ArrayList<String>)   document.get("paths")      );
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchUserData(String userId, String loginType){
        db.getDocument(USERLIBRARY,userId).addOnSuccessListener(document ->{
            if (document.exists()) {
                String name = document.getString("name");
                String username = document.getString("username");
                String email = document.getString("email");
                String reportNo = document.getString("report_no");
                String stressLevel = document.getString("stress_level");
                String eventNo = document.getString("event_no");
                String phoneNo = document.getString("phone_no");
                String adminId = document.getString("admin_id");
                String password = document.getString("password");
                String profilePhotoUrl = document.getString("profile_photo_url");
                String supportGroupNo = document.getString("support_group_no");
                String stressScore = document.getString("stress_score");
                currentUser = new User(name,Integer.parseInt(userId),username,email,Integer.parseInt(reportNo),stressLevel,Integer.parseInt(eventNo),phoneNo,Integer.parseInt(adminId),password,profilePhotoUrl,Integer.parseInt(supportGroupNo), Integer.parseInt(stressScore));
                if(loginType.equals(USER)) {
                    fetchRequest(currentUser.userId, "user_id");
                    fetchArticle();
                    fetchEvents();
                    fetchSupportGroup();
                    fetchUsers();
                    fetchReports();
                    fetchDocumentTemplate();
                    login(loginType);
                }
                else{
                    fetchAdminData(loginType);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fetchAdminData(String loginType){
        db.getDocument("Admins",Integer.toString(currentUser.adminId)).addOnSuccessListener(document ->{
            if (document.exists()) {
                int requestManaged = Integer.parseInt(document.getString("request_managed"));
                currentAdmin = new Admin(currentUser.name, currentUser.userId, currentUser.username, currentUser.email, currentUser.reportNo, currentUser.stressLevel, currentUser.eventsNo, currentUser.phoneNo, currentUser.adminId, currentUser.password, currentUser.profilePhotoUrl, currentUser.supportGroupNo, requestManaged, currentUser.stressScore);
                fetchArticle();
                fetchEvents();
                fetchSupportGroup();
                fetchUsers();
                fetchRequest(currentAdmin.adminId, "admin_id");
                fetchReports();
                fetchDocumentTemplate();
                login(loginType);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchReports(){
        reports.clear();
        db.getCollection("Reports").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        latestReportIndex = latestReportIndex < Integer.parseInt(document.getId()) ? Integer.parseInt(document.getId()) : latestReportIndex;
                        int reportId = Integer.parseInt(document.getId());
                        String name = document.getString("name");
                        int userId = Integer.parseInt((String) document.get("user_id"));
                        int adminId = Integer.parseInt((String) document.get("admin_id"));
                        String discriminationType = (String) document.get("discrimination_type");
                        String location = (String) document.get("location");
                        Date date = stringToDate(document.getString("date"));
                        String description = (String) document.get("description");
                        String phoneNumber = (String) document.get("phone_number");
                        String email = (String) document.get("email");
                        String witness = (String) document.get("witness");
                        String extraInfo = (String) document.get("extra_info");
                        String personInvolved = (String) document.get("person_involved");
                        String injury = (String) document.get("injury");
                        boolean isAnonymous = Boolean.parseBoolean(document.get("is_anonymous").toString());
                        ArrayList<String> impacts = (ArrayList<String>) document.get("impacts");
                        ArrayList<String> actions = (ArrayList<String>) document.get("actions");
                        reports.add(new Report(reportId, name, userId, adminId, discriminationType, location, date, description, phoneNumber, email, witness, extraInfo, personInvolved, injury, isAnonymous, impacts, actions));
                    }
                    sortReport();
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    private void sortReport() {
        int n = reports.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare the dates in the Report objects
                if (reports.get(j).date.compareTo(reports.get(j + 1).date) > 0) {
                    // Swap if they are in the wrong order
                    Report temp = reports.get(j);
                    reports.set(j, reports.get(j + 1));
                    reports.set(j + 1, temp);
                }
            }
        }
    }

    // fetch request data from firebase
    public void fetchRequest (int id, String idType) {
        requests.clear();
        db.getCollection("Requests").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        latestRequestIndex = latestRequestIndex < Integer.parseInt(document.getId()) ? Integer.parseInt(document.getId()) : latestRequestIndex;
                        //kena cek whether user or admin because kalau user kita display request tapi admin tunjuk request yg dia manage
                        if (id == Integer.parseInt(document.getString(idType))){ //jadikan variable sbb nak dua2 type of user (user and admin sbb kalau admin masuk and kita amik
                            //as current user id, jadi mcm display request of the admin je but cannot manage the reqs
                            //buat parameter sbb ada bnda nk ubah, jadikan variable
                            int adminId =Integer.parseInt(document.getString("admin_id"));
                            String date = document.getString("date");
                            String description = document.getString("description");
                            String desiredOutcome = document.getString("desired_outcome");
                            String method = document.getString("method");
                            String reason = document.getString("reason");
                            String status = document.getString("status");
                            String time = document.getString("time");
                            String type = document.getString("type");
                            String urgency = document.getString("urgency");
                            int userId = Integer.parseInt(document.getString("user_id"));
                            int requestId = Integer.parseInt(document.getId());
                            ArrayList<String> attachmentPaths = (ArrayList<String>) document.get("attachment_paths");

                            requests.add(new Request(reason, desiredOutcome, method, urgency, date, time, description, status, adminId, userId, requestId, type, attachmentPaths));
                        }
                    }
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    private void fetchArticle (){
        articles.clear();
        db.getCollection("Articles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        latestArticleIndex = latestArticleIndex < Integer.parseInt(document.getId()) ? Integer.parseInt(document.getId()) : latestArticleIndex;
                        articles.add(new Article(Integer.parseInt(document.getId()),document.getString("article_url"), document.getString("caption"), document.getString("image_url"), document.getString("author"), document.getString("date"), document.getString("type")));
                    }
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    private void fetchEvents (){
        events.clear();
        db.getCollection("Events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    List<DocumentSnapshot> documents = new ArrayList<>(snapshot.getDocuments());
                    Collections.sort(documents, (doc1, doc2) -> {
                        String dateStr1 = doc1.getString("date");
                        String dateStr2 = doc2.getString("date");

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
                            Date date1 = sdf.parse(dateStr1);
                            Date date2 = sdf.parse(dateStr2);

                            return date1.compareTo(date2); // Ascending order
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });



                    for (DocumentSnapshot document : documents) {
                        latestEventIndex = Integer.parseInt(document.getId());
                        events.add(new Event(Integer.parseInt(document.getId()),
                                document.getString("event_url"),
                                document.getString("title"),
                                document.getString("description"),
                                document.getString("image_url"),
                                document.getString("organizer"),
                                document.getString("date")));
                    }
                }else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    private void fetchSupportGroup(){
        supportGroups.clear();
        db.getCollection("Support Groups").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        latestSupportGroupIndex = latestSupportGroupIndex < Integer.parseInt(document.getId()) ? Integer.parseInt(document.getId()) : latestSupportGroupIndex;
                        ArrayList<String> temp = (ArrayList<String>) document.get("participants_id");
                        ArrayList<Integer> participantsId = new ArrayList();
                        for(int i=0 ; i<temp.size() ; i++){
                            participantsId.add(Integer.parseInt(temp.get(i)));
                        }
                        supportGroups.add(new SupportGroup(document.getString("name"), document.getString("description"), Integer.parseInt(document.getId()), document.getString("icon_url"), participantsId));
                    }
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    private void fetchUsers(){
        users.clear();
        db.getCollection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        String name = document.getString("name");
                        String username = document.getString("username");
                        String email = document.getString("email");
                        String reportNo = document.getString("report_no");
                        String stressLevel = document.getString("stress_level");
                        String eventNo = document.getString("event_no");
                        String phoneNo = document.getString("phone_no");
                        String adminId = document.getString("admin_id");
                        String password = document.getString("password");
                        String profilePhotoUrl = document.getString("profile_photo_url");
                        String supportGroupNo = document.getString("support_group_no");
                        String stressScore = document.getString("stress_score");
                        users.add(new User(name,Integer.parseInt(document.getId()),username,email,Integer.parseInt(reportNo),stressLevel,Integer.parseInt(eventNo),phoneNo,Integer.parseInt(adminId),password,profilePhotoUrl,Integer.parseInt(supportGroupNo), Integer.parseInt(stressScore)));
                    }
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        emailInput.setText("Afzan@gmail.com");
        passwordInput.setText("SayaAdmin");
    }

    @Override
    public void onBackPressed() {
        if(false)
            super.onBackPressed();
        finishAffinity();
    }
}