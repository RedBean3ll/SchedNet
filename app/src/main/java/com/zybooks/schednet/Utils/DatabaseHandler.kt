package com.zybooks.schednet.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zybooks.schednet.Model.TodoModel
import com.zybooks.schednet.Model.TodoModelFull

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {
        //DATABASE
        private const val VERSION: Int = 1
        private const val DB_NAME: String = "BeeMovie"

        //TABLES ----------------------------------------------
        //USERS
        private const val DB_USERS: String = "Users"
        //CALENDAR
        private const val DB_CALMAIN: String = "CalendarMain"
        //TODO, TODO HUB
        private const val DB_LISTS: String = "TodoLists" //HUB
        private const val DB_TODOMAIN: String = "TodoMain"
        //FAVORITE
        private const val DB_FAVORITETODO: String = "FavoriteTodo"
        private const val DB_FAVORITECAL: String = "FavoriteCalendar"
        //ACTIVE REMINDER
        private const val DB_ACTIVEREMINDERS: String = "ActiveReminders"

        //ELEMENTS ----------------------------------------------
        //USERS
        private const val USER_ID: String = "user" //PRIMARY KEY
        private const val USER_NAME: String = "username" //NOT EMAIL
        private const val USER_PASSWORD: String = "password"
        private const val DEFAULT_LOGIN: String = "defaultlogin"
        //SETTINGS LIST: FREQCLEAR_CALENDAR & FREQCLEAR_TODO (INSTANT, DAY, MONTH, YEAR),

        //HUB
        //HAS USE_ID
        private const val LIST_ID: String = "list"
        private const val LIST_NAME: String = "listtitle"

        //TODO, CAL, FAVORITE (IN FAV AS TEMPLATE)
        //HAS USER_ID
        private const val EVENT_ID: String = "id" //PRIMARY KEY
        private const val EVENT_NAME: String = "title"
        private const val EVENT_DESCRIPTION: String = "description"
        private const val EVENT_ALLDAY: String = "allday" //IS ALL DAY?
        private const val EVENT_EVENTSTART: String = "datestart" //START TIME (EVENT TIME IF ALL DAY) <<NOTE: ONLY TIME IN FAVORITES>>
        private const val EVENT_EVENTEND: String = "dateend" //END TIME (MAY NOT EXIST)
        private const val EVENT_REMINDNUMBER: String = "remind" //IS REMIND?
        private const val EVENT_REMINDONE: String = "remindone"
        private const val EVENT_REMINDTWO: String = "remindtwo"
        private const val EVENT_REPEATTYPE: String = "repeattype" //DAILY, WEEKLY, MONTHLY
        private const val EVENT_STATUSTIMESTAMP: String = "statusstamp" //<<IF STAMP IS SPECIAL STRING THEN DO NOT SHOW
        private const val EVENT_CREATESTAMP: String = "createstamp"

        //TODO, CAL
        private const val EVENT_STATUS: String = "status" //SELECTED OR TIME PASSED
        private const val EVENT_REPEATTIME: String = "repeattime" //<<DAY OF WEEK (EG: MON or TUES) AND WEEK OF MONTH FOR WEEKLY AND MONTHLY>>

        //TODO (HAS LIST_ID)
        private const val EVENT_PRIORITYSTATUS: String = "priority"
        private const val EVENT_DUEDATE: String = "due"

        //CAL, FAVORITE CAL
        private const val CALEVENTLOCATION: String = "eventlocation"

        //ACTIVE REMINDS
        private const val REMIND_ID: String = "remind" //PRIMARY KEY
        //HAS EVENT_ID BUT NOT PRIMARY KEY
        private const val REMIND_SOURCE: String = "source" //CAL OR TDO
        private const val REMIND_TIME: String = "remindtime"

        //COMMANDS ----------------------------------------------
        private const val CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS 'Users' ('user' INTEGER PRIMARY KEY, 'username' TEXT DEFAULT 'EMERGENCY' NOT NULL, 'password' TEXT DEFAULT 'EMERGENCY' NOT NULL, 'frequencycleancal' TEXT DEFAULT 'week' NOT NULL, 'frequencycleantdo' TEXT DEFAULT 'month' NOT NULL, 'defaultlogin' INTEGER DEFAULT 0 NOT NULL, 'loggedin' INTEGER DEFAULT 0 NOT NULL)"
        private const val CREATE_TABLE_LISTS = "CREATE TABLE IF NOT EXISTS 'TodoLists' ('user' INTEGER DEFAULT 0 NOT NULL, 'list' INTEGER PRIMARY KEY, 'listtitle' TEXT DEFAULT 'TITLE' NOT NULL)"
        private const val CREATE_TABLE_TODO = "CREATE TABLE IF NOT EXISTS 'TodoMain' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'list' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT, 'description' TEXT, 'due' TEXT, 'remindone' TEXT, 'repeattype' INTEGER DEFAULT 0 NOT NULL, 'repeattime' TEXT, 'statusstamp' TEXT, 'createstamp' INTEGER, 'priority' INTEGER DEFAULT 0 NOT NULL)"
        private const val CREATE_TABLE_CALENDAR = "CREATE TABLE IF NOT EXISTS 'CalendarMain' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT, 'eventlocation' TEXT, 'description' TEXT, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT, 'dateend' TEXT, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' INTEGER DEFAULT 0 NOT NULL, 'repeattime' TEXT, 'statusstamp' TEXT, 'createstamp' INTEGER)"
        private const val CREATE_TABLE_FAV_TODO = "CREATE TABLE IF NOT EXISTS 'FavoriteTodo' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT, 'description' TEXT, 'due' TEXT, 'remindone' TEXT, 'repeattype' INTEGER DEFAULT 0 NOT NULL, 'repeattime' TEXT)"
        private const val CREATE_TABLE_FAV_CALENDAR = "CREATE TABLE IF NOT EXISTS 'FavoriteCalendar' ( 'user' INTEGER DEFAULT 0 NOT NULL, 'id' INTEGER PRIMARY KEY, 'title' TEXT, 'eventlocation' TEXT, 'description' TEXT, 'allday' INTEGER DEFAULT 1 NOT NULL, 'datestart' TEXT, 'dateend' TEXT, 'remind' INTEGER DEFAULT 0 NOT NULL, 'remindone' TEXT, 'remindtwo' TEXT, 'repeattype' INTEGER DEFAULT 0 NOT NULL, 'repeattime' TEXT)"
        private const val CREATE_TABLE_ACTIVEEVENTS = "CREATE TABLE IF NOT EXISTS 'ActiveReminders' ( 'remind' INTEGER PRIMARY KEY, 'user' INTEGER, 'id' INTEGER, 'source' TEXT, 'remindtime' TEXT)"

        private const val DROP_TABLE_GLOB = "DROP TABLE IF EXISTS "
    }

        //ENV ITEMS
        private var databaseInvoke: SQLiteDatabase? = null

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
            db.execSQL(DROP_TABLE_GLOB+ DB_USERS)
            db.execSQL(DROP_TABLE_GLOB+ DB_LISTS)
            db.execSQL(DROP_TABLE_GLOB+ DB_TODOMAIN)
            db.execSQL(DROP_TABLE_GLOB+ DB_CALMAIN)
            db.execSQL(DROP_TABLE_GLOB+ DB_FAVORITETODO)
            db.execSQL(DROP_TABLE_GLOB+ DB_FAVORITECAL)
            db.execSQL(DROP_TABLE_GLOB+ DB_ACTIVEREMINDERS)
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

            var cur: Cursor? = null
            databaseInvoke?.beginTransaction()

                try {
                    cur = databaseInvoke?.query(DB_TODOMAIN, null, null, null, null, null, null)
                    if(cur != null) {
                        if(cur.moveToFirst()) {
                            do {
                                var task: TodoModel = TodoModel()
                                task.TodoId = if(cur.getColumnIndex(EVENT_ID) == -1) 0 else cur.getInt(cur.getColumnIndex(EVENT_ID))
                                task.TodoName = if(cur.getColumnIndex(EVENT_NAME) == -1) "NA" else cur.getString(cur.getColumnIndex(EVENT_NAME))
                                task.TodoPinned = if(cur.getColumnIndex(EVENT_STATUS) == -1) false else (if(cur.getInt(cur.getColumnIndex(EVENT_STATUS)) == 1) true else false)
                                task.TodoPriorityStatus = if(cur.getColumnIndex(EVENT_PRIORITYSTATUS) == -1) false else (if(cur.getInt(cur.getColumnIndex(EVENT_PRIORITYSTATUS)) == 1) true else false)
                                task.TodoCreateStamp = if(cur.getColumnIndex(EVENT_CREATESTAMP) == -1) 0 else cur.getLong(cur.getColumnIndex(EVENT_CREATESTAMP))
                                task.TodoStatusTimestamp = if(cur.getColumnIndex(EVENT_STATUSTIMESTAMP) == -1) "NA" else cur.getString(cur.getColumnIndex(EVENT_STATUSTIMESTAMP))
                            } while(cur.moveToNext())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    taskList.add(TodoModel())
                } finally {
                    databaseInvoke?.endTransaction()
                    databaseInvoke?.close()
                }

            return taskList
        }

        fun updateTaskStatusTodo(id: Int, status: Int) {
            var cv: ContentValues = ContentValues()
            cv.put(EVENT_STATUS, status)
            databaseInvoke?.update(DB_TODOMAIN, cv, EVENT_ID + "=?", arrayOf<String>(id.toString()))
        }

        fun updateTaskNameTodo(id: Int, name: String) {
            var cv: ContentValues = ContentValues()
            cv.put(EVENT_NAME, name)
            databaseInvoke?.update(DB_TODOMAIN, cv, EVENT_ID + "=?", arrayOf<String>(id.toString()))
        }

        fun deleteTaskTodo(id: Int) {
            databaseInvoke?.delete(DB_TODOMAIN, EVENT_ID + "=?", arrayOf<String>(id.toString()))
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