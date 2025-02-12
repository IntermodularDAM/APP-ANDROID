package dam.intermodular.app.registro.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dam.intermodular.app.core.dataStore.DataStoreManager
import dam.intermodular.app.registro.domain.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val dataStoreManager: DataStoreManager,
    private val registerRepository: RegisterRepository
) :ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email : StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password : StateFlow<String> = _password

}