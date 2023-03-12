package com.antisoftware.popreminder.screens.edit

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antisoftware.popreminder.R.drawable as AppIcon
import com.antisoftware.popreminder.R.string as AppText
import com.antisoftware.popreminder.common.composable.*
import com.antisoftware.popreminder.common.extension.*
import com.antisoftware.popreminder.data.Reminder
import com.antisoftware.popreminder.screens.map.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.maps.android.compose.*

@Composable
@ExperimentalMaterialApi
fun EditReminderScreen(
    reminderId: String,
    modifier: Modifier = Modifier,
    popUpScreen: () -> Unit,
    openScreen: (String) -> Unit,
    viewModel: EditReminderViewModel = hiltViewModel(),
    mapsViewModel: MapViewModel = hiltViewModel()
) {
    val reminder by viewModel.reminder

    LaunchedEffect(Unit) { viewModel.initialize(reminderId) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.edit_reminder,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen) },
            backAction = { viewModel.onBackClick(popUpScreen) }
        )

        Spacer(modifier = Modifier.spacer())

        BasicField(AppText.reminder, reminder.msg, viewModel::onTitleChange, Modifier.fieldModifier())
        DescriptionField(AppText.description, reminder.description, viewModel::onDescriptionChange, Modifier.fieldModifier())

        Spacer(modifier = Modifier.spacer())
        CardEditors(reminder, viewModel::onDateChange, viewModel::onTimeChange)
        Spacer(modifier = Modifier.spacer())
        BasicButton(text = AppText.location, modifier = Modifier.basicButton()) {
            viewModel.onMapsClick(openScreen)
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CardEditors(
    reminder: Reminder,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity

    RegularCardEditor(AppText.date, AppIcon.ic_calendar, reminder.dueDate, Modifier.card()) {
        showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, reminder.dueTime, Modifier.card()) {
        showTimePicker(activity, onTimeChange)
    }
}

private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
    }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
    val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
    }
}

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "PotentialBehaviorOverride")
//@Composable
//fun MapScreen(
//    reminderId: String,
//    popUpScreen: () -> Unit,
//    viewModel: EditReminderViewModel = hiltViewModel(),
//    mapViewModel: MapViewModel = hiltViewModel()
//) {
////    val uiState by viewModel.uiState
//    val reminder by viewModel.reminder
//
//    LaunchedEffect(Unit) { viewModel.initialize(reminderId) }
//
//    // Set properties using MapProperties which you can use to recompose the map
//    val mapProperties = MapProperties(
//        // Only enable if user has accepted location permissions.
//        isMyLocationEnabled = true,
//    )
//    val uiSettings = remember {
//        MapUiSettings(zoomControlsEnabled = false)
//    }
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { mapViewModel.onOkClick(popUpScreen) },
//                backgroundColor = MaterialTheme.colors.secondary,
//                contentColor = MaterialTheme.colors.onPrimary,
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Icon(Icons.Filled.Check, "Ok/Continue")
//            }
//        }
//    ) {
//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            properties = mapProperties,
//            uiSettings = uiSettings,
//            onMapLongClick = {
//                viewModel.onMapLongClick(it)
//            }
//        ) {
//            var latitude by remember { mutableStateOf(0.0) }
//            var longitude by remember { mutableStateOf(0.0) }
//            if (reminder.latitude != null) {
//                latitude = reminder.latitude!!
//                longitude = reminder.longitude!!
//                Marker(
//                    state = MarkerState(position = LatLng(latitude, longitude)),
//                    title = "Parking spot (${reminder.latitude}, ${reminder.longitude})",
//                    snippet = "Long click to delete",
//                    onInfoWindowLongClick = {
//                        viewModel.onMarkerLongClick()
//                    },
//                    onClick = {
//                        it.showInfoWindow()
//                        true
//                    },
//                    icon = BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_GREEN
//                    )
//                )
//            }
//        }
//    }
//}
