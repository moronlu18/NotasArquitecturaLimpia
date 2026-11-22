package com.example.notas.domain.repository

import com.example.notas.domain.model.Note
import kotlinx.coroutines.flow.Flow

// El dominio dice QUÉ necesita, no CÓMO se consigue.
// La implementación real está en data/repository/NoteRepositoryImpl.
interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    fun getNote(id: Int): Flow<Note?>
    suspend fun insert(note: Note)
}
