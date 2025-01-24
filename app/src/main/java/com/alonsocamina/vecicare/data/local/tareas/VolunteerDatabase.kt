package com.alonsocamina.vecicare.data.local.tareas

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VolunteerTask::class], version = 1)
abstract class VolunteerDatabase : RoomDatabase() {
    abstract fun volunteerTaskDao(): VolunteerTaskDao

    companion object {
        @Volatile
        private var INSTANCE : VolunteerDatabase? = null

        fun getDatabase(context : Context) : VolunteerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VolunteerDatabase::class.java,
                    "volunteer_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}