package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MotionEventCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TrangChu extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private float x1,x2,y1,y2;
    private static  int MIN_DISTANCE=150;
    private GestureDetector detector;
    LocationManager myLM;
    String gpstest;

    public int getIduser() {
        return iduser;
    }

    public static int iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        Intent it=getIntent();
        String value=it.getStringExtra("userid");
        Toast.makeText(this,""+value+"",Toast.LENGTH_LONG).show();
        iduser=Integer.parseInt(value);
        detector=new GestureDetector(TrangChu.this,this);
        myLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
    public void refreshLocation( ) {
        //lay vi tri cua may de hien thi
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
        myLM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new Mylocationchage());
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();
                float valueX=x2-x1;
                float valueY=y2-y1;

                if(Math.abs(valueX)>MIN_DISTANCE){
                    if(x2>x1){

                        Toast.makeText(this,"left",Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(this,"Right",Toast.LENGTH_LONG).show();
                        Intent it= new Intent(TrangChu.this,Listnote.class);
                        startActivity(it);
                    }
                }
                else if(Math.abs(valueY)>MIN_DISTANCE)
                {
                    if(y2>y1){
                        Toast.makeText(this,"top",Toast.LENGTH_LONG).show();
                        Intent it= new Intent(TrangChu.this,Imagenote.class);
                        startActivity(it);

                    }
                    else {
                        refreshLocation();
                        Toast.makeText(this,"bottom",Toast.LENGTH_LONG).show();
                        Intent it= new Intent(TrangChu.this,Textnote.class);
                        startActivity(it);
                    }
                }
        }
        return super.onTouchEvent(event);
    }



    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}