package com.hotel.bookingapp.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
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
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Images Row
            Row(Modifier.fillMaxWidth()) {
                hotel.thumbnail.take(2).forEach { imageUrl ->
                    val painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        error = painterResource(id = R.drawable.thumbnail_default)
                    )
                    Image(
                        painter = painter,
                        contentDescription = hotel.title,
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(6.dp))
                }
            }

            // Title and stars
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    hotel.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(8.dp))
                val numStars = hotel.rating.filter { it.isDigit() }.toIntOrNull() ?: 0
                repeat(numStars) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Address (no overflow)
            Text(
                listOfNotNull(hotel.address, hotel.city, hotel.country).joinToString(", "),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Amenities as wrapped chips (use FlowRow)
            hotel.amenities?.let { amenities ->
                FlowRow(
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    amenities.take(5).forEach { amenity ->
                        AssistChipContent(amenity.displayName)
                    }
                    if (amenities.size > 5) {
                        AssistChipContent("+${amenities.size - 5}")
                    }
                }
            }

            // Nearby as wrapped chips
            hotel.nearby?.takeIf { it.isNotEmpty() }?.let { places ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Nearby:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(6.dp))
                    FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
                        places.forEach { nearby ->
                            AssistChipContent(nearby.displayName)
                        }
                    }
                }
            }

            // Price (USD)
            hotel.currency?.get("USD")?.let { usdPrice ->
                Text(
                    "Starts from \$${usdPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
            }
            Text("1 Adult / 1 night", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            // Free Cancellation (if applicable)
            hotel.others?.find { it.key == "free_cancellation_policy" }?.let {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFC3FAD5), shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = it.displayName, color = Color(0xFF189B65), style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Room info section and loader
            if (isRoomLoading) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF189B65)
                    )
                }
            } else {
                val rooms = hotel.totalRoomsAvailable
                    ?: hotel.moreInfo?.rooms?.firstOrNull()?.noRoomsAvailable
                    ?: hotel.noRoomsAvailable
                    ?: 0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFFCF3), RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_room),
                        contentDescription = "Rooms",
                        tint = Color(0xFF189B65),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Total rooms available: $rooms",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF189B65)
                    )
                }
            }

            // Booking button row (example, you can adjust)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { /* Proceed action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0063F7))
                ) {
                    Text("Proceed")
                }
            }
        }
    }
}

@Composable
fun AssistChipContent(label: String?) {
    if (!label.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .background(Color(0xFFF3F3F3), shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
