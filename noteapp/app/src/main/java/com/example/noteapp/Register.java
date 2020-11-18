package com.example.noteapp;

import androidx.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText txtusername,txtpassword,txtconfirmpassword,txtemail;
    Button btnregister;
    public static int idcreateaccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        register();
    }
    public void register(){
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtusername.getText().toString().equals("")||txtpassword.getText().toString().equals("")||
                txtconfirmpassword.getText().toString().equals("")||txtemail.getText().toString().equals("")){
                    Toast.makeText(Register.this,"Bạn cần điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(txtpassword.getText().toString().equals(txtconfirmpassword.getText().toString())==false){
                        Toast.makeText(Register.this,"Mật khẩu nhập lại không chính xác",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //post object lên sevver để nhận code
                        checkpermission();
                        postaccount();

                    }

                }
            }
        });
    }
    public void init(){
        txtusername=(EditText) findViewById(R.id.edtusernamecr);
        txtpassword=(EditText) findViewById(R.id.edtpasswordcr);
        txtconfirmpassword=(EditText) findViewById(R.id.edtconfirmpasswordcr);
        txtemail=(EditText) findViewById(R.id.edtemailcr);
        btnregister=(Button) findViewById(R.id.btnregister);
    }

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
    void postaccount() {
        Map<String,String> postData = new HashMap<>();
        postData.put("username", ""+txtusername.getText().toString()+"");
        postData.put("password", ""+txtpassword.getText().toString()+"");
        postData.put("email", ""+txtemail.getText().toString()+"");


        HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
        //task.execute("http://192.168.1.100:58938/api/note");
        task.execute("http://192.168.1.101:58938/api/account");
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
        private ProgressDialog pDialog;
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Waitting");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected void onPostExecute(String result) {
            if(result!=null){
                try {
                    JSONObject j=new JSONObject(result);
                    String result1=j.getString("result");
                    String id=j.getString("id");
                    Log.e("id", id );
                    if (result1.equals("success")){
                        Intent it=new Intent(Register.this,Active.class);
                        it.putExtra("userid",id);
                        startActivity(it);

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