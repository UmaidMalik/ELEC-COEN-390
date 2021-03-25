package com.elec_coen_390.uvme;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    private Context context;
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE THE TABLES
        String CREATE_TABLE_UV_DATA = "CREATE TABLE " + Config.UV_TABLE_NAME +          /** Defining uv data table */
                " (" + Config.COLUMN_UV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Config.COLUMN_DATE + " TEXT NOT NULL," +
                Config.COLUMN_UV_VALUE + " DOUBLE NOT NULL," +
                Config.COLUMN_UV_TIME + " DOUBLE NOT NULL)";

        sqLiteDatabase.execSQL(CREATE_TABLE_UV_DATA);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ALTER THE DESIGN FOR AN UPDATE
    }

    public long insertUV(UvReadings uv){

        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_DATE, uv.getDate() );
        contentValues.put(Config.COLUMN_UV_VALUE, uv.getUv() );
        contentValues.put(Config.COLUMN_UV_TIME, uv.getUvTime() );

        try {
            id = db.insertOrThrow(Config.UV_TABLE_NAME, null, contentValues);
        } catch (SQLException ex) {
            Log.d(TAG, "EXCEPTION: " + ex);

        } finally {
            db.close();
        }
        return id;
    }


    public List<UvReadings> getAllUVData(String date) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.query(Config.UV_TABLE_NAME, null, null, null, Config.COLUMN_UV_ID,  Config.COLUMN_DATE + "=" + "'" + date + "'", Config.COLUMN_UV_TIME);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                List<UvReadings> uvList = new ArrayList<>();
                do {
                    // We get all the parameters
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_UV_ID));
                    double time = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_UV_TIME));
                    double value = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_UV_VALUE));
                    UvReadings uvReadings = new UvReadings(time, value, date);
                    uvList.add(uvReadings);
                } while (cursor.moveToNext());
                return uvList; }


        } catch (SQLException exception) { Log.d(TAG, "EXCEPTION: " + exception);}
            finally {
            if (cursor != null)
                cursor.close();
            database.close();
        }
        return Collections.emptyList(); // Nothing to display
    }
}