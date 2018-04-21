package com.example.andrew.garage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GuestPinActivity extends AppCompatActivity implements View.OnClickListener {
    String pin, userString;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_pin);
        Bundle info = getIntent().getExtras();
        userString = info.getString("USER_ID");



        Button b0= findViewById(R.id.zeroButton);
        Button b1= findViewById(R.id.oneButton);
        Button b2= findViewById(R.id.twoButton);
        Button b3= findViewById(R.id.threeButton);
        Button b4= findViewById(R.id.fourButton);
        Button b5= findViewById(R.id.fiveButton);
        Button b6= findViewById(R.id.sixButton);
        Button b7= findViewById(R.id.sevenButton);
        Button b8= findViewById(R.id.eightButton);
        Button b9= findViewById(R.id.nineButton);
        Button bPound= findViewById(R.id.poundButton);
        Button bStar= findViewById(R.id.starButton);
        Button bEnter= findViewById(R.id.enterButton);
        Button bDelete= findViewById(R.id.deleteButton);
        b0.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        bPound.setOnClickListener(this);
        bStar.setOnClickListener(this);
        bEnter.setOnClickListener(this);
        bDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        result = findViewById(R.id.passwordText);
        pin = result.getText().toString();
        switch (v.getId())
        {
            case R.id.zeroButton:
                result.append("0");
                break;
            case R.id.oneButton:
                result.append("1");
                break;
            case R.id.twoButton:
                result.append("2");
                break;
            case R.id.threeButton:
                result.append("3");
                break;
            case R.id.fourButton:
                result.append("4");
                break;
            case R.id.fiveButton:
                result.append("5");
                break;
            case R.id.sixButton:
                result.append("6");
                break;
            case R.id.sevenButton:
                result.append("7");
                break;
            case R.id.eightButton:
                result.append("8");
                break;
            case R.id.nineButton:
                result.append("9");
                break;
            case R.id.poundButton:
                result.append("#");
                break;
            case R.id.starButton:
                result.append("*");
                break;
            case R.id.enterButton:
                Background b = new Background();
                b.execute(pin);
                break;
            case R.id.deleteButton:
                result.setText("");
                break;

        }
    }


    class Background extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String pinCode = params[0];
            System.out.println(pinCode);
            String credentials = "pi:andy4444";
            String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            System.out.println(credBase64);
            String response = "";

            String url = "http://108.218.183.252/GuestPin.php?Pin=" + pinCode;
            System.out.println(url);
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
            String userData = "";
            String pinData = "";
            String adminData = "";
            try {
                String finalJson = response.toString();
                JSONObject userObject = new JSONObject(finalJson);
                System.out.println(userObject);
                userData = String.valueOf(userObject.get("User"));
                System.out.println(userData);
                pinData = String.valueOf(userObject.get("Pin"));
                System.out.println(pinData);
                adminData = String.valueOf(userObject.get("Admin"));
                System.out.println(adminData);
            }
            catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

                launchActivity0();

        }
    }


    private void launchActivity0() {
        Intent intent = new Intent(this, AdminActivity.class);
        intent.putExtra("USER_ID", userString);
        startActivity(intent);
    }

}
