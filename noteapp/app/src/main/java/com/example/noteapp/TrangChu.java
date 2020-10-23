package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class TrangChu extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private float x1,x2,y1,y2;
    private static  int MIN_DISTANCE=150;
    private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        TextView tv=(TextView) findViewById(R.id.tvtest);
        Intent it=getIntent();
        String value=it.getStringExtra("userid");
        Toast.makeText(this,""+value+"",Toast.LENGTH_LONG).show();
        detector=new GestureDetector(TrangChu.this,this);


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
                        Toast.makeText(this,"Right",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this,"left",Toast.LENGTH_LONG).show();

                    }
                }
                else if(Math.abs(valueY)>MIN_DISTANCE)
                {
                    if(y2>y1){
                        Toast.makeText(this,"bottom",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this,"top",Toast.LENGTH_LONG).show();
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