package com.example.warre_welvaert_project_android.ui.food

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.ui.SimpleRadioButtonComponent

@Composable
fun AddFoodScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val optionsPet = listOf("dog", "cat")
    val optionsType = listOf("dry", "wet")
    var foodType by remember { mutableStateOf(optionsType[optionsType.lastIndex]) }
    var foodAnimal by remember { mutableStateOf(optionsPet[optionsPet.lastIndex]) }

    val onTypeSelection: (String) -> Unit = { value -> foodType = value }
    val onAnimalSelection: (String) -> Unit = { value -> foodAnimal = value }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.food_add_pet), fontSize = 18.sp)
        SimpleRadioButtonComponent(options = optionsPet, onSelectionChanged = onAnimalSelection)
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = stringResource(id = R.string.food_add_type), fontSize = 18.sp)
        SimpleRadioButtonComponent(options = optionsType, onSelectionChanged = onTypeSelection)

        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = {
                navController.navigate("food_add_step_two/${foodAnimal}/${foodType}")
            }
        ) {
            Text(
                text = stringResource(id = R.string.confirm),
                modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 7.dp, bottom = 7.dp),
                fontSize = 17.sp
            )
        }
        Spacer(modifier = modifier.height(100.dp))
    }
}