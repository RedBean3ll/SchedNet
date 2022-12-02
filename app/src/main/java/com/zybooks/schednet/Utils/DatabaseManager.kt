package com.zybooks.schednet.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.NonNull
import com.zybooks.schednet.Model.*
import java.time.*

class DatabaseManager(context: Context): SQLiteOpenHelper(context, DB_NAME, null, VERSION_NO) {

    companion object {
        private const val VERSION_NO: Int = 12
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
        private const val ACCOUNT_NAME: String = "name"
        private const val DEFAULT_LOGIN: String = "defaultlogin"
        private const val SECURITY_ONE: String = "question"
        private const val SECURITY_TWO: String = "question_two"
        private const val ANSWER_ONE: String = "answer"
        private const val ANSWER_TWO: String = "answer_two"
        private const val DEFAULT_FAVORITE: String = "prefered_favorite"

        //LIST TABLE
        private const val LIST_ID: String = "list"
        private const val LIST_NAME: String = "listtitle"
        private const val LIST_PINNED: String = "ispinned"
        private const val LIST_TOUCH: String = "touchtimestamp"

        //TDO TABLE
        private const val EVENT_ID: String = "id" //PRIMARY KEY
        private const val EVENT_NAME: String = "title"
        private const val EVENT_DESCRIPTION: String = "description"
        private const val EVENT_CREATESTAMP: String = "createstamp"
        private const val EVENT_ISPINNED: String = "priority"
        private const val EVENT_ISCOMPLETED: String = "completion"
        private const val EVENT_SORTPRIORITY: String = "priority_level"

        private const val EVENT_EVENTSTART: String = "datestart"
        private const val CALENDAR_DATE: String = "calendar_date"

        private const val FAVORITE_NAME: String = "favorite_name"
    }


