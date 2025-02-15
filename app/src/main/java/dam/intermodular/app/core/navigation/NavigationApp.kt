package dam.intermodular.app.core.navigation

import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import dam.intermodular.app.habitaciones.FavoritesScreen
import dam.intermodular.app.habitaciones.HabitacionesViewModel
import dam.intermodular.app.habitaciones.MainScreen
import dam.intermodular.app.habitaciones.RoomDetailsFragment

import dam.intermodular.app.home.presentation.views.HomeScreen
import dam.intermodular.app.login.presentation.viewModel.LoginViewModel
import dam.intermodular.app.login.presentation.views.LoginScreen
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.view.InfoReservaScreen
import dam.intermodular.app.reservas.view.ModificarReservaScreen
import dam.intermodular.app.reservas.view.ReservasScreen


@Composable
fun NavigationApp(){
    val navController = rememberNavController()
    val viewModelLogin : LoginViewModel = viewModel()
    val habitacionesViewModel: HabitacionesViewModel = viewModel()
    val authState by viewModelLogin.authState.collectAsState()
    val token by viewModelLogin.authState.collectAsState()
    val isChekingToken by viewModelLogin.isCheckingToken.collectAsState()
    val isVisible =

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
        composable("main_screen") {
            MainScreen(navController, habitacionesViewModel)
        }
        composable("favorites_screen") {
            FavoritesScreen(navController, habitacionesViewModel)
        }
        composable("reservas_screen"){
            ReservasScreen(navController)
        }
        composable(
            "infoReserva/{reservaJson}",
            arguments = listOf(navArgument("reservaJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val reservaJson = backStackEntry.arguments?.getString("reservaJson")
            val reserva = Gson().fromJson(reservaJson, Reservas::class.java)
            InfoReservaScreen(navController, reserva)
        }

        composable(
            "modificarReserva/{reservaJson}",
            arguments = listOf(navArgument("reservaJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val reservaJson = backStackEntry.arguments?.getString("reservaJson")
            val reserva = Gson().fromJson(reservaJson, Reservas::class.java)
            ModificarReservaScreen(navController, reserva)
        }

        composable(
            "room_details_screen/{roomName}/{roomDescription}/{roomPrice}/{roomOption}/{roomImage}/{previousScreen}",
            arguments = listOf(
                navArgument("roomName") { type = NavType.StringType },
                navArgument("roomDescription") { type = NavType.StringType },
                navArgument("roomPrice") { type = NavType.StringType },
                navArgument("roomOption") { type = NavType.StringType },
                navArgument("roomImage") { type = NavType.StringType },
                navArgument("previousScreen") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val roomDescription = backStackEntry.arguments?.getString("roomDescription") ?: ""
            val roomPrice = backStackEntry.arguments?.getString("roomPrice") ?: ""
            val roomOption = backStackEntry.arguments?.getString("roomOption") ?: ""
            val roomImage = backStackEntry.arguments?.getString("roomImage") ?: ""
            val previousScreen = backStackEntry.arguments?.getString("previousScreen") ?: "main_screen"

            RoomDetailsFragment(
                navController = navController,
                roomName = roomName,
                roomDescription = roomDescription,
                roomPrice = roomPrice,
                roomOption = roomOption,
                roomImage = roomImage,
                previousScreen = previousScreen
            )
        }

    }
}