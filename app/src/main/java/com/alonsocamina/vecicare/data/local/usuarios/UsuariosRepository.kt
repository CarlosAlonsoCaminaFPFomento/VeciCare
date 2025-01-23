package com.alonsocamina.vecicare.data.local.usuarios

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.util.Log
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosContract.UsuarioEntry

class UsuariosRepository(context: Context) {
    private val dbHelper = UsuariosHelper(context)

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
}