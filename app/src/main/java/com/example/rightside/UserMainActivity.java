package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserMainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout homeButton = findViewById(R.id.HomeButton);
        LinearLayout profileButton = findViewById(R.id.ProfileButton);
        LinearLayout helpButton = findViewById(R.id.HelpButton);
        if(savedInstanceState == null) {
            goToPage(eventsPage, getSupportFragmentManager());
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stack.clear();
                goToPage(userHomePage,getSupportFragmentManager());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(userProfilePage,getSupportFragmentManager());
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(faqPage,getSupportFragmentManager());
            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.color_up_button);
    }

    @Override
    public void onBackPressed() {
        if(false)
            super.onBackPressed();
        if(stack.isEmpty()) {
            Intent intent = new Intent(UserMainActivity.this, LoginPage.class);
            startActivity(intent);
        }
        else {
            stack.removeFirst();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, stack.getFirst()).commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(stack.isEmpty()) {
            Intent intent = new Intent(UserMainActivity.this, LoginPage.class);
            startActivity(intent);
        }
        else {
            stack.removeFirst();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, stack.getFirst()).commit();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mailbox, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.mailbox){
            //bukak mailbox
        }
        return super.onOptionsItemSelected(item);
    }


}