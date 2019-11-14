package com.httpparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView resutlTv;

    private HttpURLConnection connection = null;

    BufferedReader bufferedReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resutlTv = (TextView) findViewById(R.id.resultTv);

        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText (getApplicationContext() , "Please be cool and wait" ,Toast.LENGTH_LONG ).show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://httpbin.org/get");

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                final StringBuffer stringBuffer = new StringBuffer();

                String line = "";

                while ((line =bufferedReader.readLine())!= null){

                    stringBuffer.append(line);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resutlTv.setText(stringBuffer.toString());
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){ connection.disconnect();}
                try {
                    if (bufferedReader != null){ bufferedReader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}