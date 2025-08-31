package com.hotel.bookingapp.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hotel.bookingapp.R
import com.hotel.bookingapp.model.Hotel
import com.hotel.bookingapp.viewmodel.HotelViewModel

@Composable
fun HotelListScreen(viewModel: HotelViewModel) {
    val hotels by viewModel.hotels.collectAsState()
    val isRoomLoading by viewModel.isRoomLoading.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(hotels) { hotel ->
            HotelCard(hotel, isRoomLoading)
        }
    }
}
@Composable
fun HotelCard(
    hotel: Hotel,
    isRoomLoading: Boolean
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (hotel.thumbnail.isNotEmpty()) {
                val painter = rememberAsyncImagePainter(
                    model = hotel.thumbnail.firstOrNull(),
                    error = painterResource(id = R.drawable.thumbnail_default)
                )
                Image(
                    painter = painter,
                    contentDescription = hotel.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(hotel.title, style = MaterialTheme.typography.titleLarge)
            Text("${hotel.city}, ${hotel.country}", style = MaterialTheme.typography.bodyLarge)
            Text("Rating: ${hotel.rating}", style = MaterialTheme.typography.bodyLarge)
            Text("Address: ${hotel.address}", style = MaterialTheme.typography.bodyLarge)

            if (isRoomLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                }
            } else {
                // Show total rooms available or fallback
                if (hotel.totalRoomsAvailable != null) {
                    Text("Total rooms available: ${hotel.totalRoomsAvailable}", style = MaterialTheme.typography.titleMedium)
                } else {
                    Text("No room availability", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

