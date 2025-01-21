package com.alonsocamina.vecicare.data.local.usuarios

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
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
                
            )
        }
    }
}