package dam.intermodular.app.core.navigation

import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dam.intermodular.app.home.presentation.views.HomeScreen
import dam.intermodular.app.login.presentation.viewModel.LoginViewModel
import dam.intermodular.app.login.presentation.views.LoginScreen


@Composable
fun NavigationApp(){
    val navController = rememberNavController()
    val  viewModelLogin : LoginViewModel = viewModel()
    val authState by viewModelLogin.authState.collectAsState()
    val token by viewModelLogin.authState.collectAsState()
    val isChekingToken by viewModelLogin.isCheckingToken.collectAsState()


    NavHost(navController=navController, startDestination = Login)
    {


        composable<Login> {
            LoginScreen(
                navigationTo = navController,
                viewModel = viewModelLogin
            )
        }
        composable<Home>{
            HomeScreen{ name -> navController.navigate(Detail(name = name))}
        }

    }
}