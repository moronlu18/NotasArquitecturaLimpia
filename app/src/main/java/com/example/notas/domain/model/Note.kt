package com.example.notas.domain.model

// Entidad del dominio: qué es una nota para nuestra app.
// Es Kotlin puro: no sabe nada de Room, Compose ni Android.
data class Note(
    val id: Int,
    val title: String,
    val content: String
)
