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
import com.victor.isasturalmacen.screens.tools.AddNewToolScreen
import com.victor.isasturalmacen.screens.tools.ToolsScreen
import com.victor.isasturalmacen.viewModels.authentication.Destination
import com.victor.isasturalmacen.viewModels.authentication.LoginViewModel

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
             ProductsScreen(navigateToWires = {},
                 navigateToMagneto = {},
                 navigateToContactors = {},
                 navigateToPowerSupply = {},
                 navigateToCabinet = {},
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
    }
}