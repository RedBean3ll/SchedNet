package com.zybooks.calendarappexample;

import static com.zybooks.calendarappexample.event.eventsList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;


public class SQLiteManager extends SQLiteOpenHelper {
    private LocalTime time;
    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "CalendarDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Events";
    private static final String COUNTER = "Counter";



    private static final String EVENT_TITLE = "event";
    private static final String EVENT_DATE = "date";
    private static final String EVENT_TIME = "time";

    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");


    public SQLiteManager(Context context ) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){

        if(sqLiteManager == null){
            sqLiteManager = new SQLiteManager(context);
        }
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(EVENT_TITLE)
                .append(" TEXT, ")
                .append(EVENT_DATE)
                .append(" TEXT, ")
                .append(EVENT_TIME)
                .append(" Text)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {



        }
        public void addEventToDatabase(event event){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(EVENT_TITLE, event.getName());
            contentValues.put(EVENT_DATE, String.valueOf(CalendarUtils.selectedDate));
            LocalTime time = LocalTime.now();
            contentValues.put(EVENT_TIME, CalendarUtils.formattedTime(time));

            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        }

        public void populateEventListArray(){
            eventsList.clear();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

           try( Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
           {
               if(result.getCount() != 0){
                   while(result.moveToNext()){
                       String title = result.getString(1);
                       LocalDate date = LocalDate.parse(result.getString(2));
                       LocalTime time = time();

                       event event = new event(title,date, time);
                       eventsList.add(event);
                   }
               }
           }
        }

    private LocalTime time() {
        time = LocalTime.now();
        return time;
    }

   private String getStringFromDate(Date date){
        if(date == null){
            return null;

        }
        return dateFormat.format(date);
        }

        private Date getDateFromString(String string) {
            try {
                return (Date) dateFormat.parse(string);
            } catch (ParseException | NullPointerException e) {
                return null;
            }
        }
    }

