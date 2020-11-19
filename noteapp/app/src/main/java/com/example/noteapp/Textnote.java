package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.TypefaceCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Textnote extends AppCompatActivity {
    BottomNavigationView navigation;
    EditText txttitle,txtcontent;
    LocationManager myLM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textnote);
        innit();
         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void innit(){
        txttitle=(EditText) findViewById(R.id.title);
        txtcontent=(EditText) findViewById(R.id.content);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.so:
                    Toast.makeText(Textnote.this,"so",Toast.LENGTH_LONG).show();
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    return true;
                case R.id.cham:
                    Toast.makeText(Textnote.this,"chan",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.dam:

                    Boolean status=false;
                    if(status==true){
                        Toast.makeText(Textnote.this,"true",Toast.LENGTH_LONG).show();
                        status=false;
                    }
                    else {
                        status=true;
                        boldSpan = new StyleSpan(Typeface.BOLD);
                        int start = txtcontent.getSelectionStart();
                        int end = txtcontent.getSelectionEnd();
                        int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
                        txtcontent.getText().setSpan(boldSpan, start, end, flag);
                    }

                    return true;
                case R.id.nghieng:
                    Toast.makeText(Textnote.this,"nghieng",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.gachchan:
                    Toast.makeText(Textnote.this,"gachchan",Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutoolbarcreatetext,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.luu:
                checkInternetConnection();
                checkpermission();
                postTextnote();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //check permistion

    public void checkpermission() {
        //neu la ndroid <M thi

        //neu co roi thi dang ki broadcasrt recever been treen neu chua co yeu cau nguoi dung cap quyen
        String[] pem_array = {Manifest.permission.ACCESS_FINE_LOCATION};


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //khai bao list chua quyen can xin
            List<String> list_permission = new ArrayList<String>();
            for (int i = 0; i < pem_array.length; i++) {
                if (checkSelfPermission(pem_array[i]) != PackageManager.PERMISSION_GRANTED) {

                    list_permission.add(pem_array[i]);
                }
            }
            if(!list_permission.isEmpty()){
                requestPermissions(list_permission.toArray(new String[list_permission.size()]),9999);
            }
            else {

            }
        }
        else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void postTextnote() {
        Map<String,String> postData = new HashMap<>();
        postData.put("userid", ""+TrangChu.iduser+"");
        postData.put("title", ""+txttitle.getText().toString()+"");
        postData.put("content", ""+txtcontent.getText().toString()+"");

            postData.put("gps", ""+Mylocationchage.GPS+"");
        postData.put("createdate",""+ LocalDateTime.now() +"");
        HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
        //task.execute("http://192.168.1.100:58938/api/note");
        //task.execute("http://192.168.1.101:58938/api/note");
        task.execute(""+Const.URL+"/uploadtext");
    }
    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
        // This is the JSON body of the post
        JSONObject postData;
        // This is a constructor that allows you to pass in the JSON body
        public HttpPostAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
        @Override
        protected String doInBackground(String... params) {
            try {
                // This is getting the url from the string we passed in
                URL url = new URL(params[0]);
                // Create the urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                // OPTIONAL - Sets an authorization header
                urlConnection.setRequestProperty("Authorization", "someAuthString");
                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();

                if (statusCode ==  200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    String response = convertInputStreamToString(inputStream);
                    //Log.d("res", response);
                    //JSONObject jsonObject = new JSONObject(response);
                    //String res = jsonObject.getString("result_message");
                    return  response;
                    //Toast.makeText(DoiMkActivity.this, "result_message: " + res, Toast.LENGTH_SHORT).show();
                } else {
                    // Status code is not 200
                    // Do something to handle the error
                }

            } catch (Exception e) {
                Log.d("ERR: ", e.getLocalizedMessage());
            }
            return null;
        }

        protected void onPostExecute(String result) {

            }
        }


}