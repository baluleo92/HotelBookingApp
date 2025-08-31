package com.hotel.bookingapp.model

data class Hotel(
    val id: String,
    val title: String,
    val address: String,
    val city: String,
    val country: String,
    val thumbnail: List<String>,
    val rating: String,
    val roomInfo: String? = null,                // for category or suite name
    val totalRoomsAvailable: Int? = null,        // overall rooms available
    val roomsAvailable: Map<String, Int>? = null,// may include room type breakdown
    val amenities: List<Amenity>? = null,        // hotel amenities chips/icons
    val tags: List<HotelTag>? = null,            // tags like 'Hotel', 'Breakfast'
    val nearby: List<Nearby>? = null,            // nearby locations/chips
    val currency: Map<String, Double>? = null,   // price in multiple currencies
    val price: Double? = null,                   // convenience field, fallback
    val moreInfo: MoreInfo? = null,              // see below for details
    val others: List<OtherFeature>? = null,      // features like free cancellation
    val noRoomsAvailable: Int? = null            // fallback for room count
)

data class Amenity(
    val type: String,
    val key: String,
    val displayName: String
    // Add optional val iconRes: Int? for icons if needed
)

data class HotelTag(
    val type: String,
    val key: String,
    val displayName: String
)

data class Nearby(
    val type: String,
    val key: String,
    val displayName: String
)

data class OtherFeature(
    val type: String,
    val key: String,
    val displayName: String
)

// For additional hotel details like rooms, notes, description, policies
data class MoreInfo(
    val rooms: List<RoomInfo>? = null,
    val notes: String? = null,
    val description: String? = null,
    val policies: String? = null
)

data class RoomInfo(
    val noRoomsAvailable: Int? = null,
    val title: String? = null,
    val category: String? = null,
    // add more as needed from your JSON
)
