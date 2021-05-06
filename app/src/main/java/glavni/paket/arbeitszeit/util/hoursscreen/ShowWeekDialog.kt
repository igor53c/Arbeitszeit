package glavni.paket.arbeitszeit.util.hoursscreen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import glavni.paket.arbeitszeit.db.Day

@Composable
fun showWeekDialog(days: List<Day>?, showDialog: Boolean): Boolean {
    var showDialogForWeek by remember { mutableStateOf(showDialog) }
    AlertDialog(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        title = {
            Text(
                text = "Select week:",
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            if(days != null) {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.7f)
                ) {
                    itemsIndexed(
                        items = days,
                        itemContent = { index, day ->
                            AnimatedListWeek(day = day)
                        })
                }
            }
        },
        buttons = {
            TextButton(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp),
                onClick = { showDialogForWeek = false }
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.h6
                )
            }
        },
        onDismissRequest = { showDialogForWeek = false },
        shape = RoundedCornerShape(16.dp)
    )
    return showDialogForWeek
}