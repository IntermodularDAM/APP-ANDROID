package dam.intermodular.app.reservas.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import dam.intermodular.app.habitaciones.Habitacion
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReservasViewModel : ViewModel() {
    private val _reservas = MutableStateFlow<List<Reservas>>(emptyList())
    val reservas: StateFlow<List<Reservas>> = _reservas

    init {
        getAllReservas()
    }

    fun getAllReservas() {
        viewModelScope.launch {
            try {
                Log.d("API_CALL", "Llamando a la API para obtener reservas...") // ✅ DEBUG 1
                val response = RetrofitClient.reservaApiService.getAllReservas()
                Log.d("API_RESPONSE", "Reservas recibidas: $response") // ✅ DEBUG 2

                if (response.reservas.isNotEmpty()) {
                    Log.d("API_RESPONSE", "Lista de reservas obtenida: ${response.reservas}")
                    _reservas.value = response.reservas
                } else {
                    Log.d("API_RESPONSE", "La API devolvió una lista vacía")
                }
            } catch (e: Exception) {
                Log.e("ReservaViewModel", "Error al obtener reservas: ${e.message}", e)
            }
        }
    }

    fun updateReserva(
        reservaId: String,
        idUser: String,
        idHabt: String,
        fechaEntrada: String,
        fechaSalida: String,
        estado: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val updatedReserva = Reservas(reservaId, idUser, idHabt, fechaEntrada, fechaSalida, estado)
                RetrofitClient.reservaApiService.updateReserva(reservaId, updatedReserva)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}