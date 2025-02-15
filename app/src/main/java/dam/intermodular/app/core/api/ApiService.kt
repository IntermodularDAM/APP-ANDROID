package dam.intermodular.app.core.api

import dam.intermodular.app.habitaciones.model.Habitacion
import dam.intermodular.app.login.data.LoginResponse
import dam.intermodular.app.login.presentation.model.LogInModel
import dam.intermodular.app.registro.data.RegisterResponse
import dam.intermodular.app.registro.presentation.model.RegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("Usuario/logIn")
    suspend fun apiLogIn(@Body login: LogInModel): Response<LoginResponse>

    @POST("Usuario/registroUsuario")
    suspend fun apiAddUser(@Body register : RegisterModel) : Response<RegisterResponse>

    @GET("Habitacion/habitaciones")
    suspend fun getHabitaciones(): Response<List<Habitacion>>
}
