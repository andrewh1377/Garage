package com.example.andrew.garage;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PinActivity extends AppCompatActivity implements View.OnClickListener {
    String pin;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);


        Button b0= findViewById(R.id.ZeroButton);
        Button b1= findViewById(R.id.OneButton);
        Button b2= findViewById(R.id.TwoButton);
        Button b3= findViewById(R.id.ThreeButton);
        Button b4= findViewById(R.id.FourButton);
        Button b5= findViewById(R.id.FiveButton);
        Button b6= findViewById(R.id.SixButton);
        Button b7= findViewById(R.id.SevenButton);
        Button b8= findViewById(R.id.EightButton);
        Button b9= findViewById(R.id.NineButton);
        Button bPound= findViewById(R.id.PoundButton);
        Button bStar= findViewById(R.id.StarButton);
        Button bEnter= findViewById(R.id.EnterButton);
        Button bDelete= findViewById(R.id.DeleteButton);
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
        result = findViewById(R.id.PasswordText);
        pin = result.getText().toString();
        switch (v.getId())
        {
            case R.id.ZeroButton:
                result.append("0");
                break;
            case R.id.OneButton:
                result.append("1");
                break;
            case R.id.TwoButton:
                result.append("2");
                break;
            case R.id.ThreeButton:
                result.append("3");
                break;
            case R.id.FourButton:
                result.append("4");
                break;
            case R.id.FiveButton:
                result.append("5");
                break;
            case R.id.SixButton:
                result.append("6");
                break;
            case R.id.SevenButton:
                result.append("7");
                break;
            case R.id.EightButton:
                result.append("8");
                break;
            case R.id.NineButton:
                result.append("9");
                break;
            case R.id.PoundButton:
                result.append("#");
                break;
            case R.id.StarButton:
                result.append("*");
                break;
            case R.id.EnterButton:
                Background b = new Background();
                b.execute(pin);
                break;
            case R.id.DeleteButton:
                result.setText("");
                break;

        }
    }

    class Background extends AsyncTask<String, String, String> {
        //String response;

        @Override
        protected String doInBackground(String... params) {
            String pinCode = params[0];
            System.out.println(pinCode);
            String credentials = "pi:andy4444";
            String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");
            System.out.println(credBase64);
            String response = "";

                String url = "http://108.218.183.252/Login.php?Pin=" + pinCode;
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


            if (pinData.equals(pin) && adminData.equals("1")) {
                launchActivity0(userData);
            }
            else if (pinData.equals(pin) && adminData.equals("0")){
                launchActivity1();
            }
            else
                launchActivity2();

        }
    }


    private void launchActivity0(String userData) {
        Intent intent = new Intent(this, AdminActivity.class);
        intent.putExtra("USER_ID", userData);
        startActivity(intent);
    }
    private void launchActivity1() {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }
    private void launchActivity2(){
        Toast.makeText(PinActivity.this, "Incorrect Pin!",
                Toast.LENGTH_SHORT).show();
    }


}
