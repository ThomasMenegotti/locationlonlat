package com.example.locationlonlat;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private  DBHandler dbHandler;

    private EditText addressInput;
    private Button lonlatVal, edit,delete,add;
    private TextView lon,lat;


    private ArrayList<Location> locArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize variables
        dbHandler = new DBHandler(MainActivity.this);
        locArrayList = new ArrayList<>();
        addressInput= findViewById(R.id.addressField);
        lonlatVal= findViewById(R.id.lonlatDispButton);
        lon=findViewById(R.id.LonView);
        lat=findViewById(R.id.LatView);
        edit=findViewById(R.id.editButton);
        delete=findViewById(R.id.deleteData);
        add=findViewById(R.id.addData);

        //set onClick listeners for
        //"Show Latitude and Longitude", "Edit Address Data", "Get and Add Data" and "Delete Data" Buttons
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ensures that the Latitude and Longitude Values are present
                //If they are blank, set hint to inform user how to use this button.
                if(lat.getText().equals("")){
                    lat.setHint("First, enter the new address and search for the location. After locating the address, press 'Edit Address Button' again button to update the address in the database.");
                    lon.setHint("First, enter the new address and search for the location. After locating the address, press 'Edit Address Button' again button to update the address in the database.");
                }else{
                    //If they are present Store those values in strings
                    String[] vals = new String[2];
                    vals[0] = lat.getText().toString();
                    vals[1] = lon.getText().toString();
                    String latitude=vals[0];
                    String longitude=vals[1];

                    String newAddress=addressInput.getText().toString();
                    //Send to a function to handle the editing address of a location
                    editLocation(latitude,longitude,newAddress);
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ensures that the Latitude and Longitude Values are present
                //If they are blank, set hint to inform user how to use this button.
                if(lat.getText().equals("")){
                    lat.setHint("Enter in address and search to delete entry from table");
                    lon.setHint("Enter in address and search to delete entry from table");
                }else{
                    //If they are present Store those values in strings
                    String[] vals = new String[2];
                    vals[0]= lat.getText().toString();
                    vals[1]=lon.getText().toString();
                    String latitude =vals[0];
                    String longitude = vals[1];

                    //Send to a function to handle the deleting entries of a location
                    removeLocation(latitude,longitude);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ensures that the Address Input Field is populated
                //If they are blank, set hint to inform user how to use this button.
                String address=addressInput.getText().toString();
                if(address.equals("")){
                    lat.setText("");
                    lon.setText("");
                    lat.setHint("Enter in an exact Address line in order to add an entry to the database.");
                    lon.setHint("Enter in an exact Address line in order to add an entry to the database.");
                }else{
                    //Send to a function to handle the forward Geocoding to get latitude and longitude of address
                    forwardGeo(address,1);

                }
            }
        });

        lonlatVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the text inputting from Address Input Field and passes to query method to handle searching
                //Database for the values associated with the address
                String addressQuery=addressInput.getText().toString();
                String[] result=queryLocation(addressQuery);
                //If address is not found set as hint it was not found
                if(result[0].equals("Address not found")){
                    lon.setHint(result[1]);
                    lat.setHint(result[0]);
                }else {
                    //else display the coordinates
                    lon.setText(result[1]);
                    lat.setText(result[0]);
                }
            }
        });

        String fileName = "values.txt";
        try {
            //Send the file name to a readfile function to read and get the values of the file
            readFile(fileName);
            locArrayList = dbHandler.readLocations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //This is to view the entire database dump in Logcat, allowing you to ensure database is
        //Working, updating, deleting, adding.
        if(!locArrayList.isEmpty()) {
            for(Location loc : locArrayList) {
                Log.i("Location Database", "ID: "+loc.getId() + " | Address: " + loc.getAddress() + " | Latitude: " + loc.getLatitude() + " | Longitude: " +loc.getLongitude());
            }
        }


    }

    public String reverseGeo(double latitude, double longitude, int NUM_RESULTS) {
        //This was discussed in class as the best practice for reverse GeoCoding
        //This method is to allow application to reverse geocode
        //Given a latitude and longitude value pair, get the full address line

        //Default result value
        String results = "false";

        //Ensure that Geocoder is present in code
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this,
                    Locale.getDefault());
            try {
                //Sets a list to get info from location with lon and lat values
                List<Address> ls = geocoder.getFromLocation(latitude,
                        longitude, NUM_RESULTS);
                for (Address addr: ls) {
                    String name = addr.getFeatureName();
                    String address = addr.getAddressLine(0);
                    String city = addr.getLocality();
                    String county = addr.getSubAdminArea();
                    String prov = addr.getAdminArea();
                    String country = addr.getCountryName();
                    String postalCode = addr.getPostalCode();
                    String phone = addr.getPhone();
                    String url = addr.getUrl();
                    //Stores the full address line in result and return it
                    results = address;
                }
            } catch (IOException e) { }

        }
        return results;
    }

    public void forwardGeo(String addAddress, int NUM_RESULTS){
        //This was discussed in class as the best practice for forward GeoCoding WITHOUT constraints
        //This method is to allow application to forward geocode
        //Given an address, get the longitude and latitude values

        //Ensures that geocoder is present
        if(Geocoder.isPresent()){
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            try{
                //Gets info from location name which is the addAddress parameter when calling this method
                List<Address> ls= geocoder.getFromLocationName(addAddress,NUM_RESULTS);
                for(Address addr:ls){
                    //Gets and stores longitude and latitude values in Strings
                    double latitude = addr.getLatitude();
                    double longitude = addr.getLongitude();
                    String latitudeStr=Double.toString(latitude);
                    String longitudeStr=Double.toString(longitude);

                    //Pass to database to add as entry for new location given the address and longitude and latitude values found
                    dbHandler.addNewLocation(addAddress,latitudeStr,longitudeStr);

                    //This is to view the entire database dump in Logcat, allowing you to ensure database is
                    //Working, updating, deleting, adding.
                    locArrayList = dbHandler.readLocations();
                    if(!locArrayList.isEmpty()) {
                        for(Location loc : locArrayList) {
                            Log.i("Location Database", "ID: "+loc.getId() + " | Address: " + loc.getAddress() + " | Latitude: " + loc.getLatitude() + " | Longitude: " +loc.getLongitude());
                        }
                    }
                }
            }catch (IOException e){}
            //Toast to notify user that it has worked
            Toast.makeText(this, "Added Entry in Database",Toast.LENGTH_SHORT).show();

        }
    }

    public String[] queryLocation(String address){
        //Function for Handing the Query of database given the full address line is given
        DBHandler dbHander=new DBHandler(MainActivity.this);
        //Sets the long & lat values in result after calling the database function "queryLocation"
        String[] result=dbHander.queryLocation(address);

        dbHandler.close();
        return result;
    }
    public void removeLocation(String latitude, String longitude){
        //Function for Handing the Removal of database entry given the Long & Lat values are populated & passed
        DBHandler dbHandler = new DBHandler(MainActivity.this);
        //Passes to database to remove entry from database
        dbHandler.removeLocation(latitude, longitude);
        //Toast to notify user that it has worked
        Toast.makeText(this, "Removed Entries from Database",Toast.LENGTH_SHORT).show();
        dbHandler.close();

        //This is to view the entire database dump in Logcat, allowing you to ensure database is
        //Working, updating, deleting, adding.
        locArrayList = dbHandler.readLocations();
        if(!locArrayList.isEmpty()) {
            for(Location loc : locArrayList) {
                Log.i("Location Database", "ID: "+loc.getId() + " | Address: " + loc.getAddress() + " | Latitude: " + loc.getLatitude() + " | Longitude: " +loc.getLongitude());
            }
        }
    }

    public void editLocation(String latitude, String longitude, String newAddress){
        //Function for Handing the Editing of Address in database entry given the Long & Lat values are populated & passed along with a new address
        DBHandler dbHandler=new DBHandler(MainActivity.this);
        //Passes to database to Update entry to database
        dbHandler.updateLocation(latitude,longitude,newAddress);

        //Toast to notify user that it has worked
        Toast.makeText(this, "Updated Address names in database",Toast.LENGTH_SHORT).show();
        dbHandler.close();

        //This is to view the entire database dump in Logcat, allowing you to ensure database is
        //Working, updating, deleting, adding.
        locArrayList = dbHandler.readLocations();
        if(!locArrayList.isEmpty()) {
            for(Location loc : locArrayList) {
                Log.i("Location Database", "ID: "+loc.getId() + " | Address: " + loc.getAddress() + " | Latitude: " + loc.getLatitude() + " | Longitude: " +loc.getLongitude());
            }
        }
    }

    public void readFile(String fileName) throws IOException {
        //Simple readFile function to grab the coord values from file then passes them to reverse geo
        //to get the full address line
        String[] coords;
        String address = "";

        //Create a FileInputStream and a BufferedReader to read the file
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                coords = line.split(", ");
                address = reverseGeo(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 1);
                if(address != null) {
                    dbHandler.addNewLocation(address, coords[0], coords[1]);
                }
            }
        }
    }

}