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
                Config.COLUMN_YEAR + " TEXT NOT NULL," +
                Config.COLUMN_MONTH + " TEXT NOT NULL," +
                Config.COLUMN_DAY + " TEXT NOT NULL," +
                Config.COLUMN_HOUR + " TEXT NOT NULL," +
                Config.COLUMN_MIN + " TEXT NOT NULL," +
                Config.COLUMN_SEC + " TEXT NOT NULL," +
                Config.COLUMN_UV_VALUE + " REAL NOT NULL) ";

        Log.d(TAG,"table created with this query "+ CREATE_TABLE_UV_DATA);
        sqLiteDatabase.execSQL(CREATE_TABLE_UV_DATA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ALTER THE DESIGN FOR AN UPDATE
    }

    // do this instead
    //public long insertUV(UvReadings uv,float uvIntensity){
    public long insertUV(float uvIntensity,Calendar currentDateTime){
        long id =-1;

        currentDateTime.getInstance();
        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);
        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_YEAR, year);
        contentValues.put(Config.COLUMN_MONTH, month);
        contentValues.put(Config.COLUMN_DAY, day);
        contentValues.put(Config.COLUMN_HOUR, hour);
        contentValues.put(Config.COLUMN_MIN, minute);
        contentValues.put(Config.COLUMN_SEC, second);
        contentValues.put(Config.COLUMN_UV_VALUE, uvIntensity);


        try {
            id = sqLiteDatabase.insertOrThrow(Config.UV_TABLE_NAME, null, contentValues);
        }

        catch (SQLiteException e){
            Log.d(TAG,"Exception"+ e.getMessage());
            Toast.makeText(context, "UMAID HELP ME", Toast.LENGTH_SHORT).show();
        }

        finally { // if nothing goes wrong, it will go to db.close() if there was an error it wont go
            sqLiteDatabase.close(); }
        return id;
    }

// do this instead
    //public List<UvReadings> getAllUVData() {
    public List<UvReadings> getAllUVData(String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // before removing COLUMN_DATE
            //cursor = database.query(Config.UV_TABLE_NAME, null, null, null, Config.COLUMN_ID,  Config.COLUMN_DATE + "=" + "'" + date + "'", Config.COLUMN_UV_TIME);

            cursor = database.query(Config.UV_TABLE_NAME, null, null, null,
                    null,null,null);

            if (cursor != null && cursor.moveToFirst())
            {
                cursor.moveToFirst();
                List<UvReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));
                    int day = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_DAY));
                    int month = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MONTH));
                    int year = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_YEAR));
                    int hour = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_HOUR));
                    int min = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MIN));
                    int sec = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SEC));
                    float value =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_VALUE));

                    UvReadings uvReadings = new UvReadings(id,year,month,day,hour,min,sec,value);
                    uvList.add(uvReadings);


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