package dam.intermodular.app.habitaciones

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("DefaultLocale")
@Composable
fun FavoritesScreen(navController: NavController, habitacionesViewModel: HabitacionesViewModel) {
    val favoritos by habitacionesViewModel.favoritos.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Habitaciones Favoritas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (favoritos.isEmpty()) {
            Text(text = "No hay habitaciones favoritas.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(favoritos) { habitacion ->
                    RoomCard(
                        habitacion = habitacion,
                        habitacionesViewModel = habitacionesViewModel,
                        onClick = {
                            val encodedNombre = URLEncoder.encode(habitacion.nombre, StandardCharsets.UTF_8.toString())
                            val encodedDescripcion = URLEncoder.encode(habitacion.descripcion, StandardCharsets.UTF_8.toString())
                            val encodedImagenBase64 = URLEncoder.encode(habitacion.imagenBase64, StandardCharsets.UTF_8.toString())
                            navController.navigate("room_details_screen/$encodedNombre/$encodedDescripcion/${habitacion.precio_noche}/$encodedImagenBase64")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
