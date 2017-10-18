package edu.orangecoastcollege.cs273.wheretonext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    static final String DATABASE_NAME = "WhereToNext";
    private static final String DATABASE_TABLE = "Colleges";
    private static final int DATABASE_VERSION = 1;


    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLE
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_POPULATION = "population";
    private static final String FIELD_TUITION = "tuition";
    private static final String FIELD_RATING = "rating";
    private static final String FIELD_IMAGE_NAME = "image_name";


    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT, "
                + FIELD_POPULATION + " INTEGER, "
                + FIELD_TUITION + " FLOAT, "
                + FIELD_RATING + " FLOAT, "
                + FIELD_IMAGE_NAME + " TEXT" + ")";
        database.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {

        // 1) Drop existing table
        database.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);

        // 2) Build (Create) a new one
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, GETALL

    public void addCollege(College newCollege) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_NAME, newCollege.getName());
        values.put(FIELD_POPULATION, newCollege.getPopulation());
        values.put(FIELD_TUITION, newCollege.getTuition());
        values.put(FIELD_RATING, newCollege.getRating());
        values.put(FIELD_IMAGE_NAME, newCollege.getImageName());

        long id = db.insert(DATABASE_TABLE, null, values);
        newCollege.setId(id); // set college ID to match the id in SQL db

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public ArrayList<College> getAllColleges() {
        ArrayList<College> collegeList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                DATABASE_TABLE,
                new String[]{KEY_FIELD_ID, FIELD_NAME, FIELD_POPULATION, FIELD_TUITION,
                FIELD_RATING, FIELD_IMAGE_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()){
            do{
                College college = new College(cursor.getInt(0), cursor.getString(1),
                        cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4),
                        cursor.getString(5));

                        collegeList.add(college);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return collegeList;
    }
}
