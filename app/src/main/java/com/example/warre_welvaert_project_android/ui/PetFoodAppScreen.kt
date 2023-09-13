package com.example.warre_welvaert_project_android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.ui.food.AddFoodScreen
import com.example.warre_welvaert_project_android.ui.food.AddFoodSecondStep
import com.example.warre_welvaert_project_android.ui.food.FoodDetailScreen
import com.example.warre_welvaert_project_android.ui.food.FoodOverviewScreen
import com.example.warre_welvaert_project_android.ui.pet.AddPetScreen
import com.example.warre_welvaert_project_android.ui.pet.PetDetailScreen
import com.example.warre_welvaert_project_android.ui.pet.PetOverviewScreen
import com.example.warre_welvaert_project_android.ui.timer.TimerScreenOverview

sealed class BottomNavigationItem(val route: String, val icon: Int) {
    data object Food : BottomNavigationItem("food_overview", R.drawable.nav_food)
    data object Pet : BottomNavigationItem("pet_overview", R.drawable.nav_pet)
    data object Timer : BottomNavigationItem("timer_overview", R.drawable.nav_clock)
}

@Composable
fun PetFoodTopAppBar(
    modifier: Modifier = Modifier,
    string: String
) {
    TopAppBar(
        title = { Text(string)},
        modifier = modifier
    )
}

@Composable
fun PetFoodTopAppBarWithNav(
    modifier: Modifier = Modifier,
    navController: NavController,
    string: String,
    route: String
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.navigate(route)}) {
                Icon(Icons.Filled.ArrowBack, "navigate back")
            }
        },
        title = { Text(string)},
        modifier = modifier
    )
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Food,
        BottomNavigationItem.Pet,
        BottomNavigationItem.Timer
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = screen.icon), contentDescription = null) },
                selected = currentRoute == screen.route,
                modifier = Modifier.padding(13.dp),
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

@Composable
fun PetFoodApp() {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val topBar: @Composable () -> Unit = when (currentDestination?.destination?.route) {
        ("food_detail/{foodId}" ) -> { {
            PetFoodTopAppBarWithNav(
                navController = navController,
                string = stringResource(id = R.string.nav_food_detail),
                route = "food_overview")
        } }
        ("food_add" ) -> { {
            PetFoodTopAppBarWithNav(
                navController = navController,
                string = stringResource(id = R.string.nav_food_add),
                route = "food_overview")
        } }
        ("food_add_step_two/{animal}/{type}" ) -> { {
            PetFoodTopAppBarWithNav(
                navController = navController,
                string = stringResource(id = R.string.nav_food_add),
                route = "food_add")
        } }
        ("pet_add" ) -> { {
            PetFoodTopAppBarWithNav(
                navController = navController,
                string = stringResource(id = R.string.nav_pet_add),
                route = "pet_overview")
        } }
        ("pet_detail/{petId}" ) -> { {
            PetFoodTopAppBarWithNav(
                navController = navController,
                string = stringResource(id = R.string.nav_pet_detail),
                route = "pet_overview")
        } }
        ("food_overview") -> { { PetFoodTopAppBar( string  = stringResource(id = R.string.nav_food)) } }
        ("pet_overview") -> { { PetFoodTopAppBar( string  = stringResource(id = R.string.nav_pet)) } }
        ("timer_overview") -> { { PetFoodTopAppBar( string  = stringResource(id = R.string.nav_timer)) } }
        else -> { { PetFoodTopAppBar( string  = stringResource(id = R.string.app_name)) } }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { topBar.invoke() },
        bottomBar = { BottomNavigation(navController) },
        drawerContent = { DrawerMenu(navController = navController, scaffoldState = scaffoldState) }
    ) { contentPadding -> Modifier.padding(contentPadding)
        NavHost(navController, startDestination = BottomNavigationItem.Pet.route) {
            composable(BottomNavigationItem.Food.route) { FoodOverviewScreen(navController = navController) }
            composable(BottomNavigationItem.Pet.route) { PetOverviewScreen(navController = navController) }
            composable(BottomNavigationItem.Timer.route) { TimerScreenOverview() }
            composable(
                "food_detail/{foodId}",
                arguments = listOf(navArgument("foodId") { type = NavType.IntType })
            ) {  backStackEntry ->
                FoodDetailScreen(
                    foodId = backStackEntry.arguments?.getInt("foodId"),
                    navController = navController
                )
            }
            composable(
                "food_add_step_two/{animal}/{type}",
                arguments = listOf(
                    navArgument("animal") { type = NavType.StringType },
                    navArgument("type") { type = NavType.StringType  }
                ),
            ) {  backStackEntry ->
                AddFoodSecondStep(
                    foodAnimal = backStackEntry.arguments?.getString("animal"),
                    foodType = backStackEntry.arguments?.getString("type"),
                    navController = navController
                )
            }
            composable("food_add") { AddFoodScreen(navController = navController) }
            composable("pet_add") { AddPetScreen(navController = navController) }
            composable("pet_detail/{petId}",
                arguments = listOf(navArgument("petId") { type = NavType.IntType })
            ) { backStackEntry ->
                PetDetailScreen(
                    petId = backStackEntry.arguments?.getInt("petId"),
                    navController = navController)
            }
        }
    }
}