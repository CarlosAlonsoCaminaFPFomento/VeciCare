package com.alonsocamina.vecicare.data.local.usuarios

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuariosHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(UsuariosContract.SQL_CREATE_ENTRIES)
        Log.d("UsuariosHelper", "Tabla creada con el comando: ${UsuariosContract.SQL_CREATE_ENTRIES}")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(UsuariosContract.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.d("UsuariosHelper", "Base de datos actualizada de versión $oldVersion a $newVersion")
    }

    suspend fun insertUser(usuario: Usuario): Long {
        return withContext(Dispatchers.IO) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(UsuariosContract.UsuarioEntry.COLUMN_NAME, usuario.name)
                put(UsuariosContract.UsuarioEntry.COLUMN_SURNAME, usuario.surname)
                put(UsuariosContract.UsuarioEntry.COLUMN_EMAIL, usuario.email)
                put(UsuariosContract.UsuarioEntry.COLUMN_COUNTRY_CODE, usuario.countryCode)
                put(UsuariosContract.UsuarioEntry.COLUMN_PHONE_NUMBER, usuario.phoneNumber)
                put(UsuariosContract.UsuarioEntry.COLUMN_ADDRESS, usuario.address)
                put(UsuariosContract.UsuarioEntry.COLUMN_DESCRIPTION, usuario.description)
                put(UsuariosContract.UsuarioEntry.COLUMN_BIRTH_DATE, usuario.birthDate)
                put(UsuariosContract.UsuarioEntry.COLUMN_ROLE, usuario.role)
                put(UsuariosContract.UsuarioEntry.COLUMN_PASSWORD, usuario.password)
            }
            val result = db.insert(UsuariosContract.UsuarioEntry.TABLE_NAME, null, values)
            Log.d("UsuariosHelper", "Usuario insertado: $usuario, Resultado: $result")
            result
        }
    }

    fun isDatabaseOpen(): Boolean {
        return writableDatabase.isOpen
    }

    companion object {
        const val DATABASE_NAME = "UsuariosVeciCare.db"
        const val DATABASE_VERSION = 5
    }
}
