package com.example.locationlonlat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "location11";
    private static final String TABLE_NAME = "geocoding11";
    private static final String IDS_COL = "id";
    private static final String ADDRESSES_COL = "address";
    private static final String LATITUDE_COL = "latitude";

    private static final String LONGITUDE_COL = "longitude";

    private static final int  DB_VERSION = 14;
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //When starting the application, this runs ensuring that a table is created with the correct columns.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + IDS_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESSES_COL + " TEXT,"
                + LATITUDE_COL + " TEXT,"
                + LONGITUDE_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public void addNewLocation(String address, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //On the below line we are passing all values with its key and values that are passed when calling this method.
        values.put(ADDRESSES_COL, address);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);
        //In this line we are passing the values to our database and the database will automatically auto increment the first ID column.
        db.insert(TABLE_NAME, null, values);
        //Proper code etiquette to close database connection.
        db.close();

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //This method will check if the table already exists within the database.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void removeLocation(String latitude, String longitude) {
        //This method will remove location from database based on the Latitude and Longitude
        SQLiteDatabase db = this.getWritableDatabase();
        //This was originally error checking code.
        if(latitude.equals("Enter in address and search to delete entry from table")){
            Log.d("Removal", "removeLocation: Nothing to remove");
        }else {
            Log.d("LONGLATVAL2", "Delete Values: " + latitude + " lat and "+longitude +" long.");
            //Deletes from table where Latitude is equal to the latitude parameter passed when calling this method
            //And where Longitude is equal to the longitude parameter passed when calling this method
            db.delete(TABLE_NAME, LATITUDE_COL + " = ? AND " + LONGITUDE_COL + " = ?", new String[]{latitude,longitude});
            db.close();
        }

    }

    public ArrayList<Location> readLocations() {
        //This method will get all locations from the database and returns an ArrayList of the locations to be printed in the Logcat Section of Android Studio.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Location> locArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                //This will grab the data in cursor and add it to a new locArrayList instance
                locArrayList.add(new Location(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            //Keeps going until cursor.moveToNext returns with nothing left to move to
        }
        cursor.close();

        return locArrayList;
    }

    public String[] queryLocation(String addressQuery){
        //This method will read the latitude and longitude values from the database
        //corresponding with the address that was passed through while calling this method.
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT " + LATITUDE_COL + ", " + LONGITUDE_COL + " FROM " + TABLE_NAME + " WHERE " + ADDRESSES_COL+ " = ?";
        String[] selection={addressQuery};
        Cursor cursor = db.rawQuery(query,selection);
        String[] result=new String[2];
        //Default result
        result[0]="Address not found";
        result[1]="Address not found";

        if (cursor.moveToFirst()){
            //If in database, store the latitude and longitude values in result array and return it
            String latitude = cursor.getString(0);
            String longitude= cursor.getString(1);
            result[0]=latitude;
            result[1]=longitude;
        }
        cursor.close();
        db.close();
        return result;
    }

    public void updateLocation(String latitude, String longitude, String address) {
        //This method will update a location to the new address given the address parameter
        //For all instances that have the latitude and longitude parameters in database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ADDRESSES_COL, address);

        db.update(TABLE_NAME, values, LATITUDE_COL +" = ? AND "+ LONGITUDE_COL + " = ?", new String[]{latitude,longitude});
    }
}

