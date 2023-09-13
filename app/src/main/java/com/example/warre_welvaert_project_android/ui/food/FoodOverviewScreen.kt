package com.example.warre_welvaert_project_android.ui.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.EmptyScreen

@Composable
fun FoodOverviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dbViewModel: FoodDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val foods by dbViewModel.getAllFoods().collectAsState(emptyList())
    LazyColumn {
        if (foods.isEmpty()) {
            item {
                EmptyScreen(
                    title = stringResource(id = R.string.empty_title),
                    instructions = stringResource(id = R.string.empty_text_food)
                )
            }
        } else {
            items(foods) { food ->
                FoodOverviewCard(modifier = modifier, food = food, navController = navController)
            }
        }
        item {
            Spacer(modifier = modifier.height(100.dp))
        }
    }
    FloatingActionButton(
        onClick = { navController.navigate("food_add") },
        modifier = modifier.offset(300.dp, 550.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}

@Composable
fun FoodOverviewCard(
    modifier: Modifier = Modifier,
    food: Food,
    navController: NavController
) {
    val viewModel: FoodDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val resource = when (food.animal) {
        "cat" -> painterResource(id = R.drawable.cat)
        else -> painterResource(id = R.drawable.dog)
    }
    Button( modifier = modifier
        .height(120.dp)
        .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        onClick = {navController.navigate("food_detail/${food.id}")}
    ){
        Row(modifier = modifier
            .padding(top = 15.dp, bottom = 15.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = resource, contentDescription = "animal icon",
                modifier = modifier
                    .size(45.dp)
            )
            Spacer(modifier = modifier.width(15.dp))
            Column {
                Text(
                    text = food.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(id = R.string.food_overview_days, viewModel.getDaysLeft(food)),
                    fontSize = 12.sp
                )
            }
        }
    }
}