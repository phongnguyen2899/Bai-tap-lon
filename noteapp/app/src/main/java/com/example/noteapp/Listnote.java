package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Listnote extends AppCompatActivity {
    ListView lisview;
    ArrayList<Note> arr= new ArrayList<>();
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listnote);
        lisview=(ListView) findViewById(R.id.listview);
        noteAdapter =new NoteAdapter(this,arr);
        lisview.setAdapter(noteAdapter);
        checkpermission();

    }

    public void checkpermission(){
        //neu la ndroid <M thi

        //neu co roi thi dang ki broadcasrt recever been treen neu chua co yeu cau nguoi dung cap quyen

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int has_internet=checkSelfPermission(Manifest.permission.INTERNET);
            //khai bao list chua quyen can xin
            List<String> list_permission=new ArrayList<String>();
            if(has_internet!= PackageManager.PERMISSION_GRANTED){
                list_permission.add(Manifest.permission.INTERNET);

            }

            //neu co quyen can xin
            if(!list_permission.isEmpty()){
                //elenh xin quyen
                requestPermissions(list_permission.toArray(new String[list_permission.size()]),9999);
            }
            else {
                //lay du lieu tu API VAo spinner
                callgetcountry();

            }
        }
        else{
            callgetcountry();
        }
    }

    public void callgetcountry(){
        DownloadJsonTask task= new DownloadJsonTask(this.arr);
        task.execute("http://192.168.1.100:58938/api/note?id="+TrangChu.iduser+"");
        //task.execute("http://192.168.43.48:58938/api/note?id="+TrangChu.iduser+"");
    }

    public class DownloadJsonTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {

        private ArrayList<Note> listnote;

        public DownloadJsonTask(ArrayList<Note> context) {
            this.listnote = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String textUrl = params[0];

            InputStream in = null;
            BufferedReader br = null;
            try {
                URL url = new URL(textUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                    br = new BufferedReader(new InputStreamReader(in));

                    StringBuilder sb = new StringBuilder();
                    String s = null;
                    while ((s = br.readLine()) != null) {
                        sb.append(s);
                        sb.append("\n");
                    }
                    return sb.toString();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Close
            }
            return null;
        }

        // When the task is completed, this method will be called
        // Download complete. Lets update UI
        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                //xu ly
                //convert tu string sang json array
                try {
                    JSONArray arr= new JSONArray(s);
                    //lap va them vao arrayadapter
                    for(int i=0;i<arr.length();i++){
                        JSONObject obj= arr.getJSONObject(i);
                        String title=obj.getString("title");
                        String content=obj.getString("content");
                        String createdate=obj.getString("createdate");
                        Note item=new Note(title,content,createdate);
                        listnote.add(item);
                    }
                    noteAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {

            }
        }

    }
}