package dam.intermodular.app.reservas.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import dam.intermodular.app.core.dataStore.DataStoreManager
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.viewmodel.ReservasViewModel
import dam.intermodular.app.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    navController: NavHostController,
    reservaViewModel: ReservasViewModel = viewModel()
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) } // Inicializar DataStoreManager
    val userId by dataStoreManager.getIdProfile().collectAsState(initial = null) // Obtener el ID del usuario logueado
    val reservas by reservaViewModel.reservas.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("DEBUG", "Ejecutando ReservasScreen()")
        reservaViewModel.getAllReservas()
    }

    // Filtrar reservas solo para el usuario actual
    val reservasUsuario = reservas.filter { it.idUsu == userId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Historial de Reservas",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Purple40)
            )
        },
        bottomBar = {
            IconButton(onClick = { navController.navigate("main_screen") }) {
                Icon(Icons.Filled.Home, contentDescription = "Home", tint = Purple40)
            }
        },
        containerColor = Color(0xFFD3D3D3)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (userId == null) {
                Text(
                    text = "Cargando usuario...",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth().wrapContentSize()
                )
            } else if (reservasUsuario.isEmpty()) {
                Text(
                    text = "No tienes ninguna Reserva.",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth().wrapContentSize()
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(reservasUsuario) { reserva ->
                        ReservaItem(
                            reserva = reserva,
                            onInfoClick = {
                                val reservaJson = Gson().toJson(reserva)
                                navController.navigate("infoReserva/$reservaJson")
                            },
                            onModifyClick = {
                                val reservaJson = Gson().toJson(reserva)
                                navController.navigate("modificarReserva/$reservaJson")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReservaItem(reserva: Reservas, onInfoClick: (String) -> Unit, onModifyClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(5.dp, Color.DarkGray, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Reserva ID: ${reserva.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Purple40)
            Text(
                text = "Estado: ${reserva.estado}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = if (reserva.estado == "Confirmada") Purple40 else Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onInfoClick(reserva.id) }, colors = ButtonDefaults.buttonColors(containerColor = Purple40)) {
                    Text("Info", color = Color.White)
                }

                Button(
                    onClick = { onModifyClick(reserva.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    border = BorderStroke(3.dp, Purple40)
                ) {
                    Text("Modificar", color = Color.Black)
                }
            }
        }
    }
}