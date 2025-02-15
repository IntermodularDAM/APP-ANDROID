package dam.intermodular.app.reservas.model

import com.google.gson.annotations.SerializedName

data class NuevaReserva(
    @SerializedName("id_usu") val idUsu: String,
    @SerializedName("id_hab") val idHab: String,
    @SerializedName("fecha_check_in") val fechaEntrada: String,
    @SerializedName("fecha_check_out") val fechaSalida: String,
    @SerializedName("estado_reserva") val estado: String
)