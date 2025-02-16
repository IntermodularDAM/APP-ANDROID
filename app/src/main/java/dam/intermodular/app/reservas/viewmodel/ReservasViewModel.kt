package dam.intermodular.app.reservas.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import dam.intermodular.app.habitaciones.model.Habitacion
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.api.RetrofitClient
import dam.intermodular.app.reservas.model.NuevaReserva
import dam.intermodular.app.login.presentation.viewModel.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

class ReservasViewModel : ViewModel() {
    private val _reservas = MutableStateFlow<List<Reservas>>(emptyList())
    val reservas: StateFlow<List<Reservas>> = _reservas

    init {
        getAllReservas()
    }

    fun getAllReservas() {
        viewModelScope.launch {
            try {
                Log.d("API_CALL", "Llamando a la API para obtener reservas...")
                val response = RetrofitClient.reservaApiService.getAllReservas()
                Log.d("API_RESPONSE", "Reservas recibidas: $response")

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
                Log.d("UPDATE_RESERVA", "Enviando actualización: $updatedReserva")

                val response = RetrofitClient.reservaApiService.updateReserva(reservaId, updatedReserva)
                Log.d("UPDATE_RESERVA_API", "Respuesta de la API: $response")

                if (response.isSuccessful) {
                    Log.d("UPDATE_RESERVA_SUCCESS", "Reserva actualizada correctamente: ${response.body()}")
                    onSuccess()
                } else {
                    Log.e("UPDATE_RESERVA_ERROR", "Error en la API: ${response.code()} - ${response.message()}")
                    onError()
                }
            } catch (e: Exception) {
                Log.e("UPDATE_RESERVA_ERROR", "Excepción en la actualización: ${e.message}", e)
                onError()
            }
        }
    }

    fun createReserva(
        idUsu: String,
        idHab: String,
        fechaEntrada: String,
        fechaSalida: String,
        estado: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            Log.d("USER_ID", "ID del usuario obtenido: $idUsu") // Depuración

            try {
                val nuevaReserva = NuevaReserva(idUsu, idHab, fechaEntrada, fechaSalida, estado)
                val response = RetrofitClient.reservaApiService.createReserva(nuevaReserva)
                Log.d("API_RESPONSE", "Reserva creada: $response")
                onSuccess()
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al crear reserva: ${e.message}", e)
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}