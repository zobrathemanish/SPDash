package com.sajiloprint.dashboard;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sajiloprint.dashboard.models.ProductView;
import com.sajiloprint.dashboard.models.SearchBarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
