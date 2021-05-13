package glavni.paket.arbeitszeit.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel

@Composable
fun SettingScreen(viewModel: MainViewModel = hiltNavGraphViewModel()){
    var selectedLanguage by remember { mutableStateOf(viewModel.myPreference.getLanguages()) }
    var breakBelow6 by remember { mutableStateOf(viewModel.myPreference.getBreakBelow6()) }
    var break6And9 by remember { mutableStateOf(viewModel.myPreference.getBreak6And9()) }
    var breakOver9 by remember { mutableStateOf(viewModel.myPreference.getBreakOver9()) }
    var hoursPerWeek by remember { mutableStateOf(viewModel.myPreference.getHoursPerWeek()) }
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
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
                }
            )
            Text(
                text = "English",
                modifier = Modifier
                    .clickable(onClick = {
                        selectedLanguage = "English"
                    })
                    .padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            RadioButton(
                selected = selectedLanguage == "German",
                onClick = {
                    selectedLanguage = "German"
                }
            )
            Text(
                text = "German",
                modifier = Modifier
                    .clickable(onClick = {
                        selectedLanguage = "German"
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
        var breakBelow6String by remember { mutableStateOf(breakBelow6.toString()) }
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
                        breakBelow6 = test
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
        var break6And9String by remember { mutableStateOf(break6And9.toString()) }
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
                        break6And9 = test
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
        var breakOver9String by remember { mutableStateOf(breakOver9.toString()) }
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
                        breakOver9 = test
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
                selected = !rounding,
                onClick = {
                    rounding = false
                })
            Text(
                text = "No",
                modifier = Modifier
                    .clickable(onClick = {
                        rounding = false
                    })
                    .padding(start = 4.dp, end = 8.dp, top = 8.dp)
            )
            RadioButton(
                modifier = Modifier.padding(top = 8.dp),
                selected = rounding,
                onClick = {
                    rounding = true
                })
            Text(
                text = "Yes",
                modifier = Modifier
                    .clickable(onClick = {
                        rounding = true
                    })
                    .padding(start = 4.dp, end = 8.dp, top = 8.dp)
            )
        }
        var hoursPerWeekString by remember { mutableStateOf(hoursPerWeek.toString()) }
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
                        hoursPerWeek = test
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
        Box (
            modifier = Modifier
                .fillMaxWidth()
                ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(80.dp)
                    .width(80.dp),
                onClick = {
                    viewModel.myPreference.setLanguages(selectedLanguage)
                    viewModel.myPreference.setBreakBelow6(breakBelow6)
                    viewModel.myPreference.setBreak6And9(break6And9)
                    viewModel.myPreference.setBreakOver9(breakOver9)
                    viewModel.myPreference.setRounding(rounding)
                    viewModel.myPreference.setHoursPerWeek(hoursPerWeek)
                    updateCompleteDb(viewModel)
                }
            ) {
                Icon(
                    Icons.Default.Save,
                    contentDescription = "Save",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(58.dp))
    }
}