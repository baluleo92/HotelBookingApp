package com.hotel.bookingapp.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hotel.bookingapp.model.Hotel
import com.hotel.bookingapp.viewmodel.HotelViewModel
import com.hotel.bookingapp.R

@Composable
fun HotelListScreen(viewModel: HotelViewModel) {
    val hotels by viewModel.hotels.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(hotels) { hotel ->
            HotelCard(hotel)
        }
    }
}

@Composable
fun HotelCard(hotel: Hotel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (hotel.thumbnail.isNotEmpty()) {
                val imageUrl = hotel.thumbnail.firstOrNull()

                val painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(id = R.drawable.thumbnail_default) // default image on error
                )

                Image(
                    painter = painter,
                    contentDescription = hotel.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(hotel.title, style = MaterialTheme.typography.titleLarge)
            Text("${hotel.city}, ${hotel.country}")
            Text("Rating: ${hotel.rating}")
            Text("Address: ${hotel.address}")
            hotel.roomsAvailable?.let {
                Text("Room Availability:", style = MaterialTheme.typography.labelMedium)
                it.forEach { (roomType, rooms) ->
                    Text("• Room $roomType: $rooms available")
                }
            }
        }
    }
}
