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
import dam.intermodular.app.habitaciones.view.FavoritesScreen
import dam.intermodular.app.habitaciones.viewModel.HabitacionesViewModel
import dam.intermodular.app.habitaciones.view.MainScreen
import dam.intermodular.app.habitaciones.view.RoomDetailsFragment
import androidx.navigation.toRoute
import dam.intermodular.app.core.navigation.type.createNavType

import dam.intermodular.app.home.presentation.views.HomeScreen
import dam.intermodular.app.login.presentation.viewModel.LoginViewModel
import dam.intermodular.app.login.presentation.views.LoginScreen
import dam.intermodular.app.reservas.model.Reservas
import dam.intermodular.app.reservas.view.InfoReservaScreen
import dam.intermodular.app.reservas.view.ModificarReservaScreen
import dam.intermodular.app.reservas.view.ReservarHabitacionScreen
import dam.intermodular.app.reservas.view.ReservasScreen
import dam.intermodular.app.registro.presentation.view.RegisterScreen
import dam.intermodular.app.registro.presentation.viewModel.RegisterViewModel
import dam.intermodular.app.verificationCode.presentation.view.VerificationCodeScreen
import dam.intermodular.app.verificationCode.presentation.viewModel.VerificationCodeViewModel
import dam.intermodular.app.verifyProfile.presentation.view.VerifyProfileScreen
import dam.intermodular.app.verifyProfile.presentation.viewModel.VerifyProfileViewModel
import kotlin.reflect.typeOf


@Composable
fun NavigationApp(){
    val navController = rememberNavController()
    val viewModelLogin : LoginViewModel = viewModel()
    val viewModelRegister: RegisterViewModel = viewModel()
    val viewModelVerificationCode: VerificationCodeViewModel = viewModel()
    val viewModelVerifyProfile: VerifyProfileViewModel = viewModel()
    val habitacionesViewModel: HabitacionesViewModel = viewModel()
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
        composable<Register> {
            RegisterScreen(
                navigateTo = navController,
                viewModel = viewModelRegister
            )
        }
        composable<VerificationCode>(
            typeMap = mapOf(typeOf<VerificationData>() to createNavType<VerificationData>())
        ){ navBackStackEntry ->
            val verificationCode = navBackStackEntry.toRoute<VerificationCode>()
            VerificationCodeScreen(
                verificationData = verificationCode.verificationData,
                viewModel = viewModelVerificationCode,
                navigateTo = navController
            )
        }
        composable<VerifyProfile>(
            typeMap = mapOf(typeOf<VerifyData>() to createNavType<VerifyData>())
        ){ navBackStackEntry ->
            val verifyProfile = navBackStackEntry.toRoute<VerifyProfile>()
            VerifyProfileScreen(
                verifyData = verifyProfile.verifyData,
                viewModel = viewModelVerifyProfile,
                navigateTo = navController
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
            "reservar_habitacion/{roomId}/{roomName}/{roomPrice}/{maxHuespedes}/{usuarioId}",
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("roomName") { type = NavType.StringType },
                navArgument("roomPrice") { type = NavType.StringType },
                navArgument("maxHuespedes") { type = NavType.IntType },
                navArgument("usuarioId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val roomPrice = backStackEntry.arguments?.getString("roomPrice") ?: ""
            val maxHuespedes = backStackEntry.arguments?.getInt("maxHuespedes") ?: 1
            val usuarioId = backStackEntry.arguments?.getString("usuarioId") ?: ""

            ReservarHabitacionScreen(
                navController = navController,
                habitacionId = roomId,
                habitacionNombre = roomName,
                precioNoche = roomPrice,
                maxHuespedes = maxHuespedes,
                usuarioId = usuarioId
            )
        }

        composable(
            "room_details_screen/{roomId}/{roomName}/{roomDescription}/{roomPrice}/{maxHuespedes}/{roomOption}/{roomImage}/{previousScreen}"
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val roomDescription = backStackEntry.arguments?.getString("roomDescription") ?: ""
            val roomPrice = backStackEntry.arguments?.getString("roomPrice") ?: ""
            val maxHuespedes = backStackEntry.arguments?.getString("maxHuespedes") ?: ""
            val roomOption = backStackEntry.arguments?.getString("roomOption") ?: ""
            val roomImage = backStackEntry.arguments?.getString("roomImage") ?: ""
            val previousScreen = backStackEntry.arguments?.getString("previousScreen") ?: ""

            RoomDetailsFragment(
                navController = navController,
                roomId = roomId,
                roomName = roomName,
                roomDescription = roomDescription,
                roomPrice = roomPrice,
                maxHuespedes = maxHuespedes,
                roomOption = roomOption,
                roomImage = roomImage,
                previousScreen = previousScreen
            )
        }
    }
}
