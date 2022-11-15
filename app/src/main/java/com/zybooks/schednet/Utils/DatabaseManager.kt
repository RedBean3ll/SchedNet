package com.zybooks.schednet.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.database.sqlite.transaction
import com.zybooks.schednet.Model.ListModel

class DatabaseManager(context: Context): SQLiteOpenHelper(context, DB_NAME, null, VERSION_NO) {

    companion object {
        private const val VERSION_NO: Int = 5
        private const val DB_NAME: String = "BeeMovie"

        private const val TBL_USERS = "Users"
        private const val TBL_LISTS = "TodoLists"
        private const val TBL_TODO = "TodoMain"
        private const val TBL_CALENDAR = "CalendarMain"
        private const val TBL_FAVORITE_TODO = "FavoriteTodo"
        private const val TBL_FAVORITE_CALENDAR = "FavoriteCalendar"

        //USERS TABLE
        private const val USER_ID: String = "user" //PRIMARY KEY
        private const val USER_NAME: String = "username" //NOT EMAIL
        private const val USER_PASSWORD: String = "password"
        private const val DEFAULT_LOGIN: String = "defaultlogin"

        //LIST TABLE
        private const val LIST_ID: String = "list"
        private const val LIST_NAME: String = "listtitle"
        private const val LIST_PINNED: String = "ispinned"
        private const val LIST_TOUCH: String = "touchtimestamp"

        //ALL
        private const val SECRET: String = "hush"
    }

    val TAG = "DatabaseManager"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_USERS (user INTEGER PRIMARY KEY, username TEXT DEFAULT 'NAN' NOT NULL, password TEXT DEFAULT 'NAN' NOT NULL, hush TEXT DEFAULT 'drink bleach' NOT NULL, frequencycleancal TEXT DEFAULT 'week' NOT NULL, frequencycleantdo TEXT DEFAULT 'month' NOT NULL, defaultlogin INTEGER DEFAULT 0 NOT NULL, loggedin INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_LISTS (user INTEGER DEFAULT 0 NOT NULL, list INTEGER PRIMARY KEY, listtitle TEXT DEFAULT 'TITLE' NOT NULL, ispinned INTEGER DEFAULT 0 NOT NULL, touchtimestamp INTEGER DEFAULT -1 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_TODO (user INTEGER DEFAULT 0 NOT NULL, list INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT, description TEXT, due TEXT, remindone TEXT, repeattype INTEGER DEFAULT 0 NOT NULL, repeattime TEXT, statusstamp TEXT, createstamp INTEGER, priority INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_CALENDAR (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT, eventlocation TEXT, description TEXT, allday INTEGER DEFAULT 1 NOT NULL, datestart TEXT, dateend TEXT, remind INTEGER DEFAULT 0 NOT NULL, remindone TEXT, remindtwo TEXT, repeattype INTEGER DEFAULT 0 NOT NULL, repeattime TEXT, statusstamp TEXT, createstamp INTEGER)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_FAVORITE_TODO (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT, description TEXT, due TEXT, remindone TEXT, repeattype INTEGER DEFAULT 0 NOT NULL, repeattime TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_FAVORITE_CALENDAR (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT, eventlocation TEXT, description TEXT, allday INTEGER DEFAULT 1 NOT NULL, datestart TEXT, dateend TEXT, remind INTEGER DEFAULT 0 NOT NULL, remindone TEXT, remindtwo TEXT, repeattype INTEGER DEFAULT 0 NOT NULL, repeattime TEXT)")
        /* db.execSQL("CREATE TABLE IF NOT EXISTS ActiveReminders ( remind INTEGER PRIMARY KEY, user INTEGER, id INTEGER, source TEXT, remindtime TEXT)") */
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TBL_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TBL_LISTS")
        db.execSQL("DROP TABLE IF EXISTS $TBL_TODO")
        db.execSQL("DROP TABLE IF EXISTS $TBL_CALENDAR")
        db.execSQL("DROP TABLE IF EXISTS $TBL_FAVORITE_TODO")
        db.execSQL("DROP TABLE IF EXISTS $TBL_FAVORITE_CALENDAR")
        onCreate(db)
    }

    /**
     * Takes both username and password then returns true if entry exists with exact match.
     *
     * Purpose: Check if user can login. Check if user exists. Return id if exists
     */
    //Codes: positive = success, -1 = error / transaction failed, -2 = too many or no result
    @SuppressLint("Range")
    fun readUser(@NonNull username: String, @NonNull password: String): Int {
        val db = this.readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(USER_ID), "$USER_NAME=? AND $USER_PASSWORD=?", arrayOf(username, password), null, null, null)
        try {
            if(!cursor.moveToFirst() && cursor.count != 1) {
                return -2
            }

            //EXPLAIN RESULTS
            //Log.i(TAG, "USERNAME: (req: $username, got: ${cursor.getString(cursor.getColumnIndex(USER_NAME))}) AND PASSWORD: (req: $password, got: ${cursor.getString(cursor.getColumnIndex(USER_PASSWORD))}) ")
            return cursor.getInt(cursor.getColumnIndex(USER_ID)) //DB Will ALWAYS have a username and password
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            db.endTransaction()
            db.close()

        }
    }

    fun updatePreferredLogin(id: Int, rememberMe: Boolean) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(DEFAULT_LOGIN, if(rememberMe) 1 else 0)
        db.update(TBL_USERS, cv, "$USER_ID=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun checkPreferredLogin(): Int {
        val db = this.writableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(USER_ID), "$DEFAULT_LOGIN>?", arrayOf("0"),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) {
                return -2
            }
            if(cursor.count > 1) {
                val cv = ContentValues()
                cv.put(DEFAULT_LOGIN, 0)

                do{
                    db.update(TBL_USERS, cv, "$DEFAULT_LOGIN>?", arrayOf("0"))
                } while (cursor.moveToNext())
                return -3
            }

            return cursor.getInt(cursor.getColumnIndex(USER_ID))
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            db.endTransaction()
            db.close()
        }

    }

