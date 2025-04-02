package com.victor.isasturalmacen.navigation

import ProductsScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.victor.isasturalmacen.screens.authentication.AddUserScreen
import com.victor.isasturalmacen.screens.authentication.DeleteUserScreen
import com.victor.isasturalmacen.screens.authentication.HomeScreen
import com.victor.isasturalmacen.screens.authentication.LoginScreen
import com.victor.isasturalmacen.screens.authentication.ManageAccountScreen
import com.victor.isasturalmacen.screens.products.CabinetScreen
import com.victor.isasturalmacen.screens.products.NewCabinetScreen
import com.victor.isasturalmacen.screens.products.NewContactorScreen
import com.victor.isasturalmacen.screens.products.NewMagnetothermicScreen
import com.victor.isasturalmacen.screens.products.NewPowerSupplyScreen
import com.victor.isasturalmacen.screens.products.NewWireScreen
import com.victor.isasturalmacen.screens.products.WiresScreen
import com.victor.isasturalmacen.screens.tools.AddNewToolScreen
import com.victor.isasturalmacen.screens.tools.ToolsScreen
import com.victor.isasturalmacen.viewModels.authentication.Destination
import com.victor.isasturalmacen.viewModels.authentication.LoginViewModel
import com.victor.isotronalmacen.screeens.products.ContactorScreen
import com.victor.isotronalmacen.screeens.products.MagnetothermicScreen
import com.victor.isotronalmacen.screeens.products.PowerSupplyScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(loginViewModel:LoginViewModel= hiltViewModel()) {

    val startDestination =when(loginViewModel.checkDestinationLogin()){
        Destination.Home -> HomeScreen
        Destination.Login -> LoginScreen
    }

    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<LoginScreen> {
            LoginScreen(
                navigateToHome = { navHostController.navigate(HomeScreen) },
            ) { navHostController.navigate(NewUserScreen) }
        }
        composable<NewUserScreen> {
            AddUserScreen(navigateToBack = { navHostController.popBackStack() }) {
                navHostController.navigate(HomeScreen)
            }
        }
        composable<HomeScreen> {
            HomeScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToTools = {navHostController.navigate(ToolScreen)},
                navigateToProducts = {navHostController.navigate(ProductScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<ManageAccountScreen> {
            ManageAccountScreen(navigationToBack = {navHostController.popBackStack()},
                navigateToDeleteUser = {navHostController.navigate(DeleteUserScreen)},
                navigationToLogin = { navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }}){
                navHostController.navigate(HomeScreen)
            }
        }
        composable<DeleteUserScreen> {
            DeleteUserScreen(navigateToToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<ProductScreen> {
             ProductsScreen(navigateToWires = {navHostController.navigate(WireScreen)},
                 navigateToMagneto = {navHostController.navigate(MagnetothermicScreen)},
                 navigateToContactors = {navHostController.navigate(ContactorScreen)},
                 navigateToPowerSupply = {navHostController.navigate(PowerSupplyScreen)},
                 navigateToCabinet = {navHostController.navigate(CabinetScreen)},
                 navigateToBack = {navHostController.popBackStack()},
                 navigateToHome = {navHostController.navigate(HomeScreen)},
                 navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)}) {
                 navHostController.navigate(LoginScreen) {
                     popUpTo<LoginScreen>() { inclusive = true }
                 }
             }
        }
        composable<ToolScreen> {
            ToolsScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToAddTool = {navHostController.navigate(AddNewTool)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<AddNewTool>{
            AddNewToolScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<WireScreen> {
            WiresScreen(navigateToAddWire = {navHostController.navigate(NewWireScreen)},
                navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<NewWireScreen> {
            NewWireScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToWires = {navHostController.navigate(WireScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<MagnetothermicScreen> {
            MagnetothermicScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToNewMagneto = {navHostController.navigate(NewMagnetothermicScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<NewMagnetothermicScreen> {
            NewMagnetothermicScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToMagnetos = {navHostController.navigate(MagnetothermicScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<CabinetScreen> {
            CabinetScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToNewCabinet = {navHostController.navigate(NewCabinetScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<NewCabinetScreen> {
            NewCabinetScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToCabinets = {navHostController.navigate(CabinetScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<ContactorScreen> {
            ContactorScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToNewContactor = {navHostController.navigate(NewContactorScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<NewContactorScreen> {
            NewContactorScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToContactors = {navHostController.navigate(ContactorScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<PowerSupplyScreen> {
            PowerSupplyScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToNewPowerSupply = {navHostController.navigate(NewPowerSupplyScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
        composable<NewPowerSupplyScreen> {
            NewPowerSupplyScreen(navigateToBack = {navHostController.popBackStack()},
                navigateToHome = {navHostController.navigate(HomeScreen)},
                navigateToManageAccount = {navHostController.navigate(ManageAccountScreen)},
                navigateToPowerSupplies = {navHostController.navigate(PowerSupplyScreen)}) {
                navHostController.navigate(LoginScreen) {
                    popUpTo<LoginScreen>() { inclusive = true }
                }
            }
        }
    }
}