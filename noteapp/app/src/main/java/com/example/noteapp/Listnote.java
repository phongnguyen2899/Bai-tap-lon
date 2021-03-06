package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        lisview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note i=arr.get(position);
                Toast.makeText(Listnote.this,""+i.getId()+"",Toast.LENGTH_SHORT).show();
                Toast.makeText(Listnote.this,""+i.getImg()+"",Toast.LENGTH_SHORT).show();
                String im=i.getImg();
                if(i.getImg()=="null"){
                    Intent it= new Intent(Listnote.this,Notedetail.class);
                    it.putExtra("noteid",i.getId());
                    startActivity(it);
                }
                else {
                    Intent it=new Intent(Listnote.this,NoteImgdetail.class);
                    it.putExtra("noteid",i.getId());
                    startActivity(it);
                }

            }
        });

        //giữ để xóa

        lisview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note i=arr.get(position);
                arr.remove(i);
                loadtext(i.getId());
                return true;
            }
        });

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
        //task.execute("http://192.168.1.100:58938/api/note?id="+TrangChu.iduser+"");
        //task.execute(""+Const.URL+"/api/note?id="+TrangChu.iduser+"");
        task.execute(""+Const.URL+"/getnotebyuserid/"+TrangChu.iduser+"");
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
                        String id=obj.getString("id");
                        String title=obj.getString("title");
                        String content=obj.getString("content");
                        String createdate=obj.getString("createdate");
                        String im=obj.getString("img");
                        Note item=new Note(id,title,content,createdate,im);
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

    // xoa note
    public void loadtext(String id){

            //String webUrl="http://192.168.1.100:58938/api/account?username="+txtusername.getText().toString()+"&password="+txtpassword.getText().toString()+"";
            // String webUrl="http://192.168.1.101:58938/api/account?username="+txtusername.getText().toString()+"&password="+txtpassword.getText().toString()+"";
            // String webUrl="http://192.168.0.101:58938/api/account";

            DeleteAsynctask task = new DeleteAsynctask();

            task.execute(""+Const.URL+"/delete/"+id+"");
    }
    public class DeleteAsynctask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        public DeleteAsynctask()  {

        }

        @Override
        protected String doInBackground(String... params) {
            String textUrl = params[0];

            InputStream in = null;
            BufferedReader br= null;
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
                    br= new BufferedReader(new InputStreamReader(in));

                    StringBuilder sb= new StringBuilder();
                    String s= null;
                    while((s= br.readLine())!= null) {
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Listnote.this);
            pDialog.setMessage("Waitting");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        // When the task is completed, this method will be called
        // Download complete. Lets update UI
        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                try {
                    JSONObject j=new JSONObject(result);
                    String result1=j.getString("result");
                    if (result1.equals("success")){
                        noteAdapter.notifyDataSetChanged();
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}