# Notas: Arquitectura Limpia con Jetpack Compose

Proyecto de ejemplo de las lecciones **Arquitectura de Software** y **ViewModel y Repositorio** del curso *DEINT Jetpack Compose* (moronlu18.com).

Es una app de notas sencilla: se escribe una nota con título y contenido, se guarda en la base de datos del móvil y aparece en la lista. Al pulsar una nota se abre su detalle.

## Qué se practica

- **Capas**: el código está repartido en `domain`, `data` y `presentation`.
- **Regla de Dependencias**: `domain` es Kotlin puro y no importa nada de las otras capas.
- **MVVM**: cada pantalla tiene su `ViewModel`, que guarda el estado con `mutableStateOf`.
- **State hoisting**: cada pantalla se divide en dos composables: `XScreen` (stateful, obtiene el ViewModel con `hiltViewModel()` y mantiene el estado) y `XScreenContent` (stateless, solo recibe el estado y los eventos, por lo que se puede previsualizar y probar sin ViewModel).
- **Lógica de negocio ligera en el ViewModel**: una nota sin título no se guarda.
- **Repositorio**: interfaz en el dominio e implementación con Room en `data`.
- **Mapper**: `Note` (dominio) frente a `NoteEntity` (tabla de Room).
- **Flow**: la lista se actualiza sola cada vez que cambia la tabla.
- **Navegación** con rutas en una clase sellada (`NavigationItem`) y un argumento (`itemId`) que el `DetailViewModel` lee de su `SavedStateHandle`.
- **Hilt** para la inyección de dependencias.
- **Iconos de Material**: `material-icons-core` y `material-icons-extended` para usar cualquier icono de Material Design.

### Sin casos de uso

La capa de dominio de la Arquitectura Limpia puede incluir casos de uso (`AddNoteUseCase`, `GetNotesUseCase`…), pero según la guía oficial de Android son **opcionales**. En el curso no los usamos: el ViewModel contiene la lógica de negocio ligera y llama directamente al repositorio.

## Estructura de carpetas

```
app/src/main/java/com/example/notas/
├── NotasApplication.kt          // Clase Application (punto de entrada de Hilt)
├── MainActivity.kt              // Solo llama a setContent { AppNavHost() }
│
├── domain/                      // Dominio: Kotlin puro, sin Android
│   ├── model/Note.kt            // Entidad
│   └── repository/NoteRepository.kt   // Interfaz: el «contrato»
│
├── data/                        // Datos: implementación con Room
│   ├── local/                   // NoteDao, NoteEntity, NotesDatabase
│   ├── mapper/NoteMapper.kt
│   └── repository/NoteRepositoryImpl.kt
│
├── presentation/                // Pantallas y ViewModels
│   ├── navigation/              // NavigationItem, AppNavHost
│   ├── notes/                   // NotesScreen + NotesScreenContent, NotesViewModel
│   ├── detail/                  // DetailScreen + DetailScreenContent, DetailViewModel
│   ├── components/NoteCard.kt
│   └── theme/
│
└── di/                          // Módulos de Hilt
    ├── DatabaseModule.kt
    └── RepositoryModule.kt
```

## Recorrido de una acción

1. `NotesScreenContent` avisa con el evento `onAddNote(title, content)`; `NotesScreen` lo reenvía a `viewModel.onAddNote(title, content)` y limpia el formulario si se guarda.
2. `NotesViewModel` comprueba que el título no esté vacío (lógica de negocio ligera).
3. Si es correcto, llama a `repository.insert(note)`.
4. `NoteRepositoryImpl` guarda la nota con `dao.insert(note.toEntity())`.
5. Room envía la lista nueva por el `Flow` de `dao.getAll()`.
6. El repositorio la convierte con `toDomain()`.
7. `NotesViewModel`, que escucha con `collect`, recibe la lista.
8. Se actualiza el estado `notes` y Compose vuelve a pintar la pantalla.

## Cómo abrirlo

Abre la carpeta del proyecto con Android Studio, espera a que termine la sincronización de Gradle y ejecuta la app en un emulador o en un móvil (Android 7.0 o superior).

### Diferencias con el código de las lecciones

Para que el código de las lecciones sea más corto, allí no aparecen las anotaciones de Hilt. En el proyecto:

- Los ViewModels llevan `@HiltViewModel` y `@Inject constructor`, y las pantallas los obtienen con `hiltViewModel()`.
- `onAddNote` tiene un tercer parámetro opcional, `onSaved`, para vaciar el formulario cuando la nota se guarda, y el ViewModel expone un mensaje de `error`.

### Pantallas stateful y stateless

Cada pantalla tiene dos composables en el mismo archivo:

- `NotesScreen` / `DetailScreen`: funciones **stateful**. Obtienen el ViewModel con `hiltViewModel()` y conservan el estado de la UI (por ejemplo, el título y el contenido del formulario con `rememberSaveable`).
- `NotesScreenContent` / `DetailScreenContent`: funciones **stateless**. Reciben el estado (`notes`, `error`, `title`, `content`, `note`) y los eventos (`onAddNote`, `onNoteClick`, `onBack`…) como parámetros, y solo pintan la interfaz. Al no depender de Hilt, se pueden previsualizar y probar fácilmente.
