package dam.intermodular.app.habitaciones.model

data class Habitacion(
    val _id: String,
    val num_planta: Int,
    val nombre: String,
    val tipo: String,
    val capacidad: String,
    val descripcion: String,
    val opciones: Opciones,
    val precio_noche: Double,
    val precio_noche_original: Double?,
    val tieneOferta: Boolean,
    val estado: Boolean,
    val imagenBase64: String
)

data class Opciones (
    val CamaExtra: Boolean?,
    val Cuna: Boolean?
)