    val TAG = "DatabaseManager"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_USERS (user INTEGER PRIMARY KEY, name TEXT DEFAULT 'NAN' NOT NULL, username TEXT DEFAULT 'NAN' NOT NULL, password TEXT DEFAULT 'NAN' NOT NULL, question TEXT DEFAULT 'NAN' NOT NULL, question_two TEXT DEFAULT 'NAN' NOT NULL, answer TEXT DEFAULT 'NAN' NOT NULL, answer_two TEXT DEFAULT 'NAN' NOT NULL, clean_calendar_frequency INT DEFAULT 3 NOT NULL, clean_todo_frequency INT DEFAULT 4 NOT NULL, defaultlogin INTEGER DEFAULT 0 NOT NULL, calendar_date INTEGER DEFAULT 0 NOT NULL, prefered_favorite INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_LISTS (user INTEGER DEFAULT 0 NOT NULL, list INTEGER PRIMARY KEY, listtitle TEXT DEFAULT 'TITLE' NOT NULL, ispinned INTEGER DEFAULT 0 NOT NULL, touchtimestamp INTEGER DEFAULT -1 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_TODO (user INTEGER DEFAULT 0 NOT NULL, list INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT, description TEXT DEFAULT 0 NOT NULL, createstamp INTEGER DEFAULT 0 NOT NULL, priority INTEGER DEFAULT 0 NOT NULL, completion INTEGER DEFAULT 0 NOT NULL, priority_level INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_CALENDAR (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, title TEXT DEFAULT 'TITLE' NOT NULL, description TEXT DEFAULT '' NOT NULL, datestart INTEGER DEFAULT 0 NOT NULL, createstamp INTEGER DEFAULT 0 NOT NULL, priority INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_FAVORITE_TODO (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, favorite_name DEFAULT 'TEMP TITLE' , title TEXT DEFAULT 'TITLE' NOT NULL, description TEXT DEFAULT '' NOT NULL, touchtimestamp INTEGER DEFAULT 0 NOT NULL, ispinned INTEGER DEFAULT 0 NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS $TBL_FAVORITE_CALENDAR (user INTEGER DEFAULT 0 NOT NULL, id INTEGER PRIMARY KEY, favorite_name DEFAULT 'TEMP TITLE', title TEXT DEFAULT 'TITLE' NOT NULL, description TEXT DEFAULT '' NOT NULL, touchtimestamp INTEGER DEFAULT 0 NOT NULL, ispinned INTEGER DEFAULT 0 NOT NULL)")
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

            return cursor.getInt(cursor.getColumnIndex(USER_ID)) //DB Will ALWAYS have a username and password
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            cursor.close()
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
    fun readAutoLoggedUser(): Int {
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
            cursor.close()
            db.endTransaction()
            db.close()
        }

    }

    fun insertNewUser(@NonNull name: String, @NonNull username: String, @NonNull password: String, @NonNull question: String, @NonNull questionTwo: String, @NonNull answer: String, @NonNull answerTwo: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(ACCOUNT_NAME, name)
        cv.put(USER_NAME, username)
        cv.put(USER_PASSWORD, password)
        cv.put(SECURITY_ONE, question)
        cv.put(SECURITY_TWO, questionTwo)
        cv.put(ANSWER_ONE, answer)
        cv.put(ANSWER_TWO, answerTwo)
        db.insert(TBL_USERS, null, cv)
        db.close()
    }
    //DB (name, username, password, q1, q2, a1, a2)

    @SuppressLint("Range")
    fun readPreferredFavoritePage(id: Int): Boolean {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(DEFAULT_FAVORITE), "$USER_ID=?", arrayOf(id.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return false //IF NO RESULT
            return cursor.getInt(cursor.getColumnIndex(DEFAULT_FAVORITE)) == 1
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return false
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    fun deleteUser(id: Int) {
        val db = writableDatabase
        try {
            db.execSQL("DELETE FROM $TBL_LISTS WHERE $USER_ID='$id'")
            db.execSQL("DELETE FROM $TBL_TODO WHERE $USER_ID='$id'")
            db.execSQL("DELETE FROM $TBL_CALENDAR WHERE $USER_ID='$id'")
            db.execSQL("DELETE FROM $TBL_FAVORITE_TODO WHERE $USER_ID='$id'")
            db.execSQL("DELETE FROM $TBL_FAVORITE_CALENDAR WHERE $USER_ID='$id'")
            db.execSQL("DELETE FROM $TBL_USERS WHERE $USER_ID='$id'")
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed: Delete User")
        } finally {
            db.close()
        }
    }

    fun revokeAutoLogin(id: Int) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(DEFAULT_LOGIN, false)
        db.update(TBL_USERS, cv, "$USER_ID=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun readUserByName(login: String): Int {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(USER_ID), "$USER_NAME=?", arrayOf(login),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return -2
            return cursor.getInt(cursor.getColumnIndex(USER_ID))
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readSecurityQuestion(id: Int): List<String> {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(SECURITY_ONE, SECURITY_TWO), "$USER_ID=?", arrayOf(id.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return listOf("NAN", "NAN")
            return listOf(cursor.getString(cursor.getColumnIndex(SECURITY_ONE)), cursor.getString(cursor.getColumnIndex(SECURITY_TWO)))
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return listOf("NAN", "NAN")
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun checkQuestions(id: Int, answerOne: String, answerTwo: String): Boolean {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(USER_ID), "$ANSWER_ONE=? AND $ANSWER_TWO=?", arrayOf(answerOne, answerTwo),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return false
            do {
                if(id == cursor.getInt(cursor.getColumnIndex(USER_ID))) return true
            } while(cursor.moveToNext())
            return false
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return false
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    fun updatePassword(id: Int, newKey: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(USER_PASSWORD, newKey)
        db.update(TBL_USERS, cv, "$USER_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun checkUserExists(username: String): Boolean {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_USERS, arrayOf(DEFAULT_LOGIN), "$USER_NAME=?", arrayOf(username),null,null,null,null)
        try {
            if(cursor.moveToFirst()) return true
            return false
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return false
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readAccountName(@NonNull id: Int): String {
        val db = readableDatabase
        val cursor = db.query(TBL_USERS, arrayOf(ACCOUNT_NAME), "$USER_ID=?", arrayOf(id.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return "DNE"
            return cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME))
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return "NAN"
        } finally {
            cursor.close()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readLists(@NonNull id: Int): MutableList<ListObject> {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_LISTS, arrayOf(LIST_ID, LIST_NAME, LIST_PINNED, LIST_TOUCH), "$USER_ID=?", arrayOf(id.toString()), null, null, "$LIST_PINNED DESC, $LIST_TOUCH DESC", null)
        try {
            if(cursor.moveToFirst()) {
                val spindle = mutableListOf<ListObject>()
                do {
                    val thread = ListObject(
                    cursor.getInt(cursor.getColumnIndex(LIST_ID)), cursor.getString(cursor.getColumnIndex(LIST_NAME)), cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1)
                    spindle.add(thread)
                } while (cursor.moveToNext())
                return spindle
            }
            return arrayListOf() //IF NO RESULT
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return arrayListOf()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    fun insertList(@NonNull obj: ListObject, @NonNull id: Int): Long {
        val returnValue = System.currentTimeMillis()
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(USER_ID, id)
        cv.put(LIST_NAME, obj.listName)
        cv.put(LIST_PINNED, obj.isPinned)
        cv.put(LIST_TOUCH, returnValue)
        db.insert(TBL_LISTS, null, cv)
        db.close()
        return returnValue
    }

    @SuppressLint("Range")
    fun readNewListId(@NonNull id: Int, @NonNull timestamp: Long): Int {
        val db = this.readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_LISTS, arrayOf(LIST_ID), "$USER_ID=? AND $LIST_TOUCH=?", arrayOf(id.toString(), timestamp.toString()), null, null, null, null)
        try {
            if(cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(LIST_ID))
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
        }
        finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
        return -1
    }

    @SuppressLint("Range")
    fun readListName(listId: Int): String {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_LISTS, arrayOf(LIST_NAME), "$LIST_ID=?", arrayOf(listId.toString()),null,null,null)
        try {
            if(cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(LIST_NAME))
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return "NAN"
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
        return "NAN"
    }

    fun updateListName(listId: Int, listName: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(LIST_NAME, listName)
        cv.put(LIST_TOUCH, System.currentTimeMillis())
        db.update(TBL_LISTS, cv, "$LIST_ID=?", arrayOf(listId.toString()))
        db.close()
    }

    fun updateListPinValue(listId: Int, isPinned: Boolean) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(LIST_PINNED, isPinned)
        cv.put(LIST_TOUCH, System.currentTimeMillis())
        db.update(TBL_LISTS, cv, "$LIST_ID=?", arrayOf(listId.toString()))
        db.close()
    }

    //WARNING!! USING WILD SQL
    @SuppressLint("Range")
    fun deleteList(listNumber: Int): Boolean {
        val db = writableDatabase
        try {
            db.execSQL("DELETE FROM $TBL_TODO WHERE $LIST_ID='$listNumber'")
            return db.delete(TBL_LISTS, "$LIST_ID=?", arrayOf(listNumber.toString())) > 0
        } catch (e: Exception) {
            Log.i(TAG, "FAILED TO DELETE LIST OR DATA")
        }
        finally {
            db.close()
        }
        return false
    }

    fun insertTodo(id: Int, listId: Int, obj: TodoObject) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(USER_ID, id)
        cv.put(LIST_ID, listId)
        cv.put(EVENT_NAME, obj.eventName)
        cv.put(EVENT_DESCRIPTION, obj.eventDescription)
        cv.put(EVENT_ISPINNED, obj.isPinned)
        cv.put(EVENT_CREATESTAMP, obj.eventTimestamp)

        var priorityLevel = 0
        if(obj.isPinned) priorityLevel = 1
        if(obj.isChecked) priorityLevel = -1

        cv.put(EVENT_SORTPRIORITY, priorityLevel)
        db.insert(TBL_TODO, null, cv)
        db.close()
    }

    @SuppressLint("Range")
    fun readNewTodoId(id: Int, listId: Int, timestamp: Long): Int {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_TODO, arrayOf(EVENT_ID), "$USER_ID=? AND $LIST_ID=? AND $EVENT_CREATESTAMP=?", arrayOf(id.toString(), listId.toString(), timestamp.toString()),null,null,null,null)
        try {
            if(cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(EVENT_ID))
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
        return -1
    }

    fun deleteTodo(todoNumber: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TBL_TODO, "$EVENT_ID=?", arrayOf(todoNumber.toString())) > 0
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun readTodos(id: Int, listId: Int): MutableList<TodoObject> {
        val db = readableDatabase
        db.beginTransaction()
        val cursor = db.query(TBL_TODO, arrayOf(EVENT_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_ISPINNED, EVENT_ISCOMPLETED, EVENT_CREATESTAMP, EVENT_SORTPRIORITY), "$USER_ID=? AND $LIST_ID=?", arrayOf(id.toString(), listId.toString()),null,null,"$EVENT_SORTPRIORITY DESC, $EVENT_CREATESTAMP DESC", null)
        try{
            if(cursor.moveToFirst()) {
                val todoList = mutableListOf<TodoObject>()
                do {
                    val todoEvent = TodoObject(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(EVENT_ISCOMPLETED)) == 1, cursor.getInt(cursor.getColumnIndex(EVENT_ISPINNED)) == 1, cursor.getLong(cursor.getColumnIndex(EVENT_CREATESTAMP)))
                    todoList.add(todoEvent)
                } while(cursor.moveToNext())
                return todoList
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
        return mutableListOf()
    }

    fun updateTodoPinValue(todoId: Int, event: TodoObject): Long {
        val returnValue: Long = System.currentTimeMillis()
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(EVENT_ISPINNED, event.isPinned)
        cv.put(EVENT_CREATESTAMP, returnValue)

        var priority = 0
        if(event.isPinned) priority = 1
        if(event.isChecked) priority = -1
        cv.put(EVENT_SORTPRIORITY, priority)

        db.update(TBL_TODO, cv, "$EVENT_ID=?", arrayOf(todoId.toString()))
        db.close()

        return returnValue
    }

    fun updateTodoCheckboxValue(todoId: Int, event: TodoObject): Long {
        val returnValue: Long = System.currentTimeMillis()
        val db = readableDatabase
        val cv = ContentValues()
        cv.put(EVENT_ISCOMPLETED, event.isChecked)
        cv.put(EVENT_CREATESTAMP, returnValue)

        var priority = 0
        if(event.isPinned) priority = 1
        if(event.isChecked) priority = -1
        Log.i("clickTest", "priority value: $priority")
        cv.put(EVENT_SORTPRIORITY, priority)

        db.update(TBL_TODO, cv, "$EVENT_ID=?", arrayOf(todoId.toString()))
        db.close()

        return returnValue
    }

    fun editTodo(event: TodoObject) { //TODO: Edit Todo Transaction
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(EVENT_NAME, event.eventName)
        cv.put(EVENT_DESCRIPTION, event.eventDescription)
        cv.put(EVENT_ISPINNED, event.isPinned)
        cv.put(EVENT_CREATESTAMP, System.currentTimeMillis())

        var priority = 0
        if(event.isPinned) priority = 1
        if(event.isChecked) priority = -1
        cv.put(EVENT_SORTPRIORITY, priority)
        db.update(TBL_TODO, cv, "$EVENT_ID=?", arrayOf(event.eventId.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun readIndividualTodo(eventId: Int): TodoObject {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_TODO, arrayOf(EVENT_NAME, EVENT_DESCRIPTION, EVENT_ISPINNED, EVENT_ISCOMPLETED), "$EVENT_ID=?", arrayOf(eventId.toString()),null,null,null,null)
        try {
            if(cursor.moveToFirst()) {
                val event = TodoObject(-1, cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(EVENT_ISCOMPLETED)) == 1, cursor.getInt(cursor.getColumnIndex(EVENT_ISPINNED)) == 1, null)
                return event
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }

        return TodoObject(-1,null, null,null,null,null)
    }

    fun insertCalendarEvent(id: Int, calendarEvent: CalendarDataObject) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(USER_ID, id)
        cv.put(EVENT_NAME, calendarEvent.eventName)
        cv.put(EVENT_DESCRIPTION, calendarEvent.eventDescription)
        cv.put(EVENT_EVENTSTART, calendarEvent.eventDateTime)
        db.insert(TBL_CALENDAR, null, cv)
        db.close()
    }

    //readCalendarDay(), readCalendarEvents()
    fun deleteCalendarEvent(eventId: Int) {
        val db = writableDatabase

        try {
            db.execSQL("DELETE FROM $TBL_CALENDAR WHERE $EVENT_ID='$eventId'")
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
        } finally {
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readUpcomingCalendarEvents(id: Int): ArrayList<CalendarEvent> {
        var date = LocalDate.now()
        var dateTime: ZonedDateTime = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, 0, 0, 0, 0).atZone(ZoneId.systemDefault())

        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_CALENDAR, arrayOf(EVENT_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_EVENTSTART), "$USER_ID=? AND $EVENT_EVENTSTART>?", arrayOf(id.toString(), dateTime.toInstant().toEpochMilli().toString()),null,null, "$EVENT_EVENTSTART ASC", null)
        try {
            if(cursor.moveToFirst()) {
                val eventList = ArrayList<CalendarEvent>()

                do {
                    val obj = CalendarEvent(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getLong(cursor.getColumnIndex(EVENT_EVENTSTART)))
                    eventList.add(obj)
                } while(cursor.moveToNext())

                return eventList
            }

        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return ArrayList()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }

        return ArrayList()
    }

    @SuppressLint("Range")
    fun readDayEvents(id: Int, timestamp: Long): ArrayList<CalendarEvent> { //USE datetime at very start of day
        val endDay = timestamp + 86_400_000

        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_CALENDAR, arrayOf(EVENT_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_EVENTSTART), "$USER_ID=? AND $EVENT_EVENTSTART>=? and $EVENT_EVENTSTART<?", arrayOf(id.toString(), timestamp.toString(), endDay.toString()), null,null, "$EVENT_EVENTSTART ASC", null)
        try {
            if(cursor.moveToFirst()) {
                val eventList = ArrayList<CalendarEvent>()
                do {
                    val obj = CalendarEvent(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getLong(cursor.getColumnIndex(EVENT_EVENTSTART)))
                    eventList.add(obj)
                } while(cursor.moveToNext())
                return eventList
            }
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return ArrayList()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }

        return ArrayList()
    }

    @SuppressLint("Range")
    fun readCalendarEventSingle(eventId: Int): CalendarEvent {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_CALENDAR, arrayOf(EVENT_ID, EVENT_NAME, EVENT_DESCRIPTION, EVENT_EVENTSTART), "$EVENT_ID=?", arrayOf(eventId.toString()),null,null,null,null)
        try{
            if(cursor.moveToFirst()) {
                val obj = CalendarEvent(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getLong(cursor.getColumnIndex(EVENT_EVENTSTART)))
                return obj
            }
        } catch (e: Exception) {
            Log.i(TAG, "Query Failed")
            return CalendarEvent(-1, null, null, null)
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }

        return CalendarEvent(-1, null, null, null)
    }

    fun updateCalendarEvent(obj: CalendarEvent) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(EVENT_NAME, obj.eventName)
        cv.put(EVENT_DESCRIPTION, obj.eventDescription)
        cv.put(EVENT_EVENTSTART, obj.eventDateTime)
        db.update(TBL_CALENDAR, cv, "$USER_ID=? AND $EVENT_ID=?", arrayOf(obj.eventId.toString(), obj.eventId.toString()))
        db.close()
    }

    fun resetCalendarDate(id: Int) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(CALENDAR_DATE, 0)
        db.update(TBL_USERS, cv, "$USER_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun updateCalendarDate(id: Int, timestamp: Long) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(CALENDAR_DATE, timestamp)
        db.update(TBL_USERS, cv, "$USER_ID=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun readCalendarDate(id: Int): Long {
        val db = readableDatabase

        val cursor = db.query(TBL_USERS, arrayOf(CALENDAR_DATE), "$USER_ID=?", arrayOf(id.toString()),null,null,null,null)
        try {
            if(cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(CALENDAR_DATE))
            }
        } catch(e: Exception) {
            return -1
        } finally {
            cursor.close()
            db.close()
        }
        return -2
    }

    @SuppressLint("Range")
    fun readMonthlyEventTimestamp(id: Int, startTimestamp: Long, daysInMonth: Int): ArrayList<ZonedDateTime> {
        val milliInDay: Long = 86_400_000
        val endStamp = startTimestamp + (milliInDay * daysInMonth)

        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_CALENDAR, arrayOf(EVENT_EVENTSTART), "$USER_ID=? AND $EVENT_EVENTSTART>=? AND $EVENT_EVENTSTART<?", arrayOf(id.toString(), startTimestamp.toString(), endStamp.toString()),null,null,"$EVENT_EVENTSTART ASC",null)
        try {
            if(cursor.moveToFirst()) {
                val stampList = ArrayList<ZonedDateTime>()
                do {
                    stampList.add(ZonedDateTime.ofInstant(Instant.ofEpochMilli(cursor.getLong(cursor.getColumnIndex(EVENT_EVENTSTART))), ZoneId.systemDefault()))
                } while(cursor.moveToNext())
                return stampList
            }
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return ArrayList()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
        return ArrayList()
    }

    fun insertCalendarProfile(id: Int, profileName: String, eventName: String, eventDescription: String, isPinned: Boolean): Long { //id, prof name, name, description, isPinned
        val returnValue = System.currentTimeMillis()
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(USER_ID, id)
        cv.put(FAVORITE_NAME, profileName)
        cv.put(EVENT_NAME, eventName)
        cv.put(EVENT_DESCRIPTION, eventDescription)
        cv.put(LIST_PINNED, isPinned)
        cv.put(LIST_TOUCH, returnValue)
        db.insert(TBL_FAVORITE_CALENDAR, null, cv)
        db.close()
        return returnValue
    }

    fun insertTodoProfile(id: Int, profileName: String, eventName: String, eventDescription: String, isPinned: Boolean): Long {
        val returnValue = System.currentTimeMillis()
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(USER_ID, id)
        cv.put(FAVORITE_NAME, profileName)
        cv.put(EVENT_NAME, eventName)
        cv.put(EVENT_DESCRIPTION, eventDescription)
        cv.put(LIST_PINNED, isPinned)
        cv.put(LIST_TOUCH, returnValue)
        db.insert(TBL_FAVORITE_TODO, null, cv)
        db.close()
        return returnValue
    }

    @SuppressLint("Range")
    fun readNewCalendarProfileId(timestamp: Long): Int {
        val db = readableDatabase
        db.beginTransaction()
        val cursor = db.query(TBL_FAVORITE_CALENDAR, arrayOf(EVENT_ID), "$LIST_TOUCH=?", arrayOf(timestamp.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return -2
            return cursor.getInt(cursor.getColumnIndex(EVENT_ID))
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readNewTodoProfileId(timestamp: Long): Int {
        val db = readableDatabase
        db.beginTransaction()
        val cursor = db.query(TBL_FAVORITE_TODO, arrayOf(EVENT_ID), "$LIST_TOUCH=?", arrayOf(timestamp.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return -2
            return cursor.getInt(cursor.getColumnIndex(EVENT_ID))
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return -1
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readCalendarProfile(id: Int): MutableList<FavoriteObject> {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_FAVORITE_CALENDAR, arrayOf(EVENT_ID, FAVORITE_NAME, LIST_PINNED), "$USER_ID=?", arrayOf(id.toString()),null,null,"$LIST_PINNED DESC, $LIST_TOUCH DESC", null)
        try {
            if(!cursor.moveToFirst()) return mutableListOf()
            val list = mutableListOf<FavoriteObject>()
            do {
                list.add(FavoriteObject(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(FAVORITE_NAME)), cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1, true))
            } while (cursor.moveToNext())
            return  list
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return mutableListOf()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readTodoProfile(id: Int): MutableList<FavoriteObject> {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_FAVORITE_TODO, arrayOf(EVENT_ID, FAVORITE_NAME, LIST_PINNED), "$USER_ID=?", arrayOf(id.toString()),null,null,"$LIST_PINNED DESC, $LIST_TOUCH DESC", null)
        try {
            if(!cursor.moveToFirst()) return mutableListOf()
            val list = mutableListOf<FavoriteObject>()
            do {
                list.add(FavoriteObject(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(FAVORITE_NAME)), cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1, false))
            } while(cursor.moveToNext())
            return list
        } catch (e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return mutableListOf()
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    fun deleteFavoriteProfile(favoriteId: Int, profileType: Boolean) {
        val db = writableDatabase
        if(profileType) db.delete(TBL_FAVORITE_CALENDAR, "$EVENT_ID=?", arrayOf(favoriteId.toString()))
        else db.delete(TBL_FAVORITE_TODO, "$EVENT_ID=?", arrayOf(favoriteId.toString()))
        db.close()
    }

    //pinAction [updates pin value and touch stamp]
    fun updateFavoriteProfilePinValue(favoriteId: Int , profileType: Boolean, isPinned: Boolean) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(LIST_PINNED, isPinned)
        cv.put(LIST_TOUCH, System.currentTimeMillis())
        if(profileType) db.update(TBL_FAVORITE_CALENDAR, cv, "$EVENT_ID=?", arrayOf(favoriteId.toString()))
        else db.update(TBL_FAVORITE_TODO, cv, "$EVENT_ID=?", arrayOf(favoriteId.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun readFavoriteCalendarSingle(favoriteId: Int): FavoriteDataObject {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_FAVORITE_CALENDAR, arrayOf(EVENT_ID, FAVORITE_NAME, EVENT_NAME, EVENT_DESCRIPTION, LIST_PINNED), "$EVENT_ID=?", arrayOf(favoriteId.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return FavoriteDataObject(-2, null, null, null, null)
            return FavoriteDataObject(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(FAVORITE_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1)
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return FavoriteDataObject(-1, null, null, null, null)
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun readFavoriteTodoSingle(favoriteId: Int): FavoriteDataObject {
        val db = readableDatabase
        db.beginTransaction()

        val cursor = db.query(TBL_FAVORITE_TODO, arrayOf(EVENT_ID, FAVORITE_NAME, EVENT_NAME, EVENT_DESCRIPTION, LIST_PINNED), "$EVENT_ID=?", arrayOf(favoriteId.toString()),null,null,null,null)
        try {
            if(!cursor.moveToFirst()) return FavoriteDataObject(-2, null, null, null, null)
            return FavoriteDataObject(cursor.getInt(cursor.getColumnIndex(EVENT_ID)), cursor.getString(cursor.getColumnIndex(FAVORITE_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_NAME)), cursor.getString(cursor.getColumnIndex(EVENT_DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(LIST_PINNED)) == 1)
        } catch(e: Exception) {
            Log.i(TAG, "Transaction Failed")
            return FavoriteDataObject(-1, null, null, null, null)
        } finally {
            cursor.close()
            db.endTransaction()
            db.close()
        }
    }

    fun updateFavoriteProfile(profileType: Boolean, favoriteData: FavoriteDataObject) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(FAVORITE_NAME, favoriteData.favoriteName)
        cv.put(EVENT_NAME, favoriteData.eventName)
        cv.put(EVENT_DESCRIPTION, favoriteData.eventDescription)
        cv.put(LIST_PINNED, favoriteData.isPinned)
        cv.put(LIST_TOUCH, System.currentTimeMillis())
        if(profileType) db.update(TBL_FAVORITE_CALENDAR, cv, "$EVENT_ID=?", arrayOf(favoriteData.favoriteId.toString()))
        else db.update(TBL_FAVORITE_TODO, cv, "$EVENT_ID=?", arrayOf(favoriteData.favoriteId.toString()))
    }
}
