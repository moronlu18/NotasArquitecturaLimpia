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

### Nivel superior del repositorio

```
.
├── app/                      // Módulo Android: código Kotlin y recursos
├── build.gradle.kts          // Script de Gradle raíz (plugins)
├── settings.gradle.kts       // Nombre del proyecto y módulos incluidos
├── gradle.properties         // Opciones de Gradle (JVM, etc.)
├── gradle/libs.versions.toml // Catálogo centralizado de versiones y dependencias
├── gradlew / gradlew.bat     // Wrappers de Gradle (Linux/Windows)
├── gradle/wrapper/           // JAR y propiedades del wrapper
├── local.properties          // Ruta al SDK de Android (local, no se sube)
└── README.md                 // Estos apuntes
```

Dentro del módulo `app` están `src/main/AndroidManifest.xml`, `src/main/res/`
(`values/strings.xml`, `values/themes.xml`) y `src/main/java/com/example/notas/`:

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

## Rutas de navegación

Definidas en `presentation/navigation/NavigationItem.kt` y registradas en
`presentation/navigation/AppNavHost.kt`:

| Ruta | Destino | Cómo se construye |
| --- | --- | --- |
| `notes` | Lista y formulario de notas (pantalla inicial) | `NavigationItem.Notes.route` |
| `details/{itemId}` | Detalle de una nota | `NavigationItem.Details.createRoute(id)` → `details/3` |

El argumento `itemId` viaja en la ruta y Navigation lo copia en el
`SavedStateHandle`; `DetailViewModel` lo lee con
`savedStateHandle.get<Int>("itemId")` para pedir esa nota al repositorio.

## Actualizar (editar) una nota

La app actual solo **crea** y **consulta** notas: el detalle es de solo lectura.
Para añadir la edición, la ruta del código a seguir es esta, de dentro hacia fuera:

1. `domain/repository/NoteRepository.kt`: añadir `suspend fun update(note: Note)`.
2. `data/local/NoteDao.kt`: añadir `@Update suspend fun update(note: NoteEntity)`.
3. `data/repository/NoteRepositoryImpl.kt`: implementar el método con
   `dao.update(note.toEntity())`.
4. `presentation/detail/DetailViewModel.kt`: exponer un evento `onUpdate(...)`
   que llame a `repository.update(...)`.
5. `presentation/detail/DetailScreen.kt`: convertir `DetailScreenContent` en
   campos editables y añadir el botón que dispara ese evento.

La ruta de navegación que ya lleva al detalle (y que se reutilizaría) es
`details/{itemId}`. Los apuntes en sí (este README) se actualizan en:

```
/home/lourdes/pCloudDrive/Clases/JETPACK/4_interfaz_dinamica/ejercicios_apuntes/NotasArquitecturaLimpia-main/README.md
```

y se publican con `git add README.md && git commit -m "docs: ..." && git push`.

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
