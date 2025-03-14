package dam.intermodular.app.habitaciones.view

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam.intermodular.app.R
import dam.intermodular.app.habitaciones.model.Habitacion
import dam.intermodular.app.habitaciones.viewModel.HabitacionesViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64

fun base64ToImageBitmap(base64String: String): ImageBitmap? {
    return try {
        val decodedString = Base64.getDecoder().decode(base64String)
        val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        decodedBitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

@Composable
fun RoomCard(
    habitacion: Habitacion,
    habitacionesViewModel: HabitacionesViewModel,
    onClick: () -> Unit
) {
    val isFavorite by habitacionesViewModel.isFavorite(habitacion).collectAsState(initial = false)

    val imageBitmap = remember(habitacion.imagenBase64) {
        habitacion.imagenBase64?.let { base64ToImageBitmap(it) }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(235.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Image(
                bitmap = imageBitmap ?: ImageBitmap.imageResource(R.drawable.room_image),
                contentDescription = habitacion.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Box(modifier = Modifier.fillMaxSize()) {
                // Content before the heart icon
                Column(
                    modifier = Modifier
                        .padding(16.dp) // Adjust the padding as needed
                        .align(Alignment.TopStart) // Positioning the content normally (top left, etc.)
                ) {
                    // Titulo de la habitacion
                    Text(
                        text = habitacion.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))  // Espacio entre nombre y precios

                    // Mostrar el precio original con texto, tachado
                    Text(
                        text = "Original: ${"%.2f".format(habitacion.precio_noche_original ?: 0.0)}€", // Precio original en euros
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = Color.Red
                    )

                    // Mostrar el precio por noche con texto
                    Text(
                        text = "Actual: ${"%.2f".format(habitacion.precio_noche)}€", // Precio por noche en euros
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))  // Espacio entre precios y opciones

                    // Opción entre "Cama Extra" y "Cuna"
                    val opcion = when {
                        habitacion.opciones.CamaExtra == true -> "Cama Extra"
                        habitacion.opciones.Cuna == true -> "Cuna"
                        else -> "Ninguna opción"
                    }

                    Text(
                        text = "Opcion:  $opcion",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Magenta
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Heart icon positioned at the bottom-right corner
                IconButton(
                    onClick = { habitacionesViewModel.toggleFavorite(habitacion) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MainScreen(navController: NavHostController, habitacionesViewModel: HabitacionesViewModel = viewModel()) {
    //val habitaciones by habitacionesViewModel.habitaciones.collectAsState()
    val filteredHabitaciones by habitacionesViewModel.filteredHabitaciones.collectAsState()
    //val favoritos by habitacionesViewModel.favoritos.collectAsState()

    val showFilterDialog = remember { mutableStateOf(false) }
    var showNotification by remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(searchQuery.value) {
        habitacionesViewModel.filterByName(searchQuery.value)
    }

    LaunchedEffect(Unit) {
        habitacionesViewModel.loadHabitaciones()
    }

    // Función que aplica los filtros
    val applyFilters =
        { priceRange: String?, capacity: String?, roomType: String?, options: String? ->
            habitacionesViewModel.applyFilters(priceRange, capacity, roomType, options)
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .padding(bottom = 72.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(), // Ocupa todo el espacio disponible
                    contentAlignment = Alignment.Center // Centra el contenido del Box
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally // Centra los textos dentro del Column
                    ) {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        )
                        Text(text = "Night Days", style = MaterialTheme.typography.titleLarge)
                    }

                    IconButton(
                        onClick = {
                            showNotification = true
                        }, // Al hacer clic, se abre la notificación
                        modifier = Modifier
                            .padding(end = 5.dp, top = 15.dp)
                            .align(Alignment.TopEnd) // Posiciona el icono en la esquina superior derecha
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    placeholder = { Text(text = "Buscar habitación por nombre") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent)
                )
                IconButton(onClick = { showFilterDialog.value = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Filter")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredHabitaciones.isEmpty()) {
                Text(
                    text = "No hay habitaciones disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Habitaciones recomendadas",
                    style = MaterialTheme.typography.titleLarge
                )
                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.weight(1f)) {
                    items(filteredHabitaciones) { habitacion ->
                        RoomCard(
                            habitacion = habitacion,
                            habitacionesViewModel = habitacionesViewModel,
                            onClick = {
                                try {
                                    val option = when {
                                        habitacion.opciones.CamaExtra == true -> "CamaExtra"
                                        habitacion.opciones.Cuna == true -> "Cuna"
                                        else -> "Ninguna opción"
                                    }

                                    // Evitar errores de codificación en caso de valores nulos
                                    val encodedNombre = URLEncoder.encode(habitacion.nombre ?: "Sin nombre", StandardCharsets.UTF_8.toString())
                                    val encodedDescripcion = URLEncoder.encode(habitacion.descripcion ?: "Sin descripción", StandardCharsets.UTF_8.toString())
                                    val encodedOpciones = URLEncoder.encode(option, StandardCharsets.UTF_8.toString())

                                    // Verificar que la imagen base64 no sea null
                                    val encodedImagenBase64 = habitacion.imagenBase64?.let {
                                        URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                                    } ?: ""

                                    val formattedPrecio = String.format("%.2f", habitacion.precio_noche ?: 0.0)
                                    val numMax = habitacion.capacidad?.toString() ?: "0"
                                    val roomId = habitacion._id ?: "0"

                                    navController.navigate("room_details_screen/$roomId/$encodedNombre/$encodedDescripcion/$formattedPrecio/$numMax/$encodedOpciones/$encodedImagenBase64/main_screen")
                                } catch (e: Exception) {
                                    e.printStackTrace() // Para depuración
                                    Toast.makeText(context, "Error al abrir los detalles de la habitación", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }

        // Mostrar el cuadro de diálogo de filtro
        FilterFragment(
            isVisible = showFilterDialog.value,
            onDismiss = { showFilterDialog.value = false },
            applyFilters = { priceRange, capacity, roomType, options ->
                applyFilters(priceRange, capacity, roomType, options)
                showFilterDialog.value = false // Cerrar el diálogo después de aplicar los filtros
            }
        )

        // Mostrar el cuadro de notificaciones (en este caso son notificaciones permanentes)
        Notification(
            isVisible = showNotification,
            onDismiss = { showNotification = false }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(onClick = { navController.navigate("main_screen") }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }

            IconButton(onClick = { navController.navigate("reservas_screen") }) {
                Icon(Icons.Filled.Search, contentDescription = "Historial")
            }

            IconButton(
                onClick = {
                    // Mostrar mensaje (Toast) cada vez que se pulse el botón
                    Toast.makeText(
                        context,
                        "¡BIENVENIDOS A NIGHT DAYS!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(Icons.Filled.Info, contentDescription = "Info")
            }

            IconButton(onClick = { navController.navigate("favorites_screen") }) {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
            }

            IconButton(onClick = { navController.navigate("profile_screen") }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
        }
    }
}
