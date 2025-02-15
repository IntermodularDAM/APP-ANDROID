package dam.intermodular.app.reservas.api

import dam.intermodular.app.reservas.model.NuevaReserva
import dam.intermodular.app.reservas.model.Reservas
import retrofit2.http.*

interface ReservaService {
    @GET("Reserva/getAll")
    suspend fun getAllReservas(): ReservaResponse

    @PUT("Reserva/modificarReserva/{id}")
    suspend fun updateReserva(@Path("id") id: String, @Body reserva: Reservas): Reservas

    @POST("Reserva/crearReserva")
    suspend fun createReserva(@Body reserva: NuevaReserva): ReservaResponse
}