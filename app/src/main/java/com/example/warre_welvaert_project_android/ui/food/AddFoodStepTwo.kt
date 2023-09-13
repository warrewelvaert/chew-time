package com.example.warre_welvaert_project_android.ui.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.DropDownMenu
import com.example.warre_welvaert_project_android.ui.ErrorScreen
import com.example.warre_welvaert_project_android.ui.LoadingScreen
import com.example.warre_welvaert_project_android.ui.NumberInputField
import com.example.warre_welvaert_project_android.R

@Composable
fun AddFoodSecondStep(
    modifier: Modifier = Modifier,
    navController: NavController,
    foodAnimal: String?,
    foodType: String?
){
    val apiViewModel: FoodApiViewModel = viewModel(factory = AppViewModelProvider.Factory)
    PetFoodScreen(
        foodUiState = apiViewModel.foodUiState,
        navController = navController,
        modifier = modifier,
        animal = foodAnimal,
        type = foodType
    )
}

@Composable
private fun PetFoodScreen(
    foodUiState: FoodUiState,
    modifier: Modifier = Modifier,
    navController: NavController,
    animal: String?,
    type: String?
) {
    when (foodUiState) {
        is FoodUiState.Loading -> LoadingScreen()
        is FoodUiState.Error -> ErrorScreen()
        is FoodUiState.Success -> {
            AddFoodSecondStepSuccess(
                modifier = modifier,
                navController = navController,
                foods = when(animal) {
                    "cat" -> when(type) {
                        "dry" -> foodUiState.dryCatFoods
                        "wet" -> foodUiState.wetCatFoods
                        else -> throw IllegalArgumentException()
                    }
                    "dog" -> when(type) {
                        "dry" -> foodUiState.dryDogFoods
                        "wet" -> foodUiState.wetDogFoods
                        else -> throw IllegalArgumentException()
                    }
                    else -> throw IllegalArgumentException()
                }
            )
        }
    }
}

@Composable
private fun AddFoodSecondStepSuccess(
    modifier: Modifier = Modifier,
    navController: NavController,
    foods: List<Food>
) {
    val dbViewModel: FoodDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val viewModel: FoodDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val ctx = LocalContext.current

    var food by remember { mutableStateOf(foods.first()) }
    var foodAmount by remember { mutableStateOf("") }
    var foodConsumption by remember { mutableStateOf("") }

    val onNameSelection: (Food) -> Unit = { value -> food = value }
    val onAmountChanged: (String) -> Unit = { value -> foodAmount = value }
    val onConsumptionChanged: (String) -> Unit = { value -> foodConsumption = value }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.food_add_name), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(30.dp))
        DropDownMenu( options = foods, modifier = modifier, onSelectionChanged = onNameSelection )
        Spacer(modifier = Modifier.height(30.dp))
        NumberInputField(label = stringResource(id = R.string.food_add_amount), default = "", onValueChanged = onAmountChanged)
        Spacer(modifier = Modifier.height(15.dp))
        NumberInputField(label = stringResource(id = R.string.food_add_consumption), default = "", onValueChanged = onConsumptionChanged)
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                if ( viewModel.addFoodToDb(
                        food = food,
                        amount = foodAmount,
                        consumption = foodConsumption,
                        dbViewModel = dbViewModel,
                        ctx = ctx
                )) navController.navigate("food_overview")
            }
        ) {
            Text(text = stringResource(id = R.string.add),
                modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 3.dp, bottom = 3.dp),
                fontSize = 17.sp
            )
        }
        Spacer(modifier = modifier.height(100.dp))
    }
}