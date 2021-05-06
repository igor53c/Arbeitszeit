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
        var breakBelow6String by remember { mutableStateOf(viewModel.myPreference.getBreakBelow6().toString()) }
        var breakBelow6ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = breakBelow6String,
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> {
                        breakBelow6String = ""
                        breakBelow6ErrorState = true
                    }
                    test < 0 || test > 500 -> {
                        breakBelow6String = test.toString()
                        breakBelow6ErrorState = true
                    }
                    else -> {
                        breakBelow6String = test.toString()
                        breakBelow6ErrorState = false
                        viewModel.myPreference.setBreakBelow6(test)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time below 6 hours") },
            isError = breakBelow6ErrorState
        )
        var break6And9String by remember { mutableStateOf(viewModel.myPreference.getBreak6And9().toString()) }
        var break6And9ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = break6And9String,
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> {
                        break6And9String = ""
                        break6And9ErrorState = true
                    }
                    test < 0 || test > 500 -> {
                        break6And9String = test.toString()
                        break6And9ErrorState = true
                    }
                    else -> {
                        break6And9String = test.toString()
                        break6And9ErrorState = false
                        viewModel.myPreference.setBreak6And9(test)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time between 6 and 9 hours") },
            isError = break6And9ErrorState
        )
        var breakOver9String by remember { mutableStateOf(viewModel.myPreference.getBreakOver9().toString()) }
        var breakOver9ErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = breakOver9String,
            onValueChange = {
                val test = it.toIntOrNull()
                when {
                    test == null -> {
                        breakOver9String = ""
                        breakOver9ErrorState = true
                    }
                    test < 0 || test > 500 -> {
                        breakOver9String = test.toString()
                        breakOver9ErrorState = true
                    }
                    else -> {
                        breakOver9String = test.toString()
                        breakOver9ErrorState = false
                        viewModel.myPreference.setBreakOver9(test)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 16.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Working time over 9 hours") },
            isError = breakOver9ErrorState
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
        var hoursPerWeekString by remember { mutableStateOf(viewModel.myPreference.getHoursPerWeek().toString()) }
        var hoursPerWeekErrorState by remember { mutableStateOf(false)}
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            value = hoursPerWeekString,
            onValueChange = {
                val test = it.toFloatOrNull()
                when {
                    test == null -> {
                        hoursPerWeekString = ""
                        hoursPerWeekErrorState = true
                    }
                    test < 0.0 || test > 99.0 -> {
                        hoursPerWeekString = test.toString()
                        hoursPerWeekErrorState = true
                    }
                    else -> {
                        hoursPerWeekString = test.toString()
                        hoursPerWeekErrorState = false
                        viewModel.myPreference.setHoursPerWeek(test)
                    }
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Hours required per week") },
            isError = hoursPerWeekErrorState
        )
    }
}