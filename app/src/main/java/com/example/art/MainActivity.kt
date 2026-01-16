package com.example.art

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.art.ui.screens.ZoomTemplateScreen
import com.example.art.ui.theme.ArtTheme
import com.example.art.viewmodel.ZoomTemplateViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var viewModel: ZoomTemplateViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ZoomTemplateViewModel::class.java]
        
        enableEdgeToEdge()
        setContent {
            ArtTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ZoomTemplateScreen(viewModel = viewModel)
                }
            }
        }
    }
}