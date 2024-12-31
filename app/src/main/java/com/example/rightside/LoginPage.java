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
    private boolean valid = false;
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

        RadioGroup loginOptionGroup = findViewById(R.id.LoginOptionGroup);
        EditText emailInput = findViewById(R.id.SignUpEmailInput);
        EditText passwordInput = findViewById(R.id.LoginPasswordInput);
        ImageButton HideButton = findViewById(R.id.HideButton);
        Button loginButton = findViewById(R.id.LoginButton);
        Typeface font = passwordInput.getTypeface();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedId = loginOptionGroup.getCheckedRadioButtonId();
                email = emailInput.getText().toString();
                password = passwordInput.getText().toString();
                RadioButton selectedButton = findViewById(selectedId);
                validateLoginData(selectedButton.getText().toString().toUpperCase());
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

    private void login(int userId, String loginType){

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
                            System.out.println("baru nak cek");
                            if(loginType.equals(Manager.ADMIN) && document.getString("adminId").equals(""))
                                break;
                            System.out.println("login");
                            login(Integer.parseInt(document.getId()),loginType);
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

}