package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.HashMap;
import java.util.Map;

public class Notedetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText txttitle,txtcontent;
    public String[] gps;
    public String id;
    public float log=0;
    public float lat=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        Intent it=getIntent();
         id=it.getStringExtra("noteid");
        innit();
        load_text();

    }
    public void innit(){
        txttitle=(EditText) findViewById(R.id.edttitle);
        txtcontent=(EditText) findViewById(R.id.edtcontent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menunotedetail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.luudetail:
                checkInternetConnection();
                postTextnote();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Viết code để tương tác với bản đồ

        // Thay đổi kiểu bản đồ
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Thay đổi góc camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, log))       // Zoom vào Lat, Long
                .zoom(18)
                .bearing(0)
                .tilt(40)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //Thêm MarketOption cho Map:
        if(lat!=0){
            MarkerOptions myMarker =new MarkerOptions();
            myMarker.title("Chỗ bạn tạo ghi chú");
            myMarker.snippet("Là ");
            myMarker.position(new LatLng(lat, log));

            Marker currentMarker= mMap.addMarker(myMarker);
            currentMarker.showInfoWindow();
        }
        else {
            MarkerOptions myMarker =new MarkerOptions();
            myMarker.title("Chỗ bạn tạo ghi chú");
            myMarker.snippet("Là ");
            myMarker.position(new LatLng(20.985310, 105.838620));

            Marker currentMarker= mMap.addMarker(myMarker);
            currentMarker.showInfoWindow();
        }

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
            String webUrl=""+Const.URL+"/getnotebyid/"+id+"";

            DownloadJsonTask task =new DownloadJsonTask(this.txttitle,this.txtcontent);

            task.execute(webUrl);
        }
    }
    public class DownloadJsonTask
            // AsyncTask<Params, Progress, Result>
            extends AsyncTask<String, Void, String> {

        public EditText title;
        public EditText content;

        public DownloadJsonTask(EditText title, EditText content) {
            this.title = title;
            this.content = content;
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
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject j = new JSONObject(result);
                    String jtitle = j.getString("title");
                    String jcontent = j.getString("content");
                    String jgps = j.getString("gps");
                    this.title.setText(jtitle);
                    this.content.setText(jcontent);
                    if (!jgps.equals("null")) {
                        gps=jgps.split(",");
                        log=Float.parseFloat(gps[0]);
                        lat=Float.parseFloat(gps[1]);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(Notedetail.this);
                    }
                    else {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(Notedetail.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    //luu lai
    @RequiresApi(api = Build.VERSION_CODES.O)
    void postTextnote() {
        Map<String,String> postData = new HashMap<>();
        postData.put("id", ""+id+"");
        postData.put("title", ""+txttitle.getText().toString()+"");
        postData.put("content", ""+txtcontent.getText().toString()+"");

       HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
        //task.execute("http://192.168.1.100:58938/api/note");
        //task.execute("http://192.168.1.101:58938/api/note");
        task.execute(""+Const.URL+"/updatenotetext");
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