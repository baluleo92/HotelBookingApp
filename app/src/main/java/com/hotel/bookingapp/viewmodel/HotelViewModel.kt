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

    private val _isRoomLoading = MutableStateFlow(true)
    val isRoomLoading: StateFlow<Boolean> = _isRoomLoading.asStateFlow()

    init {
        viewModelScope.launch {
            val hotelsList = repository.getHotels()
            _hotels.value = hotelsList

            // Simulate network/disk loading
            delay(5000)
            val roomData = repository.getRoomAvailabilities()
            val updated = hotelsList.map { hotel ->
                val roomsForHotel = roomData.filter { it.hotel_id == hotel.id }
                val totalRooms = roomsForHotel.sumOf { it.noRoomsAvailable }
                hotel.copy(totalRoomsAvailable = if (roomsForHotel.isNotEmpty()) totalRooms else null)
            }
            _hotels.value = updated
            _isRoomLoading.value = false // Loading finished
        }
    }
}
