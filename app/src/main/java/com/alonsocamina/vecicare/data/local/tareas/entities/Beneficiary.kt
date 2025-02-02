package com.alonsocamina.vecicare.data.local.tareas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beneficiaries")
data class Beneficiary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //ID único del beneficiario
    val name: String,
    val surname: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val description: String
)