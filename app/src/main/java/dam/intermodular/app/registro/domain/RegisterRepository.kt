package dam.intermodular.app.registro.domain

import dam.intermodular.app.core.api.ApiService
import dam.intermodular.app.core.dataStore.DataStoreManager
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {
}