package com.alonsocamina.vecicare.data.local.usuarios

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosContract.UsuarioEntry

class UsuariosRepository(context: Context) {
    private val dbHelper = UsuariosHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    //Función para insertar un usuario
    fun insertUsuario(usuario: Usuario): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(UsuarioEntry.COLUMN_NAME, usuario.name)
            put(UsuarioEntry.COLUMN_SURNAME, usuario.surname)
            put(UsuarioEntry.COLUMN_EMAIL, usuario.email)
            put(UsuarioEntry.COLUMN_PHONE_NUMBER, usuario.phoneNumber)
            put(UsuarioEntry.COLUMN_COUNTRY_CODE, usuario.countryCode)
            put(UsuarioEntry.COLUMN_ADDRESS, usuario.address)
            put(UsuarioEntry.COLUMN_DESCRIPTION, usuario.description)
            put(UsuarioEntry.COLUMN_BIRTH_DATE, usuario.birthDate)
            put(UsuarioEntry.COLUMN_ROLE, usuario.role)
            put(UsuarioEntry.COLUMN_PASSWORD, usuario.password)
        }

        val newRowId = db.insert(UsuarioEntry.TABLE_NAME,null, values)
        db.close()
        return newRowId != -1L
    }

    //Función para consultar usuarios por email
    fun getUsuarioByEmail(email: String): Usuario? {
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            UsuarioEntry.TABLE_NAME,
            null, //Todas las columnas
            "${UsuarioEntry.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        val usuario = if (cursor.moveToFirst()) {
            Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_NAME)),
                surname = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_SURNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_EMAIL)),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_PHONE_NUMBER)),
                countryCode = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_COUNTRY_CODE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_ADDRESS)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_DESCRIPTION)),
                birthDate = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_BIRTH_DATE)),
                role = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_ROLE)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_PASSWORD))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return usuario
    }

    // Función para obtener el id y role de un usuario por email
    fun getUserDetailsByEmail(email: String): Pair<Int, String>? {
        val db = dbHelper.readableDatabase

        val cursor = db.query(
            UsuarioEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID, UsuarioEntry.COLUMN_ROLE), // Solo columnas necesarias
            "${UsuarioEntry.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        val userDetails = if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val role = cursor.getString(cursor.getColumnIndexOrThrow(UsuarioEntry.COLUMN_ROLE))
            id to role
        } else {
            null
        }

        cursor.close()
        db.close()
        return userDetails
    }

    fun validateUser(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase

        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        Log.d("ValidateDebug", "Intentando validar: email=$trimmedEmail, password=$trimmedPassword")

        val cursor = db.query(
            UsuarioEntry.TABLE_NAME,
            arrayOf(UsuarioEntry.COLUMN_EMAIL),
            "${UsuarioEntry.COLUMN_EMAIL} = ? AND ${UsuarioEntry.COLUMN_PASSWORD} = ?",
            arrayOf(trimmedEmail, trimmedPassword),
            null,
            null,
            null
        )

        val isValid = cursor.count > 0
        Log.d("LoginDebug", "Validando usuario: email=$trimmedEmail, password=$trimmedPassword, resultado=$isValid")
        cursor.close()
        return isValid
    }

    fun getAllUsuarios(): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            UsuariosContract.UsuarioEntry.TABLE_NAME,
            null, // Todas las columnas
            null, // Sin cláusula WHERE
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val usuario = Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_NAME)),
                surname = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_SURNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_EMAIL)),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_PHONE_NUMBER)),
                countryCode = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_COUNTRY_CODE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_ADDRESS)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_DESCRIPTION)),
                birthDate = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_BIRTH_DATE)),
                role = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_ROLE)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(UsuariosContract.UsuarioEntry.COLUMN_PASSWORD))
            )
            usuarios.add(usuario)
        }

        cursor.close()
        db.close()

        return usuarios
    }

}