package com.example.notas.data.mapper

import com.example.notas.data.local.NoteEntity
import com.example.notas.domain.model.Note

// Conversión entre la tabla de Room y la entidad del dominio
fun NoteEntity.toDomain() = Note(id = id, title = title, content = content)
fun Note.toEntity() = NoteEntity(id = id, title = title, content = content)
