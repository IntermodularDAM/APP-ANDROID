package dam.intermodular.app.habitaciones.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FilterFragment(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    applyFilters: (String?, String?, String?, String?) -> Unit
) {
    // Crear estado para cada campo del filtro
    var priceRange by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf("") }
    var options by remember { mutableStateOf("") }

    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Apply Filters") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Filtro de rango de precio
                    Text("Price Range:")
                    TextField(
                        value = priceRange,
                        onValueChange = { priceRange = it },
                        label = { Text("Enter price range") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtro de capacidad
                    Text("Capacity:")
                    TextField(
                        value = capacity,
                        onValueChange = { capacity = it },
                        label = { Text("Enter capacity") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtro de tipo de habitación
                    Text("Room Type:")
                    TextField(
                        value = roomType,
                        onValueChange = { roomType = it },
                        label = { Text("Enter room type") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtro de opciones
                    Text("Options:")
                    TextField(
                        value = options,
                        onValueChange = { options = it },
                        label = { Text("Enter options") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    applyFilters(priceRange, capacity, roomType, options)
                    onDismiss() // Cerrar el dialogo
                }) {
                    Text("Apply Filters")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterFragmentPreview() {
    FilterFragment(isVisible = true, onDismiss = {}, applyFilters = { _, _, _, _ -> })
}
