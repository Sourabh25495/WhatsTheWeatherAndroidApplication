package com.sourabhkulkarni.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;

    TextView resultTextView;
    public void  findWeather(View view){


        Log.i("Name",cityName.getText().toString());
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);//hide keyboard on click
        imm.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {
            String encodedCityNmae= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            DownloadTask dlt=new DownloadTask();
            dlt.execute("http://api.openweathermap.org/data/2.5/weather?q="+ encodedCityNmae +"&APPID=0f84e63fb6cf5dff7cada470d85a3a5f");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById((R.id.editText2));
        resultTextView=(TextView)findViewById(R.id.textView2);
    }

    public  class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url=new URL(params[0]);

                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);

                int data=reader.read();
                while ((data!=-1)){

                    char current=(char)data;
                    result+=current;
                    data=reader.read();

                }
                return result;



            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
            }


            return null;
        }
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    String message="";
                    JSONObject jsonObject = new JSONObject(result);

                    String weatherInfo = jsonObject.getString("weather");
                    Log.i("content", weatherInfo);
                    JSONArray arr = new JSONArray(weatherInfo);
                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject jpart = arr.getJSONObject(i);
                        String main="";
                        String description="";
                        //main=jpart.getString("main");
                        description=jpart.getString("description");
                        Log.i("main", jpart.getString("main"));
                        Log.i("description", jpart.getString("description"));

                        if(main!=null&& description!=null){
                            message+=main+" "+description+"\r\n";

                        }
                    }
                    if(message!=null){
                        resultTextView.setText(message);
                    }else {
                        Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG).show();
                }
                Log.i("Content", result);
            }
        }
    }
}
