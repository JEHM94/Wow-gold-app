package com.example.carries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    TextView outputView = findViewById(R.id.textViewOutput);

                    URL url = new URL("https://us.battle.net/oauth/token");

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("client_id", "fa75ca71e373400b9ed4c4c8c8c8c5b4");
                    connection.setRequestProperty("client_secret", "xUEWjNdGp2ERMt6UI3UDlIvc7rwPCf7J");
                    connection.setRequestProperty("grant_type", "client_credentials");

                    connection.setDoOutput(true);
                    DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());

                    //dStream.writeBytes(urlParameters);
                    dStream.flush();
                    dStream.close();

                    int responseCode = connection.getResponseCode();
                    String output = "Request Url: " + url;
                    //output += System.getProperty("line.separator") + "Request Parameters: " + urlParameters;
                    output += System.getProperty("line.separator") + "Response Code: " + responseCode;

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    output += System.getProperty("line.separator") + responseOutput.toString();

                    outputView.setText(output);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}