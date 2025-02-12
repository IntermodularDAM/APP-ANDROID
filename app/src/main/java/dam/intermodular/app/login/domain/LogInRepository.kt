package dam.intermodular.app.login.domain

import android.util.Log
import dam.intermodular.app.core.api.ApiService
import dam.intermodular.app.core.dataStore.DataStoreManager
import dam.intermodular.app.login.data.LoginResponse
import dam.intermodular.app.login.presentation.model.LogInModel
import javax.inject.Inject

class LogInRepository @Inject constructor (
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
){
    suspend fun loginUserRepository(logInModel: LogInModel): Result<LoginResponse> {
        return  try{
            val response = apiService.apiLogIn(logInModel)
            if (response.isSuccessful){
                Log.d("Repository: Api Response","${response.body()}")
                val loginResponse = response.body()
                if(loginResponse != null){
                    dataStoreManager.guardarTokens(
                        loginResponse.data.token,
                        loginResponse.data.appToken,
                        loginResponse.data.user.rol
                    )
                    Result.success(loginResponse)
                }else{
                    Log.e("Login Error", "Respuesta vacía recibida del servidor")
                    Result.failure(Exception("Respuesta vacía recibido del servidor"))
                }
            }else{
                Log.e("Login Error", "Error de respuesta del servidor: Código ${response.code()}, Mensaje: ${response.message()}")
                Result.failure(Exception("Respuesta vacía recibido del servidor"))
            }
        }catch (e: Exception){
            Log.e("Login Error", "Error en la llamada a la API: ${e.message}", e)
            Result.failure(e)
        }
    }
}