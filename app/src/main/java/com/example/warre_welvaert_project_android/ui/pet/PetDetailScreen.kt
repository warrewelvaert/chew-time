package com.example.warre_welvaert_project_android.ui.pet

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.model.Pet
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.SimpleAlertDialog
import kotlinx.coroutines.launch
import com.example.warre_welvaert_project_android.R

@Composable
fun PetDetailScreen(petId: Int?, navController: NavController, modifier: Modifier = Modifier) {
    val dbViewModel: PetDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
    if (petId != null) {
        val exPet = Pet(name = "", animal = "")
        val pet by dbViewModel.getById(petId).collectAsState(initial = exPet)
        PetDetailContent(pet = pet, modifier = modifier, dbViewModel = dbViewModel, navController = navController)
    }
}

@Composable
private fun PetDetailContent(
    pet: Pet?,
    modifier: Modifier = Modifier,
    dbViewModel: PetDbViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var showConfirm by remember { mutableStateOf(false) }
    if (pet != null) {
        val image = pet.image?.let { BitmapFactory.decodeByteArray(pet.image, 0, it.size) }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.pet_detail_name, pet.name, pet.animal),
                    fontSize = 25.sp, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = modifier.height(20.dp))
                if (image == null) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "",
                        modifier = modifier.size(140.dp)
                    )
                } else {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "",
                        modifier = modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
                Spacer(modifier = modifier.height(60.dp))
                Text(text = stringResource(id = R.string.pet_detail_time, pet.name), fontSize = 20.sp)
                Spacer(modifier = modifier.height(20.dp))
            }
            items(pet.feedingTimes.size) { index ->
                Text(
                    text = pet.feedingTimes[index].toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(top = 5.dp, bottom = 5.dp)
                )
            }
            item {
                Spacer(modifier = modifier.height(50.dp))
                Button(
                    onClick = { showConfirm = true },
                    modifier = modifier.size(50.dp),
                ) {
                    Icon(Icons.Filled.Delete, "delete food")
                }
                SimpleAlertDialog(
                    show = showConfirm,
                    onDismiss = {
                        showConfirm = false
                    },
                    onConfirm = {
                        try {
                            coroutineScope.launch {
                                navController.navigate("pet_overview")
                                dbViewModel.delete(pet = pet)
                            }
                        } catch (e: Exception) {
                            Log.e("DB Error", "Failed to remove pet from database.")
                        }
                        showConfirm = false
                    },
                    title = stringResource(id = R.string.pet_detail_alert_title),
                    text = stringResource(id = R.string.pet_detail_alert_text)
                )
            }
        }
    }
}