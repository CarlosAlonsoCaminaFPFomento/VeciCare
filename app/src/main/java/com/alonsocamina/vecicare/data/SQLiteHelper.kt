package com.alonsocamina.vecicare.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "VolunteerTasks.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            )
        """.trimIndent()
        db?.let {
            it.execSQL(createTableQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            it.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(it)
         }
    }

    fun instertTask(name : String) : Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllTasks() : List<String> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME), null, null, null, null, null)
        val tasks = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                tasks.add(getString(getColumnIndexOrThrow(COLUMN_NAME)))
            }
        }
        cursor.close()
        return tasks
    }

    fun updateTask(id: Int, name: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteTask(id : Int) : Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}