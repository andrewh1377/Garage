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
    Button powerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        powerButton = findViewById(R.id.PowerButton);


        String piAddress = "http://108.218.183.252:8081";
        mWebView = (WebView)findViewById(R.id.activity_View);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
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
                Background b = new Background();
                b.execute();
            }
        });

    }
    class Background extends AsyncTask<String, String, String> {
        //String response;

        @Override
        protected String doInBackground(String... params) {
            String credentials = "pi:andy4444";
            String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            String response = "";

            String url = "http://@108.218.183.252/?trigger=1";
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
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                Toast.makeText(ViewActivity.this, "Door Opening",
                        Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(ViewActivity.this, "Something Went Wrong!!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


}