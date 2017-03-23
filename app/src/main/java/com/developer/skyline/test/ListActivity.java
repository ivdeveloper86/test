package com.developer.skyline.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.net.URL;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
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

public class ListActivity extends AppCompatActivity {

    Intent intent;
    SharedPreferences mSettings;
    TextView textView0, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9;
    private int[] id = new int[10];
    private int x, y;
    private static final String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // получаем id категории
        x = (int) getIntent().getSerializableExtra("NEWS");

        // восстанавливаем записанный номер пейджинга страницы
        mSettings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        y = mSettings.getInt("PAGE", 0);

        textView0 = (TextView) findViewById(R.id.textView0);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);

        new ParseTask().execute();
    }

    // обработка клика по новости
    public void newsClick(View view) {
        switch (view.getId()) {
            case R.id.textView0:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[0]);
                startActivity(intent);
                break;
            case R.id.textView1:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[1]);
                startActivity(intent);
                break;
            case R.id.textView2:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[2]);
                startActivity(intent);
                break;
            case R.id.textView3:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[3]);
                startActivity(intent);
                break;
            case R.id.textView4:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[4]);
                startActivity(intent);
                break;
            case R.id.textView5:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[5]);
                startActivity(intent);
                break;
            case R.id.textView6:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[6]);
                startActivity(intent);
                break;
            case R.id.textView7:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[7]);
                startActivity(intent);
                break;
            case R.id.textView8:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[8]);
                startActivity(intent);
                break;
            case R.id.textView9:
                intent = new Intent(this, ViewActivity.class);
                intent.putExtra("NEWS", id[9]);
                startActivity(intent);
                break;
        }
    }

    // обработка клика пейджинга страниц
    public void pageClick(View view) {
        SharedPreferences.Editor editor = mSettings.edit();
        switch (view.getId()) {
            case R.id.textView_1:
                editor.putInt("PAGE", 0);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_2:
                editor.putInt("PAGE", 1);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_3:
                editor.putInt("PAGE", 2);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_4:
                editor.putInt("PAGE", 3);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_5:
                editor.putInt("PAGE", 4);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_6:
                editor.putInt("PAGE", 5);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_7:
                editor.putInt("PAGE", 6);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_8:
                editor.putInt("PAGE", 7);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_9:
                editor.putInt("PAGE", 8);
                editor.apply();
                this.recreate();
                break;
            case R.id.textView_10:
                editor.putInt("PAGE", 9);
                editor.apply();
                this.recreate();
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
                URL url = new URL("http://testtask.sebbia.com/v1/news/categories/" + x + "/news?page=" + y);

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

                // получаем список list
                JSONArray categories = dataJsonObj.getJSONArray("list");

                for (int n = 0; n < categories.length(); n++) {
                    // получаем элементы по id
                    JSONObject list = categories.getJSONObject(n);

                    // получаем поля
                    id[n] = list.getInt("id");
                    String title = list.getString("title");
                    String date = list.getString("date");
                    String shortDescription = list.getString("shortDescription");

                    // конвертируем дату
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try {
                        Date d = sdf.parse(date);
                        formattedTime = output.format(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // присваиваем данные TextView
                    if (n == 0) textView0.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 1) textView1.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 2) textView2.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 3) textView3.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 4) textView4.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 5) textView5.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 6) textView6.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 7) textView7.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 8) textView8.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                    if (n == 9) textView9.setText(Html.fromHtml("<b>" + title + "</b>" + "<br>" + formattedTime + "<br>" + shortDescription));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
