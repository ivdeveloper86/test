package com.developer.skyline.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.net.URL;
import android.os.AsyncTask;
import android.webkit.WebView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class ViewActivity extends AppCompatActivity {

    WebView webView;
    private static final String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        webView = (WebView) findViewById(R.id.webView);

        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        // получаем id категории
        int x = (int) getIntent().getSerializableExtra("NEWS");

        @Override
        protected String doInBackground(Void... params) {

            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://testtask.sebbia.com/v1/news/details?id=" + x);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            JSONObject dataJsonObj = null;
            String formattedTime = "";

            try {
                dataJsonObj = new JSONObject(strJson);

                // получаем список news
                JSONObject news = dataJsonObj.getJSONObject("news");

                // получаем поля
                String title = news.getString("title");
                String date = news.getString("date");
                String shortDescription = news.getString("shortDescription");
                String fullDescription = news.getString("fullDescription");

                // конвертируем дату
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date d = sdf.parse(date);
                    formattedTime = output.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // присваиваем данные WebView
                webView.loadDataWithBaseURL("", title + "<br>" + formattedTime + "<br>" + shortDescription + "<br>" + fullDescription, "text/html", "UTF-8", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
