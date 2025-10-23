package com.soto.semana10_servicios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soto.semana10_servicios.data.model.User
import com.soto.semana10_servicios.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estados posibles de la UI
 * Sealed class asegura que solo existen estos 3 estados
 */
sealed class UserUiState {
    object Loading : UserUiState()    // Cargando datos
    data class Success(val users: List<User>) : UserUiState() // Datos cargados exitosamente
    data class Error(val message: String) : UserUiState()   // Error al cargar
}

/**
 * ViewModel que maneja la lógica de negocio y el estado de la UI
 * Incluye funcionalidad de búsqueda en tiempo real
 */
class UserViewModel : ViewModel() {

    // Repositorio para obtener los datos
    private val repository = UserRepository()

    // StateFlow privado y mutable (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    private val _searchText = MutableStateFlow("")
    private val _allUsers = mutableListOf<User>()

    // StateFlow público e inmutable (la UI solo puede observarlo)
    val uiState: StateFlow<UserUiState> = _uiState
    val searchText: StateFlow<String> = _searchText

    init {
        // Cargar usuarios automáticamente al crear el ViewModel
        loadUsers()
    }

    /**
     * Función pública para cargar o recargar usuarios
     * Maneja los diferentes estados: Loading, Success, Error
     */
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            try {
                // Intentar obtener usuarios del repositorio
                val users = repository.getUsers()
                _allUsers.clear()
                _allUsers.addAll(users)
                _uiState.value = UserUiState.Success(applySearchFilter(users))
            } catch (e: Exception) {
                // Capturar cualquier error (red, parse, etc.)
                _uiState.value = UserUiState.Error(
                    e.message ?: "Error desconocido al cargar usuarios"
                )
            }
        }
    }

    /**
     * Actualiza el texto de búsqueda y filtra los usuarios
     */
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        filterUsers()
    }

    /**
     * Filtra los usuarios según el texto de búsqueda
     */
    private fun filterUsers() {
        val filteredUsers = applySearchFilter(_allUsers)
        if (_uiState.value is UserUiState.Success) {
            _uiState.update { currentState ->
                if (currentState is UserUiState.Success) {
                    UserUiState.Success(filteredUsers)
                } else {
                    currentState
                }
            }
        }
    }

    /**
     * Aplica el filtro de búsqueda a la lista de usuarios
     */
    private fun applySearchFilter(users: List<User>): List<User> {
        return if (_searchText.value.isBlank()) {
            users
        } else {
            val query = _searchText.value.lowercase()
            users.filter { user ->
                user.name.lowercase().contains(query) ||
                        user.email.lowercase().contains(query) ||
                        user.phone.lowercase().contains(query) ||
                        user.username.lowercase().contains(query) ||
                        user.website.lowercase().contains(query) ||
                        user.address.city.lowercase().contains(query) ||
                        user.company.name.lowercase().contains(query)
            }
        }
    }
}