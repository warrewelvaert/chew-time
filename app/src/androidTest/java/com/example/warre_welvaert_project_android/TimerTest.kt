package com.example.warre_welvaert_project_android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.warre_welvaert_project_android.model.Pet
import com.example.warre_welvaert_project_android.ui.timer.TimerViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class TimerUnitTest {

    private val timerViewModel: TimerViewModel = TimerViewModel()
    private val currentTime = LocalTime.now()
    private val timeOne = currentTime.plusHours(1)
    private val timeTwo = currentTime.minusMinutes(15)

    private val petOne = Pet(
        id = 0,
        name = "test pet",
        animal = "test cat",
        feedingTimes = listOf(),
        image = null
    )

    private val petTwo = Pet(
        id = 1,
        name = "test pet",
        animal = "test cat",
        feedingTimes = listOf(
            timeOne,
            timeTwo
        ),
        image = null
    )

    @Test
    fun nextTimeTest() {
        Assert.assertEquals(null, timerViewModel.getNextTime(pet = petOne))
        Assert.assertEquals(timeOne, timerViewModel.getNextTime(pet = petTwo))
    }

    @Test
    fun timeBetweenTest() {
        Assert.assertEquals(Duration.ZERO, timerViewModel.calculateTimeDifference(null))
        Assert.assertNotEquals(Duration.ZERO, timerViewModel.calculateTimeDifference(LocalTime.now().plusHours(1)))
    }
}