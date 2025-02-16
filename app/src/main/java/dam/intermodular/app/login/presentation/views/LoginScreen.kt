package dam.intermodular.app.login.presentation.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dam.intermodular.app.core.navigation.Register
import dam.intermodular.app.login.presentation.composables.EmailTextFiel
import dam.intermodular.app.login.presentation.composables.PasswordTextField

import dam.intermodular.app.login.presentation.viewModel.LoginViewModel

@Composable
fun LoginScreen(navigationTo : NavController, viewModel: LoginViewModel){


    val email: String by viewModel.email.collectAsState()
    val password:  String by viewModel.password.collectAsState()
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =  Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Login",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Email",
            fontSize = 20.sp
        )
        EmailTextFiel(email) { newEmail ->
            viewModel.onLoginChange(newEmail, password)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Password",
            fontSize = 20.sp
        )
        PasswordTextField (password) { newPassword ->
            viewModel.onLoginChange(email,newPassword)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {  viewModel.loginUser(navigationTo) }
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                fontSize = 16.sp,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { navigationTo.navigate(Register) }
        )
        Spacer(modifier = Modifier.weight(1f))


    }
}