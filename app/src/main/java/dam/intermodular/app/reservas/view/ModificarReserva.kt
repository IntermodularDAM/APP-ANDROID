package dam.intermodular.app.reservas.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.viewmodel.ReservasViewModel
import dam.intermodular.app.ui.theme.Purple40
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarReservaScreen(navController: NavHostController, reserva: Reservas, reservaViewModel: ReservasViewModel = viewModel()) {
    val context = LocalContext.current
    var fechaEntrada by remember { mutableStateOf(reserva.fechaEntrada) }
    var fechaSalida by remember { mutableStateOf(reserva.fechaSalida) }
    var estado by remember { mutableStateOf(reserva.estado) }
    var isUpdating by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) } // Para el menú desplegable

    val estados = listOf("Confirmada", "Pendiente", "Cancelada")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // DatePicker para Fecha de Entrada
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

    // DatePicker para Fecha de Salida
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
        Text(text = "Modificar Reserva", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Purple40)
        Text(text = "ID: ${reserva.id}")

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar Fecha de Entrada
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

        // Botón para seleccionar Fecha de Salida
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

        // Menú desplegable para Estado de la Reserva
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = { },
                label = { Text("Estado") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Desplegar menú")
                    }
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                estados.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            estado = opcion
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    isUpdating = true
                    reservaViewModel.updateReserva(
                        reserva.id,
                        reserva.idUsu,
                        reserva.idHab,
                        fechaEntrada,
                        fechaSalida,
                        estado,
                        onSuccess = {
                            isUpdating = false
                            Toast.makeText(context, "Reserva actualizada con éxito", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = {
                            isUpdating = false
                            Toast.makeText(context, "Error al actualizar la reserva", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = !isUpdating
            ) {
                if (isUpdating) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Guardar")
                }
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancelar")
            }
        }
    }
}