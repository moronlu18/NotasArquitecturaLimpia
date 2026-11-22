package com.example.notas

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Punto de entrada de Hilt: prepara la inyección de dependencias de toda la app
@HiltAndroidApp
class NotasApplication : Application()
