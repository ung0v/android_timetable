package com.ulan.timetable.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.ulan.timetable.model.Fee;
import com.ulan.timetable.model.Mark;

import com.ulan.timetable.model.Teacher;
import com.ulan.timetable.model.Week;

import java.util.ArrayList;

/**
 * Created by Ulan on 07.09.2018.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 6;

    //tkb table
    private static final String DB_NAME = "timetabledb";
    private static final String TIMETABLE = "timetable";
    private static final String WEEK_ID = "id";
    //    private static final String WEEK_USER_ID = "userid";
    private static final String WEEK_SUBJECT = "subject";
    private static final String WEEK_FRAGMENT = "fragment";
    private static final String WEEK_TEACHER = "teacher";
    private static final String WEEK_ROOM = "room";
    private static final String WEEK_FROM_TIME = "fromtime";
    private static final String WEEK_TO_TIME = "totime";
    private static final String WEEK_COLOR = "color";

    //diem table
    private static final String MARK_TABLE = "marktable";
    private static final String MARK_ID = "id";

    private static final String MARK_SUBJECT_NAME = "s_name";
    private static final String MARK_CREDITS = "credits";
    private static final String MARK_DILIGENCE = "diligence";
    private static final String MARK_MID_TERM = "mid_term";
    private static final String MARK_END_TERM = "end_term";
    private static final String MARK_GRADE = "grade";


    //fee table

    private static final String FEE_TABLE = "fee_table";
    private static final String FEE_ID = "id";
    private static final String FEE_TOTAL = "total";
    private static final String FEE_PAID = "paid";
    private static final String FEE_OVERAGE = "overage";



    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMETABLE = "CREATE TABLE " + TIMETABLE + "("
                + WEEK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + WEEK_USER_ID + "INTEGER,"//add user_id
                + WEEK_SUBJECT + " TEXT,"
                + WEEK_FRAGMENT + " TEXT,"
                + WEEK_TEACHER + " TEXT,"
                + WEEK_ROOM + " TEXT,"
                + WEEK_FROM_TIME + " TEXT,"
                + WEEK_TO_TIME + " TEXT,"
                + WEEK_COLOR + " INTEGER" + ")";

        String CREATE_MARK_TABLE = "CREATE TABLE " + MARK_TABLE + "("
                + MARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MARK_SUBJECT_NAME + " TEXT,"
                + MARK_CREDITS + " TEXT,"
                + MARK_DILIGENCE + " TEXT,"
                + MARK_MID_TERM + " TEXT,"
                + MARK_END_TERM + " TEXT,"
                + MARK_GRADE + " TEXT" + ")";

        String CREATE_FEE_TABLE = "CREATE TABLE " + FEE_TABLE + "("
                + FEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FEE_TOTAL + " TEXT,"
                + FEE_PAID + " TEXT,"
                + FEE_OVERAGE + " TEXT" + ")";


        db.execSQL(CREATE_TIMETABLE);
        db.execSQL(CREATE_MARK_TABLE);
        db.execSQL(CREATE_FEE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE);

            case 2:
                db.execSQL("DROP TABLE IF EXISTS " + MARK_TABLE);

            case 3:
                db.execSQL("DROP TABLE IF EXISTS " + FEE_TABLE);


        }
        onCreate(db);
    }

    /**
     * Methods for Week fragments
     **/
    public void insertWeek(Week week) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
//        contentValues.put(WEEK_USER_ID, week.getUserId());
        contentValues.put(WEEK_FRAGMENT, week.getFragment());
        contentValues.put(WEEK_TEACHER, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        Log.d("color", String.valueOf(week.getColor()));
        db.insert(TIMETABLE, null, contentValues);
        db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public void deleteWeekById(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, WEEK_ID + " = ? ", new String[]{String.valueOf(week.getId())});
        db.close();
    }

    public void updateWeek(Week week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
        contentValues.put(WEEK_TEACHER, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.update(TIMETABLE, contentValues, WEEK_ID + " = " + week.getId(), null);
        db.close();
    }

    public ArrayList<Week> getWeek(String fragment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Week> weeklist = new ArrayList<>();
        Week week;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM " + TIMETABLE + " ) WHERE " + WEEK_FRAGMENT + " LIKE '" + fragment + "%'", null);
        while (cursor.moveToNext()) {
            week = new Week();
            week.setId(cursor.getInt(cursor.getColumnIndex(WEEK_ID)));
            week.setSubject(cursor.getString(cursor.getColumnIndex(WEEK_SUBJECT)));
            week.setTeacher(cursor.getString(cursor.getColumnIndex(WEEK_TEACHER)));
            week.setRoom(cursor.getString(cursor.getColumnIndex(WEEK_ROOM)));
            week.setFromTime(cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME)));
            week.setToTime(cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME)));
            week.setColor(cursor.getInt(cursor.getColumnIndex(WEEK_COLOR)));
            weeklist.add(week);
        }
        return weeklist;
    }

    /**
     * Methods for MARK_TABLE activity
     **/

    public void insertMark(Mark mark) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MARK_SUBJECT_NAME, mark.getS_name());
//        contentValues.put(WEEK_USER_ID, week.getUserId());
        contentValues.put(MARK_CREDITS, mark.getCredits());
        contentValues.put(MARK_DILIGENCE, mark.getDiligence());
        contentValues.put(MARK_MID_TERM, mark.getMid_term());
        contentValues.put(MARK_END_TERM, mark.getEnd_term());
        contentValues.put(MARK_GRADE, mark.getGrade());
//        Log.d("color", String.valueOf(week.getColor()));
        db.insert(MARK_TABLE, null, contentValues);
//        db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public ArrayList<Mark> getMark() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Mark> markList = new ArrayList<>();
        Mark mark;
        Cursor cursor = db.rawQuery(" SELECT * FROM " + MARK_TABLE, null);
        while (cursor.moveToNext()) {
            mark = new Mark();
            mark.setId(cursor.getInt(cursor.getColumnIndex(MARK_ID)));
            mark.setS_name(cursor.getString(cursor.getColumnIndex(MARK_SUBJECT_NAME)));
            mark.setCredits(cursor.getString(cursor.getColumnIndex(MARK_CREDITS)));
            mark.setDiligence(cursor.getString(cursor.getColumnIndex(MARK_DILIGENCE)));
            mark.setMid_term(cursor.getString(cursor.getColumnIndex(MARK_MID_TERM)));
            mark.setEnd_term(cursor.getString(cursor.getColumnIndex(MARK_END_TERM)));
            mark.setGrade(cursor.getString(cursor.getColumnIndex(MARK_GRADE)));

            markList.add(mark);
        }
        return markList;
    }
    public void insertFee(Fee fee) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FEE_TOTAL, fee.getTotal());
        contentValues.put(FEE_PAID, fee.getPaid());
        contentValues.put(FEE_OVERAGE, fee.getOverage());
        db.insert(FEE_TABLE, null, contentValues);
        db.close();
    }

    public ArrayList<Fee> getFee() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Fee> feeList = new ArrayList<>();
        Fee fee;
        Cursor cursor = db.rawQuery(" SELECT * FROM " + FEE_TABLE, null);
        while (cursor.moveToNext()) {
            fee = new Fee();
            fee.setTotal(cursor.getString(cursor.getColumnIndex(FEE_TOTAL)));
            fee.setPaid(cursor.getString(cursor.getColumnIndex(FEE_PAID)));
            fee.setOverage(cursor.getString(cursor.getColumnIndex(FEE_OVERAGE)));


            feeList.add(fee);
        }
        return feeList;
    }

}
