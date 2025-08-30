package com.hotel.bookingapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.hotel.bookingapp.HotelRepository
import com.hotel.bookingapp.model.Hotel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {
    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> = _hotels.asStateFlow()

    init {
        viewModelScope.launch {
            val hotelsList = repository.getHotels()
            _hotels.value = hotelsList

            delay(5000)
            val roomData = repository.getRoomAvailabilities()
            val updated = hotelsList.map { hotel ->
                val rooms = roomData.filter { it.hotel_id == hotel.id }
                    .associate { it.room_type_id to it.noRoomsAvailable }
                hotel.copy(roomsAvailable = rooms.ifEmpty { null })
            }
            _hotels.value = updated
        }
    }
}