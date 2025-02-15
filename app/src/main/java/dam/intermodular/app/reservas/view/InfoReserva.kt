package dam.intermodular.app.reservas.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoReservaScreen(navController: NavHostController, reserva: Reservas) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Información de la Reserva",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Purple40)
            )
        },
        containerColor = Color(0xFFF2F2F2) // Fondo gris claro
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoItem(label = "ID", value = reserva.id)
            InfoItem(label = "Usuario", value = reserva.idUsu)
            InfoItem(label = "Habitación", value = reserva.idHab)
            InfoItem(label = "Check-in", value = reserva.fechaEntrada)
            InfoItem(label = "Check-out", value = reserva.fechaSalida)
            InfoItem(label = "Estado", value = reserva.estado)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Purple40)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Purple40),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Purple40)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, color = Color.DarkGray)
        }
    }
}