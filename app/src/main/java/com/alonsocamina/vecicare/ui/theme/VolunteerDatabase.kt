package com.alonsocamina.vecicare.ui.theme

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VolunteerTask::class], version = 1)
abstract class VolunteerDatabase : RoomDatabase() {
    abstract fun volunteerTaskDao(): VolunteerTaskDao
}