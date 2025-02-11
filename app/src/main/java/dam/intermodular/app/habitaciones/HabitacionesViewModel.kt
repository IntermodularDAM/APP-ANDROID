package dam.intermodular.app.habitaciones

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dam.intermodular.app.core.api.ApiService
import dam.intermodular.app.core.dataStore.DataStoreManager
import javax.inject.Inject

@HiltViewModel
class HabitacionesViewModel @Inject constructor(
    application: Application,
    private val dataStoreManager: DataStoreManager,
    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val _habitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    private val _filteredHabitaciones = MutableStateFlow<List<Habitacion>>(emptyList())
    private val _favoritos = MutableStateFlow<List<Habitacion>>(emptyList())

    val habitaciones: StateFlow<List<Habitacion>> = _habitaciones.asStateFlow()
    val filteredHabitaciones: StateFlow<List<Habitacion>> = _filteredHabitaciones.asStateFlow()
    val favoritos: StateFlow<List<Habitacion>> = _favoritos.asStateFlow()

    init {
        loadHabitaciones()
    }

    fun loadHabitaciones() {
        viewModelScope.launch {
            try {
                val response = apiService.getHabitaciones()
                if (response.isSuccessful) {
                    response.body()?.let { habitaciones ->
                        _habitaciones.value = habitaciones
                        _filteredHabitaciones.value = habitaciones
                        loadFavoritos() // Solo ejecuta si hay datos
                    } ?: run {
                        Log.e("API_ERROR", "Respuesta vacía de la API")
                    }
                } else {
                    Log.e("API_ERROR", "Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Fallo al obtener habitaciones: ${e.localizedMessage}")
            }
        }
    }

    private fun loadFavoritos() {
        viewModelScope.launch {
            val favoritosIds = dataStoreManager.favoritos.first()
            _favoritos.value = _habitaciones.value.filter { it._id in favoritosIds }
        }
    }

    fun toggleFavorite(habitacion: Habitacion) {
        viewModelScope.launch {
            dataStoreManager.toggleFavorite(habitacion._id)
            loadFavoritos()
        }
    }

    fun isFavorite(habitacion: Habitacion): StateFlow<Boolean> {
        return favoritos.map { it.any { fav -> fav._id == habitacion._id } }
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
    }

    fun filterByName(query: String) {
        _filteredHabitaciones.update { habitaciones ->
            if (query.isEmpty()) _habitaciones.value
            else _habitaciones.value.filter { it.nombre.contains(query, ignoreCase = true) }
        }
    }
}
