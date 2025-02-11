package dam.intermodular.app.core.dataStore

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import dam.intermodular.app.core.tinkCrypt.TinkManager
import javax.inject.Inject


class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val tinkManager= TinkManager(context)

    val favoritos: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[FAVORITES_KEY] ?: emptySet()  // Si no existe, retornamos un set vacío
    }

    suspend fun guardarTokens(accessToken: String, applicationToken: String, role: String){
        withContext(Dispatchers.IO){
            val encryptedAccessToken = tinkManager.aead.encrypt(accessToken.toByteArray(),null)
            val encryptedApplicationToken = tinkManager.aead.encrypt(applicationToken.toByteArray(),null)

            val encryptAccessString = Base64.encodeToString(encryptedAccessToken, Base64.DEFAULT)
            val encryptApplicationString = Base64.encodeToString(encryptedApplicationToken, Base64.DEFAULT)

            context.dataStore.edit { preference ->
                preference[ACCESS_TOKEN_KEY] = encryptAccessString
                preference[ACCESS_ROLE_KEY] = encryptApplicationString
                preference[APPLICATION_TOKEN_KEY] = role
            }
        }
    }

    fun getAccessToken(): Flow<String?>{
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]?.let { data ->
                val encryptedBytes = Base64.decode(data, Base64.DEFAULT)
                val decryptedBytes = tinkManager.aead.decrypt(encryptedBytes,null)
                String(decryptedBytes)
            }
        }
    }

    fun getApplicationToken(): Flow<String?>{
        return context.dataStore.data.map { preferences ->
            preferences[APPLICATION_TOKEN_KEY]?.let { dato ->
                val encryptedApplication = Base64.decode(dato, Base64.DEFAULT)
                val decryptedApplication = tinkManager.aead.decrypt(encryptedApplication, null)
                String(decryptedApplication)
            }
        }
    }

    fun getRole(): Flow<String?> {
        return context.dataStore.data.map { references ->
            references[ACCESS_ROLE_KEY]
        }
    }//val role = dataStoreManager.getRole().first()  Para obtener el valor una sola vez

    suspend fun deleteStore(){
        withContext(Dispatchers.IO){
            context.dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN_KEY)
                preferences.remove(APPLICATION_TOKEN_KEY)
                preferences.remove(ACCESS_ROLE_KEY)
            }
        }
    }

    // Función para agregar o quitar favoritos en el DataStore
    suspend fun toggleFavorite(id: String) {
        context.dataStore.edit { preferences ->
            // Obtenemos los favoritos actuales (si existen)
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()

            // Determinamos si agregar o quitar el ID del favorito
            val newFavorites = if (id in currentFavorites) {
                currentFavorites - id  // Si ya es favorito, lo quitamos
            } else {
                currentFavorites + id  // Si no es favorito, lo agregamos
            }

            // Guardamos el nuevo conjunto de favoritos
            preferences[FAVORITES_KEY] = newFavorites
        }
    }
}