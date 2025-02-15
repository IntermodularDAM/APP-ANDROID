package dam.intermodular.app.core.api

import dam.intermodular.app.habitaciones.Habitacion
import dam.intermodular.app.login.data.LoginResponse
import dam.intermodular.app.login.presentation.model.LogInModel
import dam.intermodular.app.registro.data.RegisterResponse
import dam.intermodular.app.registro.presentation.model.RegisterModel
import dam.intermodular.app.verificationCode.data.VerificationCodeResponse
import dam.intermodular.app.verificationCode.presentation.model.VerificationCodeModel
import dam.intermodular.app.verifyProfile.data.RegisterClientResponse
import dam.intermodular.app.verifyProfile.presentation.model.VerifyProfilModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiService {
    @POST("Usuario/logIn")
    suspend fun apiLogIn(@Body login: LogInModel): Response<LoginResponse>

    @POST("Usuario/registroUsuario")
    suspend fun apiAddUser(@Body register : RegisterModel) : Response<RegisterResponse>

    @POST("Usuario/verificarUsuario")
    suspend fun apiVerificationCode(@Body code: VerificationCodeModel):Response<VerificationCodeResponse>

    @Multipart
    @POST("Cliente/registrarCliente")
    suspend fun apiRegisterProfile(
        @Part("idUsuario") idUsuario: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("apellido") apellido: RequestBody,
        @Part("dni") dni: RequestBody,
        @Part("rol") rol: RequestBody,
        @Part("date") date: RequestBody,
        @Part("ciudad") ciudad: RequestBody,
        @Part("sexo") sexo: RequestBody,
        @Part picture: MultipartBody.Part?
    ) : Response<RegisterClientResponse>

    @GET("Habitacion/habitaciones")
    suspend fun getHabitaciones(): Response<List<Habitacion>>
}
