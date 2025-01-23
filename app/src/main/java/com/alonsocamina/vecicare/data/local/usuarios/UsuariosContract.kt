package com.alonsocamina.vecicare.data.local.usuarios

import android.provider.BaseColumns

object UsuariosContract {

    //Informaciön de la tabla de usuarios de la app
    object UsuarioEntry : BaseColumns {
        const val TABLE_NAME = "usuarios"
        const val COLUMN_NAME = "nombre"
        const val COLUMN_SURNAME = "apellido"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_COUNTRY_CODE = "codigo_pais"
        const val COLUMN_PHONE_NUMBER = "telefono"
        const val COLUMN_ADDRESS = "direccion"
        const val COLUMN_DESCRIPTION = "descripcion"
        const val COLUMN_BIRTH_DATE = "fecha_nacimiento"
        const val COLUMN_ROLE = "rol"
        const val COLUMN_PASSWORD = "contrasena"
    }

    //Comando SQL para crear la tabla
    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${UsuarioEntry.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "${UsuarioEntry.COLUMN_NAME} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_SURNAME} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_EMAIL} TEXT UNIQUE NOT NULL," +
        "${UsuarioEntry.COLUMN_COUNTRY_CODE} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_PHONE_NUMBER} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_ADDRESS} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_DESCRIPTION} TEXT," +
        "${UsuarioEntry.COLUMN_BIRTH_DATE} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_ROLE} TEXT NOT NULL," +
        "${UsuarioEntry.COLUMN_PASSWORD} TEXT NOT NULL" +
        ")"

    //Comando SQL para borrar la tabla
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UsuarioEntry.TABLE_NAME}"
}



