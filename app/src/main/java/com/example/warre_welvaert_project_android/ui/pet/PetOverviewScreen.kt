package com.example.warre_welvaert_project_android.ui.pet

import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.model.Pet
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.EmptyScreen

@Composable
fun PetOverviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val dbViewModel: PetDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val pets by dbViewModel.getAllPets().collectAsState(emptyList())
    LazyColumn {
        if (pets.isEmpty()) {
            item {
                EmptyScreen(
                    title = stringResource(id = R.string.empty_title),
                    instructions = stringResource(id = R.string.empty_text_pet)
                )
            }
        } else {
            items(pets) { pet ->
                PetOverviewCard(modifier, pet = pet, navController = navController)
            }
        }
        item {
            Spacer(modifier = modifier.height(100.dp))
        }
    }
    FloatingActionButton(
        onClick = { navController.navigate("pet_add") },
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
private fun PetOverviewCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    navController: NavController
) {
    val viewModel: AddPetViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val image = pet.image?.let { BitmapFactory.decodeByteArray(pet.image, 0, it.size) }
    val timeSlot = viewModel.getSoonestTime(pet)
    Button(modifier = modifier
        .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
        .height(100.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        onClick = { navController.navigate("pet_detail/${pet.id}") }){
        Row (modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (image == null) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "",
                    modifier = modifier.size(45.dp)
                )
            } else {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "",
                    modifier = modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(50))
                )
            }
            Spacer(modifier = modifier.width(5.dp))
            Column(modifier = modifier.padding(15.dp)) {
                Text(text = pet.name.uppercase(), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(text = stringResource(id = R.string.pet_overview_time, timeSlot), fontSize = 12.sp)
            }
        }
    }
}