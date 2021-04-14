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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.text.DecimalFormat;

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
                Config.COLUMN_UV_VALUE + " REAL NOT NULL," +
                Config.COLUMN_HOUR + " INTEGER NOT NULL," +
                Config.COLUMN_MIN + " INTEGER NOT NULL," +
                Config.COLUMN_SEC + " INTEGER NOT NULL," +
                Config.COLUMN_DAY + " INTEGER NOT NULL," +
                Config.COLUMN_MONTH + " INTEGER NOT NULL," +
                Config.COLUMN_YEAR + " INTEGER NOT NULL) ";
        Log.d(TAG,"table created with this query "+ CREATE_TABLE_UV_DATA);
        sqLiteDatabase.execSQL(CREATE_TABLE_UV_DATA);
        Log.d(TAG,"Course table Created");

        String CREATE_TABLE_UV_MAX_DATA = "CREATE TABLE " + Config.UV_TABLE_NAME_MAX +
                " (" +
                Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_UV_MAX_VALUE + " REAL NOT NULL," +
                Config.COLUMN_HOUR + " INTEGER NOT NULL," +
                Config.COLUMN_MIN + " INTEGER NOT NULL," +
                Config.COLUMN_SEC + " INTEGER NOT NULL," +
                Config.COLUMN_DAY + " INTEGER NOT NULL," +
                Config.COLUMN_MONTH + " INTEGER NOT NULL," +
                Config.COLUMN_YEAR + " INTEGER NOT NULL) ";
        Log.d(TAG,"table created with this query "+ CREATE_TABLE_UV_MAX_DATA);
        sqLiteDatabase.execSQL(CREATE_TABLE_UV_MAX_DATA);
        Log.d(TAG,"Course table Created");

        String CREATE_TABLE_UV_GRAPH_DATA = "CREATE TABLE " + Config.UV_TABLE_NAME_GRAPH +
                " (" +
                Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_UV_MAX_VALUE_GRAPH + " REAL NOT NULL," +
                Config.COLUMN_UV_AVERAGE_VALUE_GRAPH + " REAL NOT NULL," +
                Config.COLUMN_HOUR + " INTEGER NOT NULL," +
                Config.COLUMN_MIN + " INTEGER NOT NULL," +
                Config.COLUMN_SEC + " INTEGER NOT NULL," +
                Config.COLUMN_DAY + " INTEGER NOT NULL," +
                Config.COLUMN_MONTH + " INTEGER NOT NULL," +
                Config.COLUMN_YEAR + " INTEGER NOT NULL) ";
        Log.d(TAG,"table created with this query "+ CREATE_TABLE_UV_GRAPH_DATA);
        sqLiteDatabase.execSQL(CREATE_TABLE_UV_GRAPH_DATA);
        Log.d(TAG,"Course table Created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ALTER THE DESIGN FOR AN UPDATE
    }

    public long insertUV(float uvIntensity, Calendar currentDateTime){
        long id =-1;

        DecimalFormat df = new DecimalFormat("#,###,##0.00");

        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);
        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR_OF_DAY);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_UV_VALUE, df.format(uvIntensity));
        contentValues.put(Config.COLUMN_HOUR, hour); //added
        contentValues.put(Config.COLUMN_MIN, minute);
        contentValues.put(Config.COLUMN_SEC, second);
        contentValues.put(Config.COLUMN_DAY, day); //added
        contentValues.put(Config.COLUMN_MONTH, month + 1);
        contentValues.put(Config.COLUMN_YEAR, year);



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

    public long insertUVMax(float uvIntensity, Calendar currentDateTime){
        long id =-1;

        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        //currentDateTime = Calendar.getInstance();
        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);
        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR_OF_DAY);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_UV_MAX_VALUE, df.format(uvIntensity));
        contentValues.put(Config.COLUMN_HOUR, hour); //added
        contentValues.put(Config.COLUMN_MIN, minute);
        contentValues.put(Config.COLUMN_SEC, second);
        contentValues.put(Config.COLUMN_DAY, day); //added
        contentValues.put(Config.COLUMN_MONTH, month + 1);
        contentValues.put(Config.COLUMN_YEAR, year);



        try {
            id = db.insertOrThrow(Config.UV_TABLE_NAME_MAX, null, contentValues);
        }

        catch (SQLiteException e){
            Log.d(TAG,"Exception"+ e.getMessage());
            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show();
        }

        finally { // if nothing goes wrong, it will go to db.close() if there was an error it wont go
            db.close(); }
        return id;
    }

    public long insertUVGraph(float uvMAX, float uvAVG, Calendar currentDateTime){
        long id =-1;

        DecimalFormat df = new DecimalFormat("#,###,##0.00");

        int day = currentDateTime.get(Calendar.DAY_OF_MONTH);
        int month = currentDateTime.get(Calendar.MONTH);
        int year = currentDateTime.get(Calendar.YEAR);
        int second = currentDateTime.get(Calendar.SECOND);
        int minute = currentDateTime.get(Calendar.MINUTE);
        int hour = currentDateTime.get(Calendar.HOUR_OF_DAY);


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_UV_MAX_VALUE_GRAPH, df.format(uvMAX));
        contentValues.put(Config.COLUMN_UV_AVERAGE_VALUE_GRAPH, df.format(uvAVG));
        contentValues.put(Config.COLUMN_HOUR, hour);
        contentValues.put(Config.COLUMN_MIN, minute);
        contentValues.put(Config.COLUMN_SEC, second);
        contentValues.put(Config.COLUMN_DAY, day);
        contentValues.put(Config.COLUMN_MONTH, month + 1);
        contentValues.put(Config.COLUMN_YEAR, year);



        try {
            id = db.insertOrThrow(Config.UV_TABLE_NAME_GRAPH, null, contentValues);
        }

        catch (SQLiteException e){
            Log.d(TAG,"Exception"+ e.getMessage());
            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show();
        }

        finally { // if nothing goes wrong, it will go to db.close() if there was an error it wont go
            db.close(); }
        return id;
    }

    public List<UVReadings> getAllUVData() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.query(Config.UV_TABLE_NAME_MAX, null, null, null, null,  null, null);

            if (cursor != null && cursor.moveToLast())
            {
                cursor.moveToLast();
                List<UVReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    long id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));


                    float uvIndexValue =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_MAX_VALUE));

                    int hour = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_HOUR));
                    int minute = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MIN));
                    int second = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SEC));

                    int day = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_DAY));
                    int month = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MONTH));
                    int year = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_YEAR));


                    UVReadings uvReadings = new UVReadings(id, uvIndexValue, hour, minute, second, day, month, year);
                    uvList.add(uvReadings);

                } while (cursor.moveToPrevious());
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

    public List<UVReadings> getUVGraphInfo() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //cursor = database.query(Config.UV_TABLE_NAME_GRAPH, null, null, null, Config.COLUMN_ID,  Config.COLUMN_DAY + "=" + "'" + calenderDay + "'" + "' AND " + Config.COLUMN_MONTH + "=" + "'" + calenderMonth + "'" + "' AND " + Config.COLUMN_YEAR + "=" + "'" + calenderYear + "'", Config.COLUMN_HOUR + "' AND " + Config.COLUMN_MIN);
            cursor = database.query(Config.UV_TABLE_NAME_GRAPH, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
            {
                cursor.moveToFirst();
                List<UVReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    long id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));


                    float uvIndexMAX =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_MAX_VALUE_GRAPH));
                    float uvIndexAVG =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_AVERAGE_VALUE_GRAPH));

                    int minute = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MIN));
                    int hour = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_HOUR));
                    int second = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SEC));

                    int day = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_DAY));
                    int month = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MONTH));
                    int year = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_YEAR));


                    UVReadings uvReadings = new UVReadings(id, uvIndexMAX, uvIndexAVG, hour, minute, second, day, month, year);
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

    public List<UVReadings> getUVGraphInfoTable() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.query(Config.UV_TABLE_NAME_GRAPH, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToLast())
            {
                cursor.moveToLast();
                List<UVReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    long id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID));


                    float uvIndexMAX =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_MAX_VALUE_GRAPH));
                    float uvIndexAVG =  cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_UV_AVERAGE_VALUE_GRAPH));

                    int minute = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MIN));
                    int hour = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_HOUR));
                    int second = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_SEC));

                    int day = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_DAY));
                    int month = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_MONTH));
                    int year = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_YEAR));


                    UVReadings uvReadings = new UVReadings(id, uvIndexMAX, uvIndexAVG, hour, minute, second, day, month, year);
                    uvList.add(uvReadings);

                } while (cursor.moveToPrevious());
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