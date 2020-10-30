package com.example.noteapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class Mylocationchage implements LocationListener {


    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public static String GPS;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String displaycontent=""+location.getLongitude()+","+location.getLatitude()+"";
        GPS=displaycontent;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
