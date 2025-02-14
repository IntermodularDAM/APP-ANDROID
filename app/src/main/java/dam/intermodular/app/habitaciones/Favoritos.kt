package dam.intermodular.app.habitaciones

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("DefaultLocale")
@Composable
fun FavoritesScreen(navController: NavController, habitacionesViewModel: HabitacionesViewModel) {
    val favoritos by habitacionesViewModel.favoritos.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = {
               navController.navigate("main_screen")
            }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(text = "Habitaciones Favoritas",
                fontSize = 28.sp,
                color = Color.Magenta,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

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

                            val option = when {
                                habitacion.opciones.CamaExtra == true -> "CamaExtra"
                                habitacion.opciones.Cuna == true -> "Cuna"
                                else -> "Ninguna opción"
                            }

                            val encodedNombre = URLEncoder.encode(habitacion.nombre, StandardCharsets.UTF_8.toString())
                            val encodedDescripcion = URLEncoder.encode(habitacion.descripcion, StandardCharsets.UTF_8.toString())
                            val encodedOpciones = URLEncoder.encode(option, StandardCharsets.UTF_8.toString())
                            val encodedImagenBase64 = URLEncoder.encode(habitacion.imagenBase64, StandardCharsets.UTF_8.toString())
                            val formattedPrecio = String.format("%.2f", habitacion.precio_noche)
                            navController.navigate("room_details_screen/$encodedNombre/$encodedDescripcion/$formattedPrecio/$encodedOpciones/$encodedImagenBase64/favorites_screen")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