    fun insertUser(@NonNull username: String, @NonNull password: String): Boolean {
        if(readUser(username, password) < 0) {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(USER_NAME, username)
            cv.put(USER_PASSWORD, password)
            db.insert(TBL_USERS, null, cv)
            db.close()
            return true
        }
        return false
    }

    @SuppressLint("Range")
    fun readLists(@NonNull id: Int): ArrayList<ListModel> {
        val db = this.readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_LISTS, arrayOf(LIST_ID, LIST_NAME, LIST_PINNED, LIST_TOUCH), "$USER_ID=?", arrayOf(id.toString()), null, null, null, null)
        try {
            if(cursor.moveToFirst()) {
                val spindle = ArrayList<ListModel>()
                do {
                    val thread = ListModel()
                    thread.ListId = cursor.getInt(cursor.getColumnIndex(LIST_ID))
                    thread.ListName = cursor.getString(cursor.getColumnIndex(LIST_NAME))
                    thread.isPinned = if(cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1) true else false
                    thread.timeStamp = cursor.getLong(cursor.getColumnIndex(LIST_TOUCH))
                    spindle.add(thread)
                } while (cursor.moveToNext())
                return spindle
            }
        } catch(e: Exception) {
            Log.i(TAG, "Transaction failed")
            return arrayListOf()
        } finally {
            db.endTransaction()
            db.close()
        }

        return arrayListOf()
    }

    fun insertList(@NonNull obj: ListModel, @NonNull id: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(USER_ID, id)
        cv.put(LIST_NAME, obj.ListName)
        cv.put(LIST_PINNED, obj.isPinned)
        cv.put(LIST_TOUCH, obj.timeStamp)
        db.insert(TBL_LISTS, null, cv)
        db.close()
    }

    @SuppressLint("Range")
    fun readListUpdate(timestamp: Long): ArrayList<ListModel> {
        //take long, find all entries
        val db = this.readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_LISTS, arrayOf(LIST_ID, LIST_NAME, LIST_PINNED, LIST_TOUCH), "$LIST_TOUCH>?", arrayOf(timestamp.toString()),null,null,null,null)
        try {
            if(cursor.moveToFirst()) {
                val spindle = ArrayList<ListModel>()
                do {
                    val thread = ListModel()
                    thread.ListId = cursor.getInt(cursor.getColumnIndex(LIST_ID))
                    thread.ListName = cursor.getString(cursor.getColumnIndex(LIST_NAME))
                    thread.isPinned = if(cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1) true else false
                    thread.timeStamp = cursor.getLong(cursor.getColumnIndex(LIST_TOUCH))
                    spindle.add(thread)
                } while (cursor.moveToNext())
                return spindle
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction failed")
        } finally {
            db.endTransaction()
            db.close()
        }
        return ArrayList()
    }

    fun updateList(obj: ListModel) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(LIST_NAME, obj.ListName)
        cv.put(LIST_PINNED, obj.isPinned)
        cv.put(LIST_TOUCH, obj.timeStamp)
        db.update(TBL_LISTS, cv, "$LIST_ID=?", arrayOf(obj.ListId.toString()))
        db.close()
    }

    fun updateListPinValue(obj: ListModel) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(LIST_PINNED, obj.isPinned)
        cv.put(LIST_TOUCH, obj.timeStamp)
        db.update(TBL_LISTS, cv, "$LIST_ID=?", arrayOf(obj.ListId.toString()))
        db.close()
    }

    fun deleteListItem(listNumber: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TBL_LISTS, "$LIST_ID=?", arrayOf(listNumber.toString())) > 0
    }

    fun insertTestUsers() {
        val db = this.writableDatabase
        db.execSQL("INSERT INTO Users (username, password) VALUES ('admin', 'password')")
        db.execSQL("INSERT INTO Users (username, password) VALUES ('coffee123', 'password123')")
        db.close()
    }




}