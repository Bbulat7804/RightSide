package com.example.rightside;

import static com.example.rightside.Manager.USER;
import static com.example.rightside.Manager.USERLIBRARY;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {
    boolean hidePassword = true;
    boolean hideReconfirmPassword = true;
    DatabaseConnection db = new DatabaseConnection();

    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView nameInput = findViewById(R.id.SignUpNameInput);
        TextView usernameInput = findViewById(R.id.SignUpUsernameInput);
        TextView phoneNoInput = findViewById(R.id.SignUpPhoneNoInput);
        TextView emailInput = findViewById(R.id.SignUpEmailInput);
        TextView passwordInput = findViewById(R.id.SignUpPasswordInput);
        TextView reconfirmPasswordInput = findViewById(R.id.SignUpReconfirmPasswordInput);
        Button signUpButton = findViewById(R.id.SignUpButton);
        Button goToLoginButton = findViewById(R.id.GoToLoginButton);
        ImageButton passwordHideButton = findViewById(R.id.HidePasswordButton);
        ImageButton reconfirmPasswordHideButton = findViewById(R.id.HideReconfirmPasswordButton);
        font = passwordInput.getTypeface();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameInput.getText().toString().trim().equals("") && !usernameInput.getText().toString().trim().equals("") && !phoneNoInput.getText().toString().trim().equals("") && !emailInput.getText().toString().trim().equals("") && !passwordInput.getText().toString().trim().equals("") && !reconfirmPasswordInput.getText().toString().trim().equals("")) {
                    validateSignUpData(nameInput.getText().toString(), usernameInput.getText().toString(), phoneNoInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString(), reconfirmPasswordInput.getText().toString());
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        passwordHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePassword = hidePassword==false;

                if(hidePassword==false)
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput.setTypeface(font);

            }
        });

        reconfirmPasswordHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReconfirmPassword = hideReconfirmPassword==false;

                if(hideReconfirmPassword==false)
                    reconfirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    reconfirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                reconfirmPasswordInput.setTypeface(font);

            }
        });
    }

    public void validateSignUpData(String name, String username, String phoneNo, String email, String password, String reconfirmPassword){
        db.getCollection(USERLIBRARY).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    boolean valid = true;
                    int id = 0;
                    for (QueryDocumentSnapshot document : snapshot) {
                        if(document.getString("email").equals(email) || document.getString("username").equals(username)){
                            valid = false;
                        }
                        id = Integer.parseInt(document.getId());
                    }
                    id += 1;
                    if(valid)
                        signUp(Integer.toString(id),name,username,phoneNo,email,password);
                } else {
                    System.out.println("No documents found in the collection.");
                }
            } else {
                System.err.println("Error fetching documents: " + task.getException());
            }
        });
    }

    public void signUp(String userId, String name, String username, String phoneNo, String email, String password){
        HashMap<String,Object> userData = new HashMap();
        userData.put("admin_id","0");
        userData.put("email",email);
        userData.put("event_no","0");
        userData.put("name",name);
        userData.put("password",password);
        userData.put("phone_no",phoneNo);
        userData.put("name",name);
        userData.put("profile_photo_url","");
        userData.put("report_no", "0");
        userData.put("stress_level","No Data");
        userData.put("username",username);
        userData.put("support_group_no","0");
        userData.put("stress_score", "0");
        db.addDocument("Users", userData, userId);
        Intent intent = new Intent(SignUpPage.this,LoginPage.class);
        startActivity(intent);
    }
}