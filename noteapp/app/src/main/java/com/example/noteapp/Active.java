package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Active extends AppCompatActivity {
    EditText txtcode;
    Button btnverifi;
    TextView tvstatus;
    public int idnewcreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        innit();
        Intent it=getIntent();
        String value=it.getStringExtra("userid");
        idnewcreate=Integer.parseInt(value);
        activeaccount();
    }
    public void activeaccount(){
        btnverifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_text();
            }
        });
    }
    public  void innit(){
        txtcode=(EditText) findViewById(R.id.codeverifi);
        btnverifi=(Button) findViewById(R.id.btnverifi);
        tvstatus=(TextView) findViewById(R.id.status);

    }
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
    void load_text()
    {
        if (checkInternetConnection())
        {

            //String webUrl="http://192.168.1.100:58938/api/account?username="+txtusername.getText().toString()+"&password="+txtpassword.getText().toString()+"";
            String webUrl="http://192.168.1.101:58938/api/account?id="+idnewcreate+"&code="+txtcode.getText().toString()+"";
            // String webUrl="http://192.168.0.101:58938/api/account";

            DownloadJsonTask task = new DownloadJsonTask();

            task.execute(webUrl);
        }
    }

    public class DownloadJsonTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        public DownloadJsonTask()  {

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
            pDialog = new ProgressDialog(Active.this);
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
                        tvstatus.setText("Đăng ký thành công");
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }
                    }
                    else {
                        tvstatus.setText("Mã xác nhận không đúng");
                        pDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}