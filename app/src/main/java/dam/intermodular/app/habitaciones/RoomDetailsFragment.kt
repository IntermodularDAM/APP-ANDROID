package dam.intermodular.app.habitaciones


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    roomOption: String,
    roomImage: String,
    previousScreen: String
) {
    //val context = LocalContext.current
    val defaultImage = painterResource(id = R.drawable.room_image) // Imagen por defecto
    val imageBitmap = base64ToImageBitmap(roomImage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = {
                when(previousScreen) {
                    "main_screen" -> navController.navigate("main_screen") { popUpTo("main_screen") { inclusive = true } }
                    "favorites_screen" -> navController.navigate("favorites_screen") { popUpTo("favorites_screen") { inclusive = true } }
                    else -> navController.popBackStack()
                }
            }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Text(
                text = "Detalles de la Habitación",
                fontSize = 28.sp,
                color = Color.Magenta,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Imagen de la habitación
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Imagen de la habitación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
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

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append("Nombre de la habitación: ")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(roomName)
                }
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append("Descripción: ")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(roomDescription)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Precio de la habitación
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append("Precio: ")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("$roomPrice €/noche")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Opciones extras
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append("Opciones extras: ")
                }
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append(roomOption)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de reserva
        Button(
            onClick = {
                navController.navigate("main_screen") // Regresar a la pantalla principal
            }
        ) {
            Text(
                text = "Reservar ahora",

            )
        }
    }
}
