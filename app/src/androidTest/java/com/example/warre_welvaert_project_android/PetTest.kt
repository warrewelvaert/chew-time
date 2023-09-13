package com.example.warre_welvaert_project_android

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.warre_welvaert_project_android.ui.pet.AddPetViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class PetTest {

    private val viewModel = AddPetViewModel()
    private val time = LocalTime.NOON

    @Test
    fun timeSlotAddTest() {
        Assert.assertEquals(0, viewModel.getAllTimeSlots().size)
        Assert.assertEquals(null, viewModel.getTimeSlot(1))
        viewModel.addTimeSlot(time)
        Assert.assertEquals(1, viewModel.getAllTimeSlots().size)
        Assert.assertEquals(null, viewModel.getTimeSlot(1))
        Assert.assertEquals(time, viewModel.getTimeSlot(0))
        Assert.assertEquals(time, viewModel.getAllTimeSlots()[0])
    }

    @Test
    fun timeSlotRemoveTest() {
        viewModel.addTimeSlot(time)
        Assert.assertEquals(time, viewModel.getTimeSlot(0))
        viewModel.removeTimeSlot(0)
        Assert.assertEquals(null, viewModel.getTimeSlot(0))
        Assert.assertEquals(0, viewModel.getAllTimeSlots().size)
    }

    @Test
    fun timeSlotUpdateTest() {
        viewModel.addTimeSlot(time)
        Assert.assertEquals(time, viewModel.getTimeSlot(0))
        viewModel.updateTimeSlot(0, time.plusHours(1))
        Assert.assertEquals(time.plusHours(1), viewModel.getTimeSlot(0))
        Assert.assertEquals(1, viewModel.getAllTimeSlots().size)
    }

    @Test
    fun timeConvertTest() {
        Assert.assertEquals(LocalTime.MIDNIGHT, viewModel.convertIntToTime(0, 0 ))
        Assert.assertEquals(LocalTime.NOON, viewModel.convertIntToTime(12, 0 ))
        Assert.assertEquals(LocalTime.NOON, viewModel.convertIntToTime(999, 999 ))
    }
}