package dam.intermodular.app.core.api

import dam.intermodular.app.habitaciones.Habitacion
import dam.intermodular.app.login.data.LoginResponse
import dam.intermodular.app.login.presentation.model.LogInModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("Usuario/logIn")
    suspend fun apiLogIn(@Body login: LogInModel): Response<LoginResponse>

    @GET("Habitacion/habitaciones")
    suspend fun getHabitaciones(): Response<List<Habitacion>>
}
