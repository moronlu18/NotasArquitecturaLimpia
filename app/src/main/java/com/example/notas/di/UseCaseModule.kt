package com.example.notas.di

import com.example.notas.domain.repository.NoteRepository
import com.example.notas.domain.usecase.AddNoteUseCase
import com.example.notas.domain.usecase.GetNoteByIdUseCase
import com.example.notas.domain.usecase.GetNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

// Crea los casos de uso. Se hace aquí, y no con anotaciones en las propias clases,
// para que el dominio siga siendo Kotlin puro, sin ninguna librería.
@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddNoteUseCase(repo: NoteRepository) = AddNoteUseCase(repo)

    @Provides
    fun provideGetNotesUseCase(repo: NoteRepository) = GetNotesUseCase(repo)

    @Provides
    fun provideGetNoteByIdUseCase(repo: NoteRepository) = GetNoteByIdUseCase(repo)
}
