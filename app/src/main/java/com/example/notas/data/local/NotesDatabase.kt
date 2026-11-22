package com.example.notas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Base de datos de la app: una sola tabla, la de notas
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
