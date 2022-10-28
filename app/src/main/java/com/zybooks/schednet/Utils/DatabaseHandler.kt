package com.zybooks.schednet.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoModelFull
import java.lang.Exception

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {
        //DATABASE
        private val VERSION: Int = 1
        private val DB_NAME: String = "BeeMovie"

        //TABLES ----------------------------------------------
        //USERS
        private val DB_USERS: String = "Users"
        //CALENDAR
        private val DB_CALMAIN: String = "CalendarMain"
        //TODO, TODO HUB
        private val DB_LISTS: String = "TodoLists" //HUB
        private val DB_TODOMAIN: String = "TodoMain"
        //FAVORITE
        private val DB_FAVORITETODO: String = "FavoriteTodo"
        private val DB_FAVORITECAL: String = "FavoriteCalendar"
        //ACTIVE REMINDER
        private val DB_ACTIVEREMINDERS: String = "ActiveReminders"

        //ELEMENTS ----------------------------------------------
        //USERS
        private val USER_ID: String = "user" //PRIMARY KEY
        private val USER_NAME: String = "username" //NOT EMAIL
        private val USER_PASSWORD: String = "password"
        private val DEFAULT_LOGIN: String = "defaultlogin"
        //SETTINGS LIST: FREQCLEAR_CALENDAR & FREQCLEAR_TODO (INSTANT, DAY, MONTH, YEAR),

        //HUB
        //HAS USE_ID
        private val LIST_ID: String = "list"
        private val LIST_NAME: String = "listtitle"

        //TODO, CAL, FAVORITE (IN FAV AS TEMPLATE)
        //HAS USER_ID
        private val EVENT_ID: String = "id" //PRIMARY KEY
        private val EVENT_NAME: String = "title"
        private val EVENT_DESCRIPTION: String = "description"
        private val EVENT_ALLDAY: String = "allday" //IS ALL DAY?
        private val EVENT_EVENTSTART: String = "datestart" //START TIME (EVENT TIME IF ALL DAY) <<NOTE: ONLY TIME IN FAVORITES>>
        private val EVENT_EVENTEND: String = "dateend" //END TIME (MAY NOT EXIST)
        private val EVENT_REPEAT: String = "repeat" //IS REPEATING? <<IF REPEATING, DO NO DELETE
        private val EVENT_REMINDNUMBER: String = "remind" //IS REMIND?
        private val EVENT_REMINDONE: String = "remindone"
        private val EVENT_REMINDTWO: String = "remindtwo"
        private val EVENT_REPEATTYPE: String = "repeattype" //DAILY, WEEKLY, MONTHLY
        private val EVENT_STATUSTIMESTAMP: String = "status" //<<IF STAMP IS SPECIAL STRING THEN DO NOT SHOW

        //TODO, CAL
        private val EVENT_STATUS: String = "status" //SELECTED OR TIME PASSED
        private val EVENT_REPEATTIME: String = "repeattime" //<<DAY OF WEEK (EG: MON or TUES) AND WEEK OF MONTH FOR WEEKLY AND MONTHLY>>

        //TODO (HAS LIST_ID)

        //CAL, FAVORITE CAL
        private val CALEVENTLOCATION: String = "eventlocation"

        //ACTIVE REMINDS
        private val REMIND_ID: String = "remind" //PRIMARY KEY
        //HAS EVENT_ID BUT NOT PRIMARY KEY
        private val REMIND_SOURCE: String = "source" //CAL OR TODO
        private val REMIND_TIME: String = "remindtime"

        //COMMANDS ----------------------------------------------
        private val CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS 'Users' ('user' INTEGER PRIMARY KEY, 'username' TEXT DEFAULT 'EMERGENCY' NOT NULL, 'password' TEXT DEFAULT 'EMERGENCY' NOT NULL, 'frequencycleancal' TEXT DEFAULT 'week' NOT NULL, 'frequencycleantdo' TEXT DEFAULT 'month' NOT NULL, 'defaultlogin' INTEGER DEFAULT 0 NOT NULL, 'loggedin' INTEGER DEFAULT 0 NOT NULL)"
        private val CREATE_TABLE_LISTS = "CREATE TABLE IF NOT EXISTS 'TodoLists' ('user' INTEGER DEFAULT 0 NOT NULL, 'list' INTEGER PRIMARY KEY, 'listtitle' TEXT DEFAULT 'TITLE' NOT NULL)"
        private val CREATE_TABLE_TODO = "CREATE TABLE IF NOT EXISTS 'TodoMain' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'list' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT DEFAULT 'TODO NAME' NOT NULL, 'description' TEXT DEFAULT 'TODO DESCRIPTION' NOT NULL, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT DEFAULT '1667174400' NOT NULL, 'dateend' TEXT, 'repeat' INTEGER DEFAULT 0 NOT NULL, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' 0TEXT, 'repeattime' TEXT, 'status' INTEGER DEFAULT 0 NOT NULL, 'statusstamp' TEXT)"
        private val CREATE_TABLE_CALENDAR = "CREATE TABLE IF NOT EXISTS 'CalendarMain' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT DEFAULT 'CAL EVT NAME' NOT NULL, 'eventlocation' TEXT, 'description' TEXT DEFAULT 'CAL DESCRIPTION' NOT NULL, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT DEFAULT '1667174400' NOT NULL, 'dateend' TEXT, 'repeat' INTEGER DEFAULT 0 NOT NULL, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' TEXT, 'repeattime' TEXT, 'status' INTEGER DEFAULT 0 NOT NULL, 'statusstamp' TEXT)"
        private val CREATE_TABLE_FAV_TODO = "CREATE TABLE IF NOT EXISTS 'FavoriteTodo' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT DEFAULT 'FAVORITE NAME' NOT NULL, 'description' TEXT DEFAULT 'FAVORITE DESCRIPTION' NOT NULL, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT DEFAULT '1667174400' NOT NULL, 'dateend' TEXT, 'repeat' INTEGER DEFAULT 0 NOT NULL, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' TEXT, 'repeattime' TEXT)"
        private val CREATE_TABLE_FAV_CALENDAR = "CREATE TABLE IF NOT EXISTS 'FavoriteCalendar' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT DEFAULT 'FAVORITE NAME' NOT NULL, 'eventlocation' TEXT, 'description' TEXT DEFAULT 'FAVORITE DESCRIPTION' NOT NULL, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT DEFAULT '1667174400' NOT NULL, 'dateend' TEXT, 'repeat' INTEGER DEFAULT 0 NOT NULL, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' TEXT, 'repeattime' TEXT)"
        private val CREATE_TABLE_ACTIVEEVENTS = "CREATE TABLE IF NOT EXISTS 'ActiveReminders' ( 'remind' INTEGER PRIMARY KEY, 'id' INTEGER, 'source' TEXT DEFAULT 'NA' NOT NULL, 'remindtime' TEXT DEFAULT 'NA' NOT NULL)"

        private val DROP_TABLE_GLOB = "DROP TABLE IF EXISTS "
    }

        //ENV ITEMS
        private var databaseInvoke: SQLiteDatabase? = null
        private var databaseHelper: SQLiteOpenHelper? = null

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TABLE_USERS)
            db?.execSQL(CREATE_TABLE_LISTS)
            db?.execSQL(CREATE_TABLE_TODO)
            db?.execSQL(CREATE_TABLE_CALENDAR)
            db?.execSQL(CREATE_TABLE_FAV_TODO)
            db?.execSQL(CREATE_TABLE_FAV_CALENDAR)
            db?.execSQL(CREATE_TABLE_ACTIVEEVENTS)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            //TEMP
            db.execSQL(DROP_TABLE_GLOB+ DB_USERS);
            db.execSQL(DROP_TABLE_GLOB+ DB_LISTS);
            db.execSQL(DROP_TABLE_GLOB+ DB_TODOMAIN);
            db.execSQL(DROP_TABLE_GLOB+ DB_CALMAIN);
            db.execSQL(DROP_TABLE_GLOB+ DB_FAVORITETODO);
            db.execSQL(DROP_TABLE_GLOB+ DB_FAVORITECAL);
            db.execSQL(DROP_TABLE_GLOB+ DB_ACTIVEREMINDERS);
            onCreate(db)
            //CREATE TEMP //COPY TO TEMP //DROP ORIGINAL //CREATE NEW //COPY TO NEW
        }

        fun openDatabase() {
            databaseInvoke = this.readableDatabase
        }

        //EVENT OPERATIONS!! NOTE: RETURN NUMBER FOR OPERATION STATUS
        fun insertTaskTodo(tdo: TodoModelFull) {
            var cv: ContentValues = ContentValues()
            cv.put(EVENT_NAME, tdo.TodoName)
            cv.put(EVENT_STATUS, tdo.TodoStatus)
            databaseInvoke?.insert(DB_TODOMAIN, null, cv)
        }

        fun getTaskTodoFull(): TodoModelFull {

            return TodoModelFull() //UPDATE BEFORE USE!!
        }

        @SuppressLint("Range")
        fun getTaskTodoLimited(): List<TodoModel> { //CONTINUE IN MORNING
            var taskList: MutableList<TodoModel> = mutableListOf()

            var cur: Cursor? = null;
            databaseInvoke?.beginTransaction()

                try {
                    cur = databaseInvoke?.query(DB_TODOMAIN, null, null, null, null, null, null)
                    if(cur != null) {
                        if(cur.moveToFirst()) {
                            /*do {
                                var task: TodoModel = TodoModel()
                                task.TodoId = cur.getInt(cur.getColumnIndex(EVENT_ID))
                                task.TodoStatus = cur.getInt(cur.getColumnIndex(EVENT_STATUS))



                            } while() */
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    taskList.add(TodoModel())
                }

            databaseInvoke?.endTransaction()

            return taskList
        }

        fun updateTaskTodo() {

        }

        fun updateTaskTodoLite() {

        }

        /*fun insertTaskCal(cal: CalModel) {

        }*/

        fun updateTodoRibbon(task: TodoModel): Int {

            return 0
        }

        fun removeTodo():Int {
            return 0
        }

        fun pollTodo() {

        }




}