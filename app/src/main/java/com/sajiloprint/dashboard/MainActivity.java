package com.sajiloprint.dashboard;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;
import com.sajiloprint.dashboard.models.ProductView;
import com.sajiloprint.usersession.UserSession;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private UserSession session;
    private String name;
    private String email;
    private String mobile;
    private TextView shopname;
    private String shopemail;
    private String send_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getValues();

        System.out.println("name email mobile = " + name + email + mobile);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentuser = mAuth.getCurrentUser().getEmail();
        shopemail = currentuser;
        OneSignal.sendTag("User_ID", shopemail);
        System.out.println("shopemail " + shopemail);

        shopname = findViewById(R.id.shopname);
        shopname.setText(currentuser);



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
        sendNotification();
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
    }

    private void sendNotification() {

        Toast.makeText(this, "Current Recipients is : user1@gmail.com ( Just For Demo )", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    System.out.println("shopemail here is" + shopemail);
                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (shopemail.equals("manishborninnepal@gmail.com")) {
                        send_email = "manishofficial4378@gmail.com";
                    }
                    else {
                        send_email = "manishborninnepal@gmail.com";
                    }

                    try {
                        System.out.println("sendemail" + send_email);
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic OTYwODcwM2QtOGZmZS00ODkwLWE3N2EtYjY2ZTZkODg4YTRh");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"82cedb4e-1757-47c6-a7a1-2f1dd7ffb698\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"English Message\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        System.out.println("asdf" + HttpURLConnection.HTTP_OK + HttpURLConnection.HTTP_BAD_REQUEST);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
