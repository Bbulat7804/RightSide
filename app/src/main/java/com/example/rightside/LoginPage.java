package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {
    boolean hide = true;
    private String email;
    private String password;
    private int selectedId;

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
            @Override
            public void onClick(View v) {
                selectedId = loginOptionGroup.getCheckedRadioButtonId();
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                RadioButton selectedButton = findViewById(selectedId);
                if(selectedButton!=null)
                    validateLoginData(selectedButton.getText().toString().toUpperCase());
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

    private void validateLoginData(String loginType){
        if(loginType==null)
            return;
        db.getCollection(USERLIBRARY).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        if(password.equals(document.getString("password")) && email.equals(document.getString("email"))) {
                            if(loginType.equals(Manager.ADMIN) && document.getString("admin_id").equals("0"))
                                break;
                            fetchUserData(document.getId(),loginType);
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

                Manager.currentUser = new User(name,Integer.parseInt(userId),username,email,Integer.parseInt(reportNo),stressLevel,Integer.parseInt(eventNo),phoneNo,Integer.parseInt(adminId),password,profilePhotoUrl,Integer.parseInt(supportGroupNo));
                if(loginType.equals(USER)) {
                    fetchRequest(currentUser.userId, "user_id");
                    fetchArticle();
                    login(loginType);
                }
                else{
                    fetchAdminData(loginType);
                }
            }
        });
    }

    private void fetchAdminData(String loginType){
        db.getDocument("Admins",Integer.toString(currentUser.adminId)).addOnSuccessListener(document ->{
            if (document.exists()) {
                int requestManaged = Integer.parseInt(document.getString("request_managed"));
                currentAdmin = new Admin(currentUser.name, currentUser.userId, currentUser.username, currentUser.email, currentUser.reportNo, currentUser.stressLevel, currentUser.eventsNo, currentUser.phoneNo, currentUser.adminId, currentUser.password, currentUser.profilePhotoUrl, currentUser.supportGroupNo, requestManaged);
                fetchArticle();
                fetchRequest(currentAdmin.adminId, "admin_id");
                login(loginType);
            }
        });
    }
    // fetch request data from firebase
    public void fetchRequest (int id, String idType) {
        requests.clear();
        db.getCollection("Requests").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        latestRequestIndex = Integer.parseInt(document.getId());
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

                            requests.add(new Request(reason, desiredOutcome, method, urgency, date, time, description, status, adminId, userId, requestId, type));
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
                        latestArticleIndex = Integer.parseInt(document.getId());
                        articles.add(new Article(Integer.parseInt(document.getId()),document.getString("article_url"), document.getString("caption"), document.getString("image_url"), document.getString("author"), document.getString("date")));
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