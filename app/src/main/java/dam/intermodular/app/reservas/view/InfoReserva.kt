package dam.intermodular.app.reservas.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.ui.theme.Purple40

@Composable
fun InfoReservaScreen(navController: NavHostController, reserva: Reservas) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Información de la Reserva", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Purple40)
        Text(text = "ID: ${reserva.id}")
        Text(text = "Usuario: ${reserva.idUsu}")
        Text(text = "Habitación: ${reserva.idHab}")
        Text(text = "Check-in: ${reserva.fechaEntrada}")
        Text(text = "Check-out: ${reserva.fechaSalida}")
        Text(text = "Estado: ${reserva.estado}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}