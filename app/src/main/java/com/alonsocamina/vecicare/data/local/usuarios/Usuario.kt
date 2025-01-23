package com.alonsocamina.vecicare.data.local.usuarios

data class Usuario(
    val id: Int = 0, //EL ID se genera en automático
    val name: String,
    val surname: String,
    val email: String,
    val countryCode: String,
    val phoneNumber: String,
    val address: String,
    val description: String,
    val birthDate: String,
    val role: String,
    val password: String
)