package com.example.risingstar;

public class Location {
    private double mLatitude, mLongitude;

    public Location(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() { return mLatitude; }

    public double getLongitude() { return mLongitude; }
}
