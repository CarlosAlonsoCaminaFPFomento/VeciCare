package com.alonsocamina.vecicare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alonsocamina.vecicare.data.local.tareas.dao.*
import com.alonsocamina.vecicare.data.local.tareas.entities.*

@Database(
    entities = [Beneficiary::class, Volunteer::class, Task::class, TaskHistory::class],
    version = 3,
    exportSchema = true
)
abstract class VeciCareDatabase : RoomDatabase() {
    // DAOs
    abstract fun beneficiaryDao(): BeneficiaryDao
    abstract fun volunteerDao(): VolunteerDao
    abstract fun taskDao(): TaskDao
    abstract fun taskHistoryDao(): TaskHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: VeciCareDatabase? = null

        fun getDatabase(context: Context): VeciCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VeciCareDatabase::class.java,
                    "vecicare_database"
                )
                    // Habilitamos migraciones destructivas durante el desarrollo
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
