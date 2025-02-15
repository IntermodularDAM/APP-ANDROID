package dam.intermodular.app.habitaciones.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import dam.intermodular.app.core.api.ApiService
import dam.intermodular.app.core.dataStore.DataStoreManager
import dam.intermodular.app.habitaciones.model.Habitacion
import java.util.Locale
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
        return favoritos
            .map { favoritosList ->
                favoritosList.any { it._id == habitacion._id }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, false) // Valor inicial 'false'
    }

    fun filterByName(query: String) {
        _filteredHabitaciones.update {
            if (query.isEmpty()) _habitaciones.value
            else _habitaciones.value.filter { it.nombre.startsWith(query, ignoreCase = true) }
        }
    }


    fun applyFilters(
        priceRange: String?,
        capacity: String?,
        roomType: String?,
        options: String?
    ) {
        _filteredHabitaciones.update { habitaciones ->
            // Verificamos si hay filtros aplicados
            val isFiltering = priceRange != null || capacity != null || roomType != null || options != null

            if (!isFiltering) {
                // Si no hay filtros, mostrar todas las habitaciones
                return@update habitaciones
            }

            // Filtrar las habitaciones
            val filtered = habitaciones.filter { habitacion ->
                // Filtrar por precio (si el filtro es no nulo)
                val matchesPrice = priceRange?.let {
                    try {
                        val price = habitacion.precio_noche.toInt()
                        val priceRangeParts = priceRange.split("-")
                        val minPrice = priceRangeParts[0].toIntOrNull() ?: 0
                        val maxPrice = priceRangeParts.getOrNull(1)?.toIntOrNull() ?: Int.MAX_VALUE
                        price in minPrice..maxPrice
                    } catch (e: Exception) {
                        false
                    }
                } ?: true // Si no hay filtro, pasa

                // Filtrar por capacidad (si el filtro es no nulo)
                val matchesCapacity = capacity?.let {
                    habitacion.capacidad == capacity
                } ?: true

                // Filtrar por tipo de habitación (si el filtro es no nulo)
                val matchesRoomType = roomType?.let {
                    habitacion.tipo.contains(roomType, ignoreCase = true)
                } ?: true

                // Filtrar por opciones adicionales (si el filtro es no nulo)
                val matchesOptions = options?.let {
                    val selectedOptions = it.split(",").map { option -> option.trim()
                        .lowercase(Locale.ROOT) }

                    selectedOptions.all { selectedOption ->
                        when (selectedOption) {
                            "cama extra" -> habitacion.opciones.CamaExtra == true
                            "cuna" -> habitacion.opciones.Cuna == true
                            else -> false
                        }
                    }
                } ?: true

                // Solo devolver habitaciones que coincidan con todos los filtros activos
                matchesPrice && matchesCapacity && matchesRoomType && matchesOptions
            }

            // Verifica si no se encontraron habitaciones
            if (filtered.isEmpty()) {
                println("No rooms available for this filter.")
            }

            filtered
        }
    }




}
