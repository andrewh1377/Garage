package com.example.andrew.garage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AdminActivity extends AppCompatActivity {

    WebView mWebView;
    Button powerButton;
    TextView userView, state_view, temp_view;
    Button guestPin;
    String userString, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        userView = findViewById(R.id.userNameView);
        state_view = findViewById(R.id.stateView);
        temp_view = findViewById(R.id.tempView);
        powerButton = findViewById(R.id.PowerButton);
        guestPin = findViewById(R.id.GuestPinButton);
        mWebView = findViewById(R.id.activity_View);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        Bundle info = getIntent().getExtras();
        userString = info.getString("USER_ID");
        userView.setText("Welcome " + userString + "!");
        Background b = new Background();
        Background c = new Background();

        flag = "0";
        b.execute(flag);

        flag = "2";
        c.execute(flag);


        String piAddress = "http://108.218.183.252:8081";
        try {
            //Load URL to WebView
            mWebView.loadUrl(piAddress);
            Toast.makeText(AdminActivity.this, "Connected: " + piAddress,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AdminActivity.this, "Connection Failed!",
                    Toast.LENGTH_SHORT).show();
        }

        guestPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity0();
            }
        });

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Background b = new Background();
                flag = "1";
                b.execute(flag);
            }
        });


    }


    class Background extends AsyncTask<String, String, String> {
        //String response;
        @Override
        protected String doInBackground(String... params) {
            String url;
            String credentials = "pi:andy4444";
            String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            String response = "";

            if (flag == "1") {
                url = "http://@108.218.183.252/?trigger=1";
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(String.valueOf(url));
                httpGet.setHeader("Authorization", "Basic " + credBase64);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (flag == "0") {
                url = "http://@108.218.183.252/temp.txt";
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(String.valueOf(url));
                httpGet.setHeader("Authorization", "Basic " + credBase64);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (flag == "2") {
                url = "http://@108.218.183.252/distance.txt";
                DefaultHttpClient client = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet(String.valueOf(url));
                httpGet.setHeader("Authorization", "Basic " + credBase64);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (flag == "1") {
                try {
                    Toast.makeText(AdminActivity.this, "Door Opening",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AdminActivity.this, "Something Went Wrong!!",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (flag == "0") {
                //state_view.setText("The garage is " + response);
                temp_view.setText(response);
                System.out.println(response);
                if (response == " Tyler loves the D!"){
                    powerButton.setText("OPEN");
                }
                else{
                    powerButton.setText("CLOSE");
                }
            } else if (flag == "2") {
                state_view.setText("The garage is " + response);
                System.out.println(response);
            }
        }


    }

    private void launchActivity0() {
        Intent intent = new Intent(AdminActivity.this, GuestPinActivity.class);
        intent.putExtra("USER_ID", userString);
        startActivity(intent);
    }
}