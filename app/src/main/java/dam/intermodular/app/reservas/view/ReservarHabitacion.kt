package dam.intermodular.app.reservas.view

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam.intermodular.app.reservas.model.NuevaReserva
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.viewmodel.ReservasViewModel
import dam.intermodular.app.ui.theme.Purple40
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ReservarHabitacionScreen(
    navController: NavHostController,
    habitacionId: String,
    habitacionNombre: String,
    precioNoche: String,
    maxHuespedes: Int,
    usuarioId: String,
    reservaViewModel: ReservasViewModel = viewModel()
) {
    val context = LocalContext.current
    var fechaEntrada by remember { mutableStateOf("") }
    var fechaSalida by remember { mutableStateOf("") }
    var numHuespedes by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val calendarEntrada = Calendar.getInstance()
    val datePickerEntrada = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fechaEntrada = dateFormat.format(calendarEntrada.apply {
                set(year, month, dayOfMonth)
            }.time)
        },
        calendarEntrada.get(Calendar.YEAR),
        calendarEntrada.get(Calendar.MONTH),
        calendarEntrada.get(Calendar.DAY_OF_MONTH)
    )

    val calendarSalida = Calendar.getInstance()
    val datePickerSalida = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fechaSalida = dateFormat.format(calendarSalida.apply {
                set(year, month, dayOfMonth)
            }.time)
        },
        calendarSalida.get(Calendar.YEAR),
        calendarSalida.get(Calendar.MONTH),
        calendarSalida.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Reservar Habitación", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Purple40)
        Text(text = "ID Habitación: $habitacionId", fontWeight = FontWeight.SemiBold)
        Text(text = "Nombre: $habitacionNombre", fontWeight = FontWeight.SemiBold)
        Text(text = "Precio por noche: $precioNoche€", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Fecha de Entrada
        OutlinedTextField(
            value = fechaEntrada,
            onValueChange = { },
            label = { Text("Fecha de Entrada (yyyy-MM-dd)") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerEntrada.show() }) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = "Seleccionar Fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Fecha de Salida
        OutlinedTextField(
            value = fechaSalida,
            onValueChange = { },
            label = { Text("Fecha de Salida (yyyy-MM-dd)") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerSalida.show() }) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = "Seleccionar Fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Número de Huéspedes
        OutlinedTextField(
            value = numHuespedes,
            onValueChange = { newValue ->
                if (newValue.toIntOrNull() in 1..maxHuespedes) {
                    numHuespedes = newValue
                }
            },
            label = { Text("Número de Huéspedes (Máximo: $maxHuespedes)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (fechaEntrada.isEmpty() || fechaSalida.isEmpty() || numHuespedes.isEmpty()) {
                        Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSaving = true

                    // ✅ Usa NuevaReserva en lugar de Reservas
                    val nuevaReserva = NuevaReserva(
                        idUsu = usuarioId,
                        idHab = habitacionId,
                        fechaEntrada = fechaEntrada,
                        fechaSalida = fechaSalida,
                        estado = "Pendiente"
                    )

                    Log.d("CREATE_RESERVA", "Enviando reserva: $nuevaReserva")

                    reservaViewModel.createReserva(
                        idUsu = usuarioId,
                        idHab = habitacionId,
                        fechaEntrada = fechaEntrada,
                        fechaSalida = fechaSalida,
                        estado = "Pendiente",
                        onSuccess = {
                            isSaving = false
                            Toast.makeText(context, "Reserva creada con éxito", Toast.LENGTH_SHORT).show()
                            navController.navigate("reservas_screen")
                        },
                        onError = { errorMsg ->
                            isSaving = false
                            Toast.makeText(context, "Error al crear la reserva: $errorMsg", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Confirmar Reserva")
                }
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancelar")
            }
        }
    }
}