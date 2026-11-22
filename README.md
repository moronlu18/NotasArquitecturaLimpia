# Notas: Arquitectura Limpia con Jetpack Compose

Proyecto de ejemplo de la lección **Arquitectura de Software** del curso *DEINT Jetpack Compose* (moronlu18.com).

Es una app de notas sencilla: se escribe una nota con título y contenido, se guarda en la base de datos del móvil y aparece en la lista. Al pulsar una nota se abre su detalle.

## Qué se practica

- **Arquitectura Limpia**: el código está repartido en las capas `domain`, `data` y `presentation`.
- **Regla de Dependencias**: `domain` es Kotlin puro y no importa nada de las otras capas.
- **MVVM**: cada pantalla tiene su `ViewModel`, que guarda el estado con `mutableStateOf`.
- **Casos de uso** con `operator fun invoke` y una regla de negocio (una nota sin título no se guarda).
- **Repositorio**: interfaz en el dominio e implementación con Room en `data`.
- **Mapper**: `Note` (dominio) frente a `NoteEntity` (tabla de Room).
- **Flow**: la lista se actualiza sola cada vez que cambia la tabla.
- **Navegación** con rutas en una clase sellada (`NavigationItem`) y un argumento (`itemId`) que el `DetailViewModel` lee de su `SavedStateHandle`.
- **Hilt** para la inyección de dependencias.

## Estructura de carpetas

```
app/src/main/java/com/example/notas/
├── NotasApplication.kt          // Clase Application (punto de entrada de Hilt)
├── MainActivity.kt              // Solo llama a setContent { AppNavHost() }
│
├── domain/                      // Dominio: Kotlin puro, sin Android
│   ├── model/Note.kt
│   ├── repository/NoteRepository.kt
│   └── usecase/
│       ├── AddNoteUseCase.kt
│       ├── GetNotesUseCase.kt
│       └── GetNoteByIdUseCase.kt
│
├── data/                        // Datos: implementación con Room
│   ├── local/                   // NoteDao, NoteEntity, NotesDatabase
│   ├── mapper/NoteMapper.kt
│   └── repository/NoteRepositoryImpl.kt
│
├── presentation/                // Pantallas y ViewModels
│   ├── navigation/              // NavigationItem, AppNavHost
│   ├── notes/                   // NotesScreen, NotesViewModel
│   ├── detail/                  // DetailScreen, DetailViewModel
│   ├── components/NoteCard.kt
│   └── theme/
│
└── di/                          // Módulos de Hilt
    ├── DatabaseModule.kt
    ├── RepositoryModule.kt
    └── UseCaseModule.kt
```

## Recorrido de una acción

1. `NotesScreen` llama a `viewModel.onAddNote(title, content)`.
2. `NotesViewModel` ejecuta `addNote(note)`.
3. `AddNoteUseCase` comprueba la regla y llama a `repo.insert(note)`.
4. `NoteRepositoryImpl` guarda la nota con `dao.insert(note.toEntity())`.
5. Room envía la lista nueva por el `Flow` de `dao.getAll()`.
6. El repositorio la convierte con `toDomain()`.
7. `NotesViewModel`, que escucha con `collect`, recibe la lista.
8. Se actualiza el estado `notes` y Compose vuelve a pintar la pantalla.

## Cómo abrirlo

Abre la carpeta del proyecto con Android Studio, espera a que termine la sincronización de Gradle y ejecuta la app en un emulador o en un móvil (Android 7.0 o superior).

### Diferencias con el código de la lección

Para que el código de la lección sea más corto, allí no aparecen las anotaciones de Hilt. En el proyecto:

- Los ViewModels llevan `@HiltViewModel` y `@Inject constructor`, y las pantallas los obtienen con `hiltViewModel()`.
- Los casos de uso se crean en `di/UseCaseModule.kt`, para que el dominio no tenga ninguna anotación.
- `onAddNote` tiene un tercer parámetro opcional, `onSaved`, para vaciar el formulario cuando la nota se guarda, y el ViewModel expone un mensaje de `error`.
