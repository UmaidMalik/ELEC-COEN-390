package com.elec_coen_390.uvme.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.elec_coen_390.uvme.Model.Course;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

        String CREATE_COURSE_TABLE = "CREATE TABLE " + Config.TABLE_COURSE_NAME + " ("
                + Config.COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_COURSE_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_COURSE_CODE + " TEXT NOT NULL"
                + ")";

        Log.d(TAG, "Table created with this query: " + CREATE_COURSE_TABLE);

        sqLiteDatabase.execSQL(CREATE_COURSE_TABLE);

        Log.d(TAG, "Course table created");



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // ALTER THE DESIGN FOR AN UPDATE

    }


    public long insertCourse(Course course)
    {
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_COURSE_TITLE, course.getTitle());
        contentValues.put(Config.COLUMN_COURSE_CODE, course.getCode());
        try {
            id = db.insertOrThrow(Config.TABLE_COURSE_NAME, null, contentValues);
        } catch(SQLiteException e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            Toast.makeText(context, "Operation Failed: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }

        return id;
    }

    public List<Course> getAllCourses(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {

            cursor = db.query(Config.TABLE_COURSE_NAME, null, null, null, null, null,  null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    List<Course> courseList = new ArrayList<>();

                    do{
                        //getting information from cursor

                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_COURSE_ID));
                        String title = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_TITLE));
                        String code = cursor.getString(cursor.getColumnIndex(Config.COLUMN_COURSE_CODE));
                        //creating a new Course object with the information


                        //adding this course object to courseList

                        courseList.add(new Course(id, title, code));

                    } while(cursor.moveToNext());

                    return courseList;
                }

            }




        }
        catch(Exception e){
            Log.d(TAG, "Exception: " + e.getMessage());
        } finally{
            if(cursor != null)
                cursor.close();

            db.close();
        }
        return Collections.emptyList();
    }
}
