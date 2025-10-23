package com.soto.semana10_servicios.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soto.semana10_servicios.data.model.User
import com.soto.semana10_servicios.viewmodel.UserUiState
import com.soto.semana10_servicios.viewmodel.UserViewModel
import com.soto.semana10_servicios.data.model.Address
import com.soto.semana10_servicios.data.model.Company

/**
 * Pantalla principal que muestra la Lista de usuarios
 * @param viewModel ViewModel que maneja el estado y lógica
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserViewModel = viewModel()
) {
    // Observar el estado del ViewModel
    // collectAsState() convierte el StateFlow en State de Compose
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "👥 Lista de Usuarios",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = { viewModel.loadUsers() },
                        enabled = uiState !is UserUiState.Loading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar usuarios"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Renderizar contenido según el estado actual
            when (uiState) {
                is UserUiState.Loading -> {
                    // Estado: Cargando
                    LoadingContent()
                }
                is UserUiState.Success -> {
                    // Estado: Éxito - mostrar lista
                    val users = (uiState as UserUiState.Success).users
                    UserListContent(users = users)
                }
                is UserUiState.Error -> {
                    // Estado: Error - mostrar mensaje y botón reintentar
                    val message = (uiState as UserUiState.Error).message
                    ErrorContent(
                        message = message,
                        onRetry = { viewModel.loadUsers() }
                    )
                }
            }
        }
    }
}

/**
 * Componente que muestra el estado de carga
 */
@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Cargando usuarios...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Componente que muestra la lista de usuarios en un LazyColumn
 * @param users Lista de usuarios a mostrar
 */
@Composable
fun UserListContent(users: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(users) { user ->
            UserCard(user = user)
        }
    }
}

/**
 * Card que muestra la información de un usuario
 * @param user Usuario a mostrar
 */
@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con nombre e ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "#${user.id}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información de contacto
            ContactInfoRow(
                icon = Icons.Default.Email,
                text = user.email
            )
            ContactInfoRow(
                icon = Icons.Default.Phone,
                text = user.phone
            )
        }
    }
}

/**
 * Componente que muestra una fila de información de contacto
 */
@Composable
fun ContactInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Componente que muestra un mensaje de error con opción de reintentar
 * @param message Mensaje de error a mostrar
 * @param onRetry Callback que se ejecuta al presionar el botón Reintentar
 */
@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de error corregido - usando Warning en lugar de ErrorOutline
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Ups! Algo salió mal",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Reintentar",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reintentar")
        }
    }
}

// =============================================
// PREVIEWS - AGREGAR ESTAS FUNCIONES AL FINAL
// =============================================

/**
 * Preview del estado de carga
 */
@Preview(showBackground = true, name = "Loading State")
@Composable
fun LoadingContentPreview() {
    MaterialTheme {
        LoadingContent()
    }
}

/**
 * Preview de una tarjeta de usuario individual
 */
@Preview(showBackground = true, name = "User Card")
@Composable
fun UserCardPreview() {
    MaterialTheme {
        // Crear un usuario de ejemplo para el preview
        val sampleUser = User(
            id = 1,
            name = "Carlos Rodríguez",
            email = "carlos.rodriguez@email.com",
            phone = "+51 987 654 321",
            username = "carlos_r",
            website = "carlos-rodriguez.com",
            address = Address(
                street = "Av. Arequipa 123",
                suite = "Departamento 4B",
                city = "Lima",
                zipcode = "15001"
            ),
            company = Company(
                name = "Tech Solutions Perú",
                catchPhrase = "Soluciones tecnológicas innovadoras",
                bs = "desarrollo-software"
            )
        )
        UserCard(user = sampleUser)
    } // ← ESTA ES LA LLAVE QUE FALTABA
}

/**
 * Preview del estado de error
 */
@Preview(showBackground = true, name = "Error State")
@Composable
fun ErrorContentPreview() {
    MaterialTheme {
        ErrorContent(
            message = "Error de conexión a internet",
            onRetry = { }
        )
    }
}

/**
 * Preview de la fila de contacto
 */
@Preview(showBackground = true, name = "Contact Row")
@Composable
fun ContactInfoRowPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ContactInfoRow(
                icon = Icons.Default.Email,
                text = "ejemplo@correo.com"
            )
            ContactInfoRow(
                icon = Icons.Default.Phone,
                text = "+1 234 567 8900"
            )
        }
    }
}

/**
 * Preview de la pantalla completa (simulando estado de éxito)
 */
@Preview(showBackground = true, name = "Full Screen - Success", widthDp = 360, heightDp = 640)
@Composable
fun UserListScreenPreview() {
    MaterialTheme {
        // Para previews más complejos podrías necesitar un ViewModel falso
        // Por ahora mostramos componentes individuales
        val sampleUsers = listOf(
            User(
                id = 1,
                name = "Carlos Rodríguez",
                email = "carlos.rodriguez@email.com",
                phone = "+51 987 654 321",
                username = "carlos_r",
                website = "carlos-rodriguez.com",
                address = Address(
                    street = "Av. Arequipa 123",
                    suite = "Departamento 4B",
                    city = "Lima",
                    zipcode = "15001"
                ),
                company = Company(
                    name = "Tech Solutions Perú",
                    catchPhrase = "Soluciones tecnológicas innovadoras",
                    bs = "desarrollo-software"
                )
            ),
            User(
                id = 2,
                name = "María García",
                email = "maria.garcia@email.com",
                phone = "+51 955 123 456",
                username = "maria_g",
                website = "maria-garcia.org",
                address = Address(
                    street = "Jr. Trujillo 456",
                    suite = "Casa 202",
                    city = "Arequipa",
                    zipcode = "04001"
                ),
                company = Company(
                    name = "Consultoría Andina",
                    catchPhrase = "Consultoría de alta calidad",
                    bs = "consultoria-empresarial"
                )
            )
        )
        UserListContent(users = sampleUsers)
    }
}