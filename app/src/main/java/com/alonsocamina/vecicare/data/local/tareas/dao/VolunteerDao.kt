package com.alonsocamina.vecicare.data.local.tareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alonsocamina.vecicare.data.local.tareas.entities.Volunteer
import kotlinx.coroutines.flow.Flow

@Dao
interface VolunteerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVolunteer(volunteer: Volunteer)

    @Query("SELECT * FROM volunteers WHERE id = :volunteerId")
    suspend fun getVolunteerById(volunteerId: Int): Volunteer?

    @Query("SELECT * FROM volunteers")
    fun getAllVolunteers(): Flow<List<Volunteer>>

    @Update
    suspend fun updateVolunteer(volunteer: Volunteer)

    @Delete
    suspend fun deleteVolunteer(volunteer: Volunteer)
}