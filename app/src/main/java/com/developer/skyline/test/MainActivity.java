package com.developer.skyline.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.net.URL;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    SharedPreferences mSettings;
    TextView muchNews, someNews, notNews;
    private static final String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        muchNews = (TextView) findViewById(R.id.muchNews);
        someNews = (TextView) findViewById(R.id.someNews);
        notNews = (TextView) findViewById(R.id.notNews);

        new ParseTask().execute();
    }

    public void newsClick(View view) {

        // очистка значений
        mSettings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.remove("PAGE");
        editor.apply();

        switch (view.getId()) {
            case R.id.muchNews:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("NEWS", 0);
                startActivity(intent);
                break;
            case R.id.someNews:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("NEWS", 1);
                startActivity(intent);
                break;
            case R.id.notNews:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("NEWS", 2);
                startActivity(intent);
                break;
        }
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://testtask.sebbia.com/v1/news/categories");

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

            try {
                JSONObject dataJsonObj = new JSONObject(strJson);

                // получаем список list
                JSONArray categories = dataJsonObj.getJSONArray("list");

                for (int n = 0; n < categories.length(); n++) {
                    // получаем элементы по id
                    JSONObject list = categories.getJSONObject(n);

                    // присваиваем данные TextView
                    if (n == 0) muchNews.setText(list.getString("name"));
                    if (n == 1) someNews.setText(list.getString("name"));
                    if (n == 2) notNews.setText(list.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
