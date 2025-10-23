package com.soto.semana10_servicios.data.repository

import com.soto.semana10_servicios.data.model.User
import com.soto.semana10_servicios.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Repositorio que maneja la fuente de datos de usuarios
 * Encapsula la lógica de acceso a datos y configuración de Retrofit
 */
class UserRepository {

    // Configuración de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/") // URL base de la API
        .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON a objetos Kotlin
        .build()

    // Crear instancia del servicio API
    private val apiService = retrofit.create(ApiService::class.java)

    /**
     * Función suspendida que obtiene la Lista de usuarios
     * Puede lanzar excepciones de red que deben ser manejadas
     */
    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}