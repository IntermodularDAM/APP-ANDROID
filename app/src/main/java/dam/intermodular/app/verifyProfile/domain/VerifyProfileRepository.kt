package dam.intermodular.app.verifyProfile.domain

import android.util.Log
import dam.intermodular.app.core.api.ApiService
import dam.intermodular.app.verifyProfile.data.RegisterClientResponse
import dam.intermodular.app.verifyProfile.presentation.model.VerifyProfilModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class VerifyProfileRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun sendVerifyProfileRepository(
        user: VerifyProfilModel,
        picture: MultipartBody.Part?
    ) : Result<RegisterClientResponse>{
        return  try {


            Log.d("Repostory ON","Entro")
            val idUsuario = user.idUsuario.toRequestBody("text/plain".toMediaTypeOrNull())
            val nombre = user.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
            val apellido = user.apellido.toRequestBody("text/plain".toMediaTypeOrNull())
            val rol = user.rol.toRequestBody("text/plain".toMediaTypeOrNull())
            val dni = user.dni.toRequestBody("text/plain".toMediaTypeOrNull())
            val date = user.date.toRequestBody("text/plain".toMediaTypeOrNull())
            val ciudad = user.ciudad.toRequestBody("text/plain".toMediaTypeOrNull())
            val sexo = user.sexo.toRequestBody("text/plain".toMediaTypeOrNull())


            Log.d("API_CALL", "idUsuario: ${idUsuario.contentType()} - ${idUsuario.contentLength()}")
            Log.d("API_CALL", "nombre: ${nombre.contentType()} - ${nombre.contentLength()}")
            Log.d("API_CALL", "apellido: ${apellido.contentType()} - ${apellido.contentLength()}")
            Log.d("API_CALL", "Imagen: ${picture?.body?.contentType()} - ${picture?.body?.contentLength()}")
            val response = apiService.apiRegisterProfile(idUsuario, nombre, apellido, dni,rol, date, ciudad, sexo, picture)
            if(response.isSuccessful){
                Log.d("Repository: VerifyProfile","${response.body()}")
                val registerClientResponse = response.body()

                if(registerClientResponse != null){
                    Result.success(registerClientResponse)
                }else{
                    Log.e("VerificationCode Error", "Respuesta vacía recibida del servidor")
                    Result.failure(Exception("Respuesta vacía recidida del seervidor"))
                }
            }else{
                Log.e("VerifyProfile Error", "Error de respuesta del servidor: Código ${response.code()}, Mensaje: ${response.message()}")
                Result.failure(Exception("Respuesta vacía recibido del servidor"))
            }
        }catch (e: Exception){
            Log.e("VerifyProfile Error", "Error en la llamada a la API: ${e.message}", e)
            Result.failure(e)
        }
    }
}