package com.hotel.bookingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hotel.bookingapp.composable.HotelListScreen
import com.hotel.bookingapp.ui.theme.HotelBookingAppTheme
import com.hotel.bookingapp.viewmodel.HotelViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optional: Use edge-to-edge, but add insets in Compose UI
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val repository = HotelRepository(this)

        setContent {
            val viewModel: HotelViewModel = viewModel(
                factory = HotelViewModelFactory(repository)
            )

            // Surface prevents double insets, use WindowInsets for Compose
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                HotelBookingAppTheme {
                    HotelListScreen(viewModel)
                }

            }
        }
    }
}