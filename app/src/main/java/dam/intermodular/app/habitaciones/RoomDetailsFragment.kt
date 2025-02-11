package dam.intermodular.app.habitaciones


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dam.intermodular.app.R


@Composable
fun RoomDetailsFragment(
    navController: NavHostController,
    roomName: String,
    roomDescription: String,
    roomPrice: String,
    roomImage: String
) {
    val context = LocalContext.current
    val defaultImage = painterResource(id = R.drawable.room_image) // Imagen por defecto
    val imageBitmap = base64ToImageBitmap(roomImage)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Título
        Text(
            text = "Detalles de la Habitación",
            fontSize = 18.sp
        )

        // Título de la habitación
        Text(
            text = roomName,
            fontSize = 22.sp,
            style = MaterialTheme.typography.bodyMedium
        )

        // Imagen de la habitación
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Imagen de la habitación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = defaultImage,
                contentDescription = "Imagen por defecto de la habitación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Descripción
        Text(
            text = roomDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        // Precio de la habitación
        Text(
            text = "Precio: $roomPrice / noche",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        // Botón de reserva
        Button(
            onClick = {
                navController.navigate("main_screen") // Regresar a la pantalla principal
            }
        ) {
            Text("Reservar Ahora")
        }
    }
}
