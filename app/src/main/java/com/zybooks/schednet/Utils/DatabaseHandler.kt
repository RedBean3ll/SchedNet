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

        //USERS
        private val BD_USERS: String = "Users"

        //CALENDAR
        private val DB_CALMAIN: String = "CalendarMain"
        private val DB_CALREPEATTIMES: String = "CalendarRepeat"
        private val DB_CALREMINDTIMES: String = "CalendarRemind"

        //TODO, TODO HUB
        private val DB_LISTS: String = "TodoLists" //HUB
        private val DB_EVENTMAIN: String = "TodoMain"
        private val DB_EVENTREMINDTIMES: String = "TodoRemind"

        //FAVORITE
        private val DB_FAVORITETODO: String = "FavoriteTodo"
        private val DB_FAVORITECAL: String = "FavoriteCalendar"

        //ELEMENTS ----------------------------------------------
        //USERS
        private val USER_ID: String = "user" //PRIMARY KEY
        private val USER_NAME: String = "username" //NOT EMAIL
        private val USER_PASSWORD: String = "password"
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
        private val EVENT_EVENTSTART: String = "datestart" //START TIME (EVENT TIME IF ALL DAY)
        private val EVENT_EVENTEND: String = "dateend" //END TIME (MAY NOT EXIST)
        private val EVENT_REPEAT: String = "repeat" //IS REPEATING? <<IF REPEATING, DO NO DELETE
        private val EVENT_REMIND: String = "remind" //IS REMIND?
        private val EVENT_REPEATTYPE: String = "repeattype" //DAILY, WEEKLY, MONTHLY
        private val EVENT_STATUSTIMESTAMP: String = "status" //<<IF STAMP IS SPECIAL STRING THEN DO NOT SHOW

        //TODO, CAL
        private val EVENT_STATUS: String = "status" //SELECTED OR TIME PASSED

        //TODO (HAS LIST_ID)

        //CAL, FAVORITE CAL
        private val CALEVENTLOCATION: String = "eventlocation"

        //REMIND TIMES
        private val EVENT_REMINDTIME: String = "remindtime"


        //COMMANDS
        private val CREATE_TABLE_EVENT =
            "CREATE TABLE IF NOT EXISTS " + DB_EVENTMAIN + "("+ USER_ID + " INTEGER, "+ EVENT_ID + " INTEGER PRIMARY KEY, " + EVENT_NAME + " TEXT, " + EVENT_DESCRIPTION + " TEXT, " + EVENT_ALLDAY + " INTEGER, " + EVENT_EVENTSTART + " TEXT, " + EVENT_EVENTEND + " TEXT, " + EVENT_REPEAT + " INTEGER, " + EVENT_REMIND + " INTEGER, " + EVENT_REPEATTYPE + " TEXT)"
        private val CREATE_TABLE_EVENTTIMES =
            "CREATE TABLE IF NOT EXISTS " + DB_EVENTREMINDTIMES + "(" + EVENT_ID + " INTEGER PRIMARY KEY, " + EVENT_REPEATTYPE + " TEXT)"

        private val CREATE_TABLE_ETT = "CREATE TABLE IF NOT EXISTS "+ DB_EVENTMAIN
    }

        //ENV ITEMS
        private var databaseInvoke: SQLiteDatabase? = null
        private var databaseHelper: SQLiteOpenHelper? = null

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TABLE_EVENT)
            db?.execSQL(CREATE_TABLE_EVENTTIMES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            //TEMP
            db.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE_EVENT);
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
            databaseInvoke?.insert(DB_EVENTMAIN, null, cv)
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
                    cur = databaseInvoke?.query(DB_EVENTMAIN, null, null, null, null, null, null)
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