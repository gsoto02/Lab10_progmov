package com.soto.semana10_servicios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.soto.semana10_servicios.screen.UserListScreen
import com.soto.semana10_servicios.ui.theme.Semana10_serviciosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Semana10_serviciosTheme {
                UserListScreen()
            }
        }
    }
}