package com.soto.semana10_servicios.data.model
/**
 * Modelo de datos que representa un Usuario
 * Esta clase se mapea directamente con el JSON que retorna la API
 */

data class User(
    val id: Int,    // ID único del usuario
    val name: String,    // Nombre completo
    val email: String,    // Correo electrónico
    val phone: String,    // Número de teléfono
    val username: String,           // 🆕 Nombre de usuario
    val website: String,            // 🆕 Sitio web
    val address: Address,           // 🆕 Dirección
    val company: Company            // 🆕 Información de empresa
)

/**
 * Modelo para la dirección del usuario
 */
data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String
)

/**
 * Modelo para la empresa del usuario
 */
data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)