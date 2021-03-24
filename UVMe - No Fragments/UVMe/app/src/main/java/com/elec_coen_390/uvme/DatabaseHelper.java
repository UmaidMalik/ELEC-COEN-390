package com.elec_coen_390.uvme;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + Config.TABLE_PROFILE_NAME + " ("
                + Config.COLUMN_PROFILE_age + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PROFILE_name + " TEXT NOT NULL, "
                + Config.COLUMN_PROFILE_gender + " TEXT NOT NULL"
                + ")";

        Log.d(TAG, "Table created with this query: " + CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        Log.d(TAG, "Profile table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // ALTER THE DESIGN FOR AN UPDATE
    }
}