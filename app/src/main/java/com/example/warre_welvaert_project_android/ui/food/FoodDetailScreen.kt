package com.example.warre_welvaert_project_android.ui.food

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.SimpleAlertDialog
import com.example.warre_welvaert_project_android.ui.theme.PastelTealDark
import kotlinx.coroutines.launch

@Composable
fun FoodDetailScreen(
        modifier: Modifier = Modifier,
        dbViewModel: FoodDbViewModel = viewModel(factory = AppViewModelProvider.Factory),
        foodId: Int?,
        navController: NavController
) {
    val fallbackFood = Food(0, "Default", "example.com", "dry", "cat", 1, 1)
    val food by dbViewModel.getById(foodId).collectAsState(initial = fallbackFood)
    Column( modifier = modifier.padding(25.dp)) {
        FoodDetailGeneral(food = food)
        FoodDetailAmount(food = food, dbViewModel = dbViewModel, navController = navController)
    }
}

@Composable
private fun FoodDetailGeneral(
    modifier: Modifier = Modifier,
    food: Food?
) {
    val viewModel: FoodDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
    if (food != null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth()) {
            Text(text = food.name.uppercase(), fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
            Row(modifier = modifier.padding(top = 50.dp, bottom = 5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.nav_food),
                    contentDescription = "animal for food",
                    modifier = modifier.size(80.dp),
                )
                Column(
                    modifier = modifier
                        .height(120.dp)
                        .padding(start = 20.dp, end = 0.dp, top = 0.dp, bottom = 15.dp)
                )
                {
                    Text(text = stringResource(id = R.string.food_detail_animal, food.animal.uppercase()), fontSize = 17.sp)
                    Text(text = stringResource(id = R.string.food_detail_amount, viewModel.getAmountLeft(food)), fontSize = 17.sp)
                    Text(text = stringResource(id = R.string.food_detail_consumption, food.dailyConsumption.toString()), fontSize = 17.sp)
                }
            }
            Text( text = stringResource(id = R.string.food_detail_days, viewModel.getDaysLeft(food)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FoodDetailAmount(
    modifier: Modifier = Modifier,
    food: Food,
    dbViewModel: FoodDbViewModel,
    navController: NavController
) {
    val viewModel: FoodDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
    var foodInput by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showConfirm by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {

        Spacer(modifier = modifier.height(25.dp))
        Text(stringResource(id = R.string.food_detail_add), fontSize = 18.sp)

        Row(modifier = modifier.padding(top = 5.dp, bottom = 35.dp)) {
            TextField(
                value = foodInput,
                onValueChange = { foodInput = it},
                modifier = modifier
                    .width(150.dp)
                    .height(50.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.updateFoodInDb(dbViewModel = dbViewModel,
                            food = food,
                            foodAmount = foodInput,
                            scope = coroutineScope,
                            ctx = ctx
                        )
                    }
                )
            )
            Spacer(modifier = modifier.width(5.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.updateFoodInDb(dbViewModel = dbViewModel,
                        food = food,
                        foodAmount = foodInput,
                        scope = coroutineScope,
                        ctx = ctx
                    )
                },
                modifier = modifier.height(50.dp))
            {
                Text(text = "Add", fontSize = 18.sp)
            }
        }
        Spacer(modifier = modifier.height(25.dp))
        Row {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(food.storeUrl)
                ctx.startActivity(intent) },
                colors = ButtonDefaults.buttonColors(backgroundColor = PastelTealDark),
                modifier = modifier.height(50.dp)
            ) {
                Text(text = stringResource(id = R.string.food_detail_order), fontSize = 18.sp, color = Color.White)
            }
            Spacer(modifier = modifier.width(20.dp))
            Button(
                onClick = { showConfirm = true },
                modifier = modifier.size(50.dp),
            ) {
                IconButton( onClick = { showConfirm = true } ) {
                    Icon(Icons.Filled.Delete, "delete food")
                }
            }
        }
        SimpleAlertDialog(
            show = showConfirm,
            onDismiss = {
                showConfirm = false
            },
            onConfirm = {
                try {
                    coroutineScope.launch {
                        navController.navigate("food_overview")
                        dbViewModel.delete(food = food)
                    }
                } catch (e: Exception) {
                    Log.e("DB Error", "Failed to remove food from database.")
                }
                showConfirm = false
            },
            title = stringResource(id = R.string.food_detail_alert_title),
            text = stringResource(id = R.string.food_detail_alert_text)
        )
    }
}
