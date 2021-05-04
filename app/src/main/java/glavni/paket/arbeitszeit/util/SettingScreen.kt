package glavni.paket.arbeitszeit.util

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import timber.log.Timber

@Composable
fun SettingScreen(viewModel: MainViewModel = hiltNavGraphViewModel()){

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        var selectedLanguage by remember { mutableStateOf(viewModel.myPreference.getLanguages()) }
        Spacer(modifier = Modifier.size(16.dp))
        Row {
            Text(
                text = "Languages:",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            RadioButton(
                selected = selectedLanguage == "English",
                onClick = {
                    selectedLanguage = "English"
                    viewModel.myPreference.setLanguages(selectedLanguage)
                }
            )
            Text(
                text = "English",
                modifier = Modifier
                    .clickable(onClick = {
                        selectedLanguage = "English"
                        viewModel.myPreference.setLanguages(selectedLanguage)
                    })
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            RadioButton(
                selected = selectedLanguage == "German",
                onClick = {
                    selectedLanguage = "German"
                    viewModel.myPreference.setLanguages(selectedLanguage)
                }
            )
            Text(
                text = "German",
                modifier = Modifier
                    .clickable(onClick = {
                        selectedLanguage = "German"
                        viewModel.myPreference.setLanguages(selectedLanguage)
                    })
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        Text(
            text = "Break (in minutes):",
            modifier = Modifier
                .padding(start = 8.dp, top = 24.dp, end = 8.dp, bottom = 0.dp)
        )
        var breakBelow6 by remember { mutableStateOf(viewModel.myPreference.getBreakBelow6()) }
        var breakBelow6ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = breakBelow6.toString(),
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> { breakBelow6ErrorState = true }
                    test < 0 -> { breakBelow6ErrorState = true }
                    else -> {
                        breakBelow6 = test
                        breakBelow6ErrorState = false
                        viewModel.myPreference.setBreakBelow6(breakBelow6)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time below 6 hours") },
            isError = breakBelow6ErrorState,
            placeholder = { Text(text = "0", maxLines = 1) }
        )
        var break6And9 by remember { mutableStateOf(viewModel.myPreference.getBreak6And9()) }
        var break6And9ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = break6And9.toString(),
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> { break6And9ErrorState = true }
                    test < 0 -> { break6And9ErrorState = true }
                    else -> {
                        break6And9 = test
                        break6And9ErrorState = false
                        viewModel.myPreference.setBreak6And9(break6And9)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time between 6 and 9 hours") },
            isError = break6And9ErrorState,
            placeholder = { Text(text = "30") }
        )
        var breakOver9 by remember { mutableStateOf(viewModel.myPreference.getBreakOver9()) }
        var breakOver9ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = breakOver9.toString(),
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> { breakOver9ErrorState = true }
                    test < 0 -> { breakOver9ErrorState = true }
                    else -> {
                        breakOver9 = test
                        breakOver9ErrorState = false
                        viewModel.myPreference.setBreakOver9(breakOver9)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 16.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time over 9 hours") },
            isError = breakOver9ErrorState,
            placeholder = { Text(text = "45", maxLines = 1) }
        )
        var rounding by remember { mutableStateOf(viewModel.myPreference.getRounding()) }
        Row {
            Text(
                text = "Rounding up Log in\nand Log out for 15 min:",
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            )
            RadioButton(
                modifier = Modifier.padding(top = 8.dp),
                selected = rounding == false,
                onClick = {
                    rounding = false
                    viewModel.myPreference.setRounding(rounding)
                })
            Text(
                text = "No",
                modifier = Modifier
                    .clickable(onClick = {
                        rounding = false
                        viewModel.myPreference.setRounding(rounding)
                    })
                    .padding(start = 4.dp, end = 8.dp, top = 8.dp)
            )
            RadioButton(
                modifier = Modifier.padding(top = 8.dp),
                selected = rounding == true,
                onClick = {
                    rounding = true
                    viewModel.myPreference.setRounding(rounding)
                })
            Text(
                text = "Yes",
                modifier = Modifier
                    .clickable(onClick = {
                        rounding = true
                        viewModel.myPreference.setRounding(rounding)
                    })
                    .padding(start = 4.dp, end = 8.dp, top = 8.dp)
            )
        }
        var hoursPerWeek by remember { mutableStateOf(viewModel.myPreference.getHoursPerWeek()) }
        var hoursPerWeekErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = hoursPerWeek.toString(),
            onValueChange = {
                val test = it.toFloatOrNull()
                when {
                    test == null -> { hoursPerWeekErrorState = true }
                    test < 0.0f -> { hoursPerWeekErrorState = true }
                    else -> {
                        hoursPerWeek = test
                        hoursPerWeekErrorState = false
                        viewModel.myPreference.setHoursPerWeek(hoursPerWeek)
                    }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Hours required per week") },
            isError = hoursPerWeekErrorState,
            placeholder = { Text(text = "40,00", maxLines = 1) }
        )
    }
}