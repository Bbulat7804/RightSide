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

public class LoginPage extends AppCompatActivity {
    boolean hide = true;
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
                int selectedId = loginOptionGroup.getCheckedRadioButtonId();
                login(selectedId, emailInput.getText().toString(), passwordInput.getText().toString());
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

    private void login(int selectedId, String email, String password){
        if(selectedId==-1)
            return;
        //Check login type user/admin
        RadioButton selectedButton = findViewById(selectedId);
        Manager.userType = selectedButton.getText().toString().toUpperCase();

        //if password or email is wrong return false
        if(!validateLoginData(email,password))
            return;

        //loadUserData();

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

    private boolean validateLoginData(String email, String password){
        return true;
    }

}