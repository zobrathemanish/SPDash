package com.sajiloprint.dashboard;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.sajiloprint.dashboard.models.ProductView;
import com.sajiloprint.usersession.UserSession;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private UserSession session;
    private String name;
    private String email;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getValues();
    }

    public void profileLaunch(View view) {
        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
    }

    public void addCard(View view) {
//        startActivity(new Intent(MainActivity.this, ProductView.class));
        Intent intent = new Intent(MainActivity.this, ProductView.class);
        intent.putExtra("value","Add");

        startActivity(intent);
    }

    public void delCard(View view) {
        startActivity(new Intent(MainActivity.this, ProductView.class));
    }

    public void orders(View view) {
        startActivity(new Intent(MainActivity.this, Orders.class));
    }

    private void getValues() {

        //create new session object by passing application context
        session = new UserSession(getApplicationContext());

        //validating session
        session.isLoggedIn();

        //get User details if logged in
        HashMap<String, String> user = session.getUserDetails();

        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);
        System.out.println("nameemailmobile " + name + email + mobile);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
