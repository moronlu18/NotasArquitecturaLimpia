package com.example.notas.domain.usecase

import com.example.notas.domain.repository.NoteRepository

// Caso de uso: obtener una nota a partir de su identificador.
// Lo usa la pantalla de detalle, que recibe el id por la ruta de navegación.
class GetNoteByIdUseCase(
    private val repo: NoteRepository
) {
    operator fun invoke(id: Int) = repo.getNote(id)
}
