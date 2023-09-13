package com.example.warre_welvaert_project_android.ui.timer

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.warre_welvaert_project_android.model.Pet
import com.example.warre_welvaert_project_android.ui.AppViewModelProvider
import com.example.warre_welvaert_project_android.ui.EmptyScreen
import com.example.warre_welvaert_project_android.ui.SimpleAlertDialog
import com.example.warre_welvaert_project_android.ui.pet.PetDbViewModel
import java.time.Duration
import com.example.warre_welvaert_project_android.R

@Composable
fun TimerScreenOverview(modifier: Modifier = Modifier) {
    val dbViewModel: PetDbViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val pets by dbViewModel.getAllPets().collectAsState(emptyList())

    var petsWithFeedingTimes by remember { mutableStateOf<List<Pet>>(emptyList()) }
    petsWithFeedingTimes = pets.filter { it.feedingTimes.isNotEmpty() }

    LazyColumn {
        if (petsWithFeedingTimes.isEmpty()) {
            item {
                EmptyScreen(
                    title = stringResource(R.string.empty_title),
                    instructions = stringResource(R.string.empty_text_timer)
                )
            }
        } else {
            items(petsWithFeedingTimes) { pet ->
                TimerCard( pet = pet, modifier = modifier, viewModel = viewModel )
            }
        }
        item {
            Spacer(modifier = modifier.height(100.dp))
        }
    }
}

@Composable
private fun TimerCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    viewModel: TimerViewModel
) {
    val ctx = LocalContext.current
    val nextTimeSlot = viewModel.getNextTime(pet = pet)
    val duration: Duration?
    var showConfirm by remember { mutableStateOf(false) }

    var hours: Long = 0
    var minutes: Long = 0

    if (nextTimeSlot != null) {
        duration = viewModel.calculateTimeDifference(time = nextTimeSlot)
        hours = duration.toHours()
        minutes = duration.minusHours(hours).toMinutes()
    }
    Button (
        onClick = {
            showConfirm = true
        },
        modifier = modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "notify",
                modifier = modifier.size(45.dp))
            Column(
                modifier = modifier
                    .padding(15.dp)
            ) {
                Text(text = pet.name.uppercase(), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(text = stringResource(R.string.timer_countdown, hours, minutes), fontSize = 12.sp)
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
                if (nextTimeSlot != null) {
                    viewModel.createAlarmsForPet(ctx = ctx, pet = pet)
                } else {
                    Log.e("Alarm Error", "Could not create alarm because timeslot was not defined.")
                }
            } catch (e: Exception) {
                Log.e("Notification Error", "Failed to create alarm.")
            }
            showConfirm = false
        },
        title = stringResource(R.string.timer_reminder_title),
        text = stringResource(R.string.timer_reminder_text)
    )
}