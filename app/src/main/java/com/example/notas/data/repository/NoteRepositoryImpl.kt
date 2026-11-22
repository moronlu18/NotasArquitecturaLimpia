package com.example.notas.data.repository

import com.example.notas.data.local.NoteDao
import com.example.notas.data.mapper.toDomain
import com.example.notas.data.mapper.toEntity
import com.example.notas.domain.model.Note
import com.example.notas.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Implementación real del repositorio, con Room.
// Conoce la interfaz del dominio (NoteRepository), pero el dominio no la conoce a ella.
class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    // Room trabaja con NoteEntity y el dominio con Note:
    // el mapper convierte una en otra
    override fun getNotes(): Flow<List<Note>> =
        dao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getNote(id: Int): Flow<Note?> =
        dao.getById(id).map { entity -> entity?.toDomain() }

    override suspend fun insert(note: Note) = dao.insert(note.toEntity())
}
