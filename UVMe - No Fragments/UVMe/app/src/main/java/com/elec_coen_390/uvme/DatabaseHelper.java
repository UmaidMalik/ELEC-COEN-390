package com.elec_coen_390.uvme;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private float uvIndex = 0.00f;
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context)
    { super(context, DATABASE_NAME, null, DATABASE_VERSION );
    this.context = context;}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE THE TABLES
        String CREATE_TABLE_UV_DATA = "CREATE TABLE " + Config.UV_TABLE_NAME +
                " (" +
                Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_DATE + " TEXT NOT NULL," +
                Config.COLUMN_UV_VALUE + " REAL NOT NULL," +
                Config.COLUMN_UV_TIME + " REAL NOT NULL) ";
        Log.d(TAG,"table created with this qurery "+ CREATE_TABLE_UV_DATA);
        sqLiteDatabase.execSQL(CREATE_TABLE_UV_DATA);
        Log.d(TAG,"Course table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ALTER THE DESIGN FOR AN UPDATE
    }

    public long insertUV(float uvIntensity, Calendar currentDateTime){
        long id =-1;

        currentDateTime = Calendar.getInstance();
        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);


        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR);

        //StringBuilder stringBuilder = new StringBuilder(second + " " )
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:mm:yyyy");
        SimpleDateFormat simpleHourFormat = new SimpleDateFormat("hh:mm:ss");

        simpleDateFormat.applyPattern(String.valueOf(day) + String.valueOf(month) + String.valueOf(year));
        simpleHourFormat.applyPattern(String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_DATE, String.valueOf(simpleDateFormat));
        contentValues.put(Config.COLUMN_UV_VALUE, UVSensorData.getUVIntensity() );
        contentValues.put(Config.COLUMN_UV_TIME, String.valueOf(simpleHourFormat));
        try {
            id = db.insertOrThrow(Config.UV_TABLE_NAME, null, contentValues);
        }

        catch (SQLiteException e){
            Log.d(TAG,"Exception"+ e.getMessage());
            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show();
        }

        finally { // if nothing goes wrong, it will go to db.close() if there was an error it wont go
            db.close(); }
        return id;
    }

    public List<UvReadings> getAllUVData(String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        //uvIndex = UVSensorData.getUVIntensity();
        try {
            cursor = database.query(Config.UV_TABLE_NAME, null, null, null, Config.COLUMN_ID,  Config.COLUMN_DATE + "=" + "'" + date + "'", Config.COLUMN_UV_TIME);
            if (cursor != null && cursor.moveToFirst())
            {
                cursor.moveToFirst();
                List<UvReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));
                    double time = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_UV_TIME));
                    float value =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_VALUE));
                     // value  = UVSensorData.getUVIntensity();
                    //UvReadings uvReadings = new UvReadings(time, value, date);
                    //uvList.add(uvReadings);
                } while (cursor.moveToNext());
                return uvList;
            }
        }
        catch (SQLException exception) { Log.d(TAG, "EXCEPTION: " + exception);}
            finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }
        return Collections.emptyList(); // Nothing to display
    }

}