package com.example.warre_welvaert_project_android.ui.pet

import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import com.example.warre_welvaert_project_android.model.Pet
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.SimpleRadioButtonComponent
import com.example.warre_welvaert_project_android.ui.TextInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.math.min

@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: AddPetViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val dbViewModel: PetDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val ctx = LocalContext.current

    val optionsPet = listOf("cat", "dog")
    var animal by remember { mutableStateOf(optionsPet[optionsPet.lastIndex]) }
    var name by remember { mutableStateOf("") }

    val onAnimalSelection: (String) -> Unit = { value -> animal = value }
    val onNameSelection: (String) -> Unit = { value -> name = value }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = modifier.height(30.dp))
            AddPetPicture(modifier = modifier, viewModel = viewModel)
            Spacer(modifier = modifier.height(30.dp))
            TextInputField(label = stringResource(R.string.pet_add_name) , defaultText = "", onValueChanged = onNameSelection)
            Spacer(modifier = modifier.height(30.dp))
            Text(text = stringResource(R.string.pet_add_animal), fontSize = 18.sp)
            Row() {
                SimpleRadioButtonComponent(options = optionsPet, onSelectionChanged = onAnimalSelection)
            }
            Spacer(modifier = modifier.height(30.dp))
            Text(text = stringResource(R.string.pet_add_time), fontSize = 18.sp)
            Spacer(modifier = modifier.height(15.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { viewModel.addTimeSlot(LocalTime.NOON) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Timeslot")
                }
                Spacer(modifier = modifier.width(20.dp))
                Text(text = "Time Slots", fontSize = 18.sp )
                Spacer(modifier = modifier.width(20.dp))
                Button(onClick = {
                    if (viewModel.getAllTimeSlots().isNotEmpty()) {
                        viewModel.removeTimeSlot(viewModel.getAllTimeSlots().lastIndex)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Timeslot")
                }
            }
        }
        items(viewModel.getAllTimeSlots().size) { index ->
            TimeSelector(
                index = index,
                viewModel = viewModel
            )
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    val pet = Pet(
                        name = name,
                        animal = animal,
                        feedingTimes = viewModel.getAllTimeSlots(),
                        image = viewModel.getImage()
                    )
                    if (name.isBlank()) {
                        Toast.makeText(ctx, "Pet name cannot be empty.", Toast.LENGTH_LONG).show()
                    } else {
                        coroutineScope.launch {
                            viewModel.addPetToDb(dbViewModel = dbViewModel, pet = pet)
                        }
                        navController.navigate("pet_overview")
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "confirm pet")
                Text(text = "Confirm",
                    modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 7.dp, bottom = 7.dp),
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(75.dp))
        }
    }
}

@Composable
private fun TimeSelector(
    index: Int,
    viewModel: AddPetViewModel
) {
    val ctx = LocalContext.current
    val initTime = LocalTime.NOON

    val timePickerDialog = TimePickerDialog(
        ctx, {_, hours : Int, minutes: Int ->
            val slot = viewModel.convertIntToTime(hour = hours, minute = minutes)
            viewModel.updateTimeSlot(id = index, newSlot = slot)
        }, initTime.hour, initTime.minute, true
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { timePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            modifier = Modifier.width(150.dp)
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "time slot", tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text(viewModel.getTimeSlot(index).toString(), fontSize = 20.sp, fontWeight = FontWeight.Normal, color = Color.White)
        }
    }
}

@Composable
private fun AddPetPicture(modifier: Modifier = Modifier, viewModel: AddPetViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val ctx = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    if (imageUri == null) {
        Icon(
            Icons.Filled.AccountCircle ,
            contentDescription = "select picture",
            modifier = modifier
                .size(140.dp)
                .clickable { galleryLauncher.launch("image/*") }
        )
    }
    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = null
            Log.e("Image Error", "Adding image is not supported on older android version.")
        } else {
            val source = ImageDecoder.createSource(ctx.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
        bitmap.value?.let { bitmap ->
            val size = min(bitmap.width, bitmap.height)
            val startX = (bitmap.width - size) / 2
            val startY = (bitmap.height - size) / 2
            val squareBitmap = Bitmap.createBitmap(bitmap, startX, startY, size, size)
            if (squareBitmap.byteCount > 10000000) {
                imageUri = null
                Toast.makeText(ctx, "Picture size is to large.", Toast.LENGTH_LONG).show()
            }
            else {
                viewModel.setImage(squareBitmap)
                Image(
                    bitmap = squareBitmap.asImageBitmap(),
                    filterQuality = FilterQuality.None,
                    contentDescription = "pet picture",
                    modifier = modifier
                        .height(140.dp)
                        .clickable { galleryLauncher.launch("image/*") }
                        .clip(RoundedCornerShape(50))
                )
            }
        }
    }
}
