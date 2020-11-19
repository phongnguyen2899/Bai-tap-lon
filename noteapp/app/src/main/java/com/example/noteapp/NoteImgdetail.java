package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoteImgdetail extends AppCompatActivity {
    EditText txttitleimge;
    TextView tvtets;
    String id;
    String[] imgname;
    ArrayList<Bitmap> bitmapArrayList;
    LinearLayout linearLayout;
    String imgnames="";
    int index=2;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_imgdetail);
        innit();
        Intent it=getIntent();
        id=it.getStringExtra("noteid");
        load_text();
       // Picasso.get().load("http://192.168.1.101:58938/Images/20201027_144025.jpg").into(imageView);
        /*String s=method(imgnames);
        imgname=imgnames.split(",");
        for (int i=0;i<imgname.length;i++){
            if (checkInternetConnection())
            {
                String imageUrl = "http://192.168.1.101:58938/Images/"+imgname[i]+"";
                // Create a task to download and display image.
                DownloadImageTask task = new DownloadImageTask();
                // Execute task (Pass imageUrl).
                task.execute(imageUrl);
            }
        }
        for (int i=0;i<bitmapArrayList.size();i++){
            ImageView item = new ImageView(this);
            item.setImageBitmap(bitmapArrayList.get(i));
            linearLayout.addView(item,index);
            index++;
        }*/

    }
    public void loadanh(String [] listimg){
        for (int i=0;i<listimg.length;i++){
            ImageView item = new ImageView(this);
            Picasso.get().load(""+Const.URL+"/Images/"+listimg[i]+"").into(item);
            linearLayout.addView(item,index);
            }
    }


    private void innit() {
        txttitleimge=(EditText) findViewById(R.id.edttitleimg);
        tvtets=(TextView) findViewById(R.id.tvtestimg);
        linearLayout=(LinearLayout) findViewById(R.id.linnearimgdetail);
        imageView=(ImageView) findViewById(R.id.testview);
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

            //String webUrl="http://192.168.1.100:58938/api/note?idnote="+id+"";
            String webUrl=""+Const.URL+"/api/note?idnote="+id+"";

            DownloadJsonTask task =new DownloadJsonTask(this.tvtets,imgnames);

            task.execute(webUrl);
        }
    }
    public class DownloadJsonTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        TextView tv;
        String s;
        public DownloadJsonTask(TextView tv,String s)  {
            this.tv=tv;
            this.s=s;
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
            pDialog = new ProgressDialog(NoteImgdetail.this);
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
                    String title=j.getString("title");
                    String img=j.getString("img");
                    txttitleimge.setText(title);
                        // load.dissloadding();
                    imgnames=img;
                    imgname=img.split(",");
                    loadanh(imgname);

                    tv.setText(img);
                        if(pDialog.isShowing()){
                            pDialog.dismiss();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class DownloadImageTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public DownloadImageTask()  {
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];

            InputStream in = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                int resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                } else {
                    return null;
                }

                Bitmap bitmap = BitmapFactory.decodeStream(in);
                bitmapArrayList.add(bitmap);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                }catch (Exception e) {

                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result  != null){
                this.imageView.setImageBitmap(result);
            } else{
                Log.e("MyMessage", "Failed to fetch data!");
            }
        }
    }
}