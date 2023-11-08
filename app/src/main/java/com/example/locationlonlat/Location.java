package com.example.locationlonlat;

public class Location {
    //BASIC LOCATION OBJECT WITH ID ADDRESS LATITUDE AND LONGITUDE VALUES

    private int id;
    private String address, latitude, longitude;

    //WHATS NEEDED FOR THE LOCATION (ID ADDRESS LATITUDE LONGITUDE)
    public Location(int id, String address, String latitude, String longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //CONSTRUCTOR METHODS
    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }
}
