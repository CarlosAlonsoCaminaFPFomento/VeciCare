package com.alonsocamina.vecicare.data.local.usuarios

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuariosHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        //Creamos la tabla cuando se inicializa la base de datos
        if (db != null) {
            db.execSQL(UsuariosContract.SQL_CREATE_ENTRIES)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Si hay una actualización, elimina y vuelve a crear la tabla
        if (db != null) {
            db.execSQL(UsuariosContract.SQL_DELETE_ENTRIES)
        }
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "UsuariosVeciCare.db"
        const val DATABASE_VERSION = 1
    }
}