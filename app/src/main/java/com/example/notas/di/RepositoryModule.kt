package com.example.notas.di

import com.example.notas.data.local.NoteDao
import com.example.notas.data.repository.NoteRepositoryImpl
import com.example.notas.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Conecta la interfaz del dominio con su implementación:
// quien pida un NoteRepository recibirá un NoteRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository = NoteRepositoryImpl(dao)
}
