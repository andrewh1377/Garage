package com.example.andrew.garage;

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
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ViewActivity extends AppCompatActivity {

    WebView mWebView;
    TextView state_view, temp_view;
    Button powerButton;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        state_view = findViewById(R.id.stateView);
        temp_view = findViewById(R.id.tempView);
        powerButton = findViewById(R.id.PowerButton);
        mWebView = (WebView)findViewById(R.id.activity_View);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        Background b = new Background();
        Background c = new Background();

        b.execute("temp");
        c.execute("status");


        String piAddress = "http://108.218.183.252:8081";

        try {
            //Load URL to WebView
            mWebView.loadUrl(piAddress);
            Toast.makeText(ViewActivity.this, "Connected: " + piAddress,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewActivity.this, "Connection Failed!",
                    Toast.LENGTH_SHORT).show();
        }

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "power";
                Background b = new Background();
                b.execute(flag);
            }
        });

    }
    class Background extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String url;
            String credentials = "pi:andy4444";
            String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            String response = "";

            if (params[0] == "power") {
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
            } else if (params[0] == "temp") {
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

                    temp_view.setText(response);
                    System.out.println(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (params[0] == "status") {
                url = "http://@108.218.183.252/distance_2.txt";
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

                    state_view.setText("The Garage is " + response);
                    System.out.println(response);
                    if (response == "Closed") {
                        powerButton.setText("OPEN");
                    } else {
                        powerButton.setText("CLOSE");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            System.out.print(response);
        }
    }


}