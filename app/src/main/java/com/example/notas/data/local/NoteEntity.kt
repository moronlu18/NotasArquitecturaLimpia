package com.example.notas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// La tabla de Room. Es distinta de la entidad del dominio (Note)
// para que el dominio no dependa de Room.
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String
)
