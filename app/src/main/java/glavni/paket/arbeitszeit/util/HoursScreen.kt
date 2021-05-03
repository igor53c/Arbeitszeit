package glavni.paket.arbeitszeit.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.LifecycleOwner
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.ui.theme.RedInit
import glavni.paket.arbeitszeit.ui.viewwmodels.MainViewModel

@Composable
fun HoursScreen(lifecycleOwner: LifecycleOwner, viewModel: MainViewModel = hiltNavGraphViewModel()) {
    val days = viewModel.getAllDays.observeAsState().value
    val buttons = listOf("2021", "", "+7:00", "April", "Week 15", "35:00")
    Column {
        var animationIndex by remember { mutableStateOf(0) }
        VerticalGrid(columns = 3, modifier = Modifier.padding(vertical = 12.dp)) {
            buttons.forEachIndexed { index, title ->
                ButtonChip(
                    selected = index == animationIndex,
                    text = title,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(onClick = {
                            animationIndex = index
                        })
                )
            }
        }
        if(days != null) {
            LazyColumn {
                itemsIndexed(
                    items = days,
                    itemContent = { index, hour ->
                        AnimatedListItem(hour = hour, 0, 0)
                    })
            }
        }
    }
}

@Composable
fun AnimatedListItem(hour: Day?, itemIndex: Int, animationIndex: Int) {
            val animatedProgress = remember { Animatable(initialValue = 300f) }
            LaunchedEffect(Unit) {
                animatedProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
    Modifier
        .padding(8.dp)
        .graphicsLayer(translationX = animatedProgress.value)
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        ) {
            Text(
                text = hour?.timeLogIn.toString(),
                style = typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = hour?.timeLogOut.toString(),
                style = typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun VerticalGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        // Keep given height constraints, but set an exact width
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )
        // Measure each item with these constraints
        val placeables = measurables.map { it.measure(itemConstraints) }
        // Track each columns height so we can calculate the overall height
        val columnHeights = Array(columns) { 0 }
        placeables.forEachIndexed { index, placeable ->
            val column = index % columns
            columnHeights[column] += placeable.height
        }
        val height = (columnHeights.maxOrNull() ?: constraints.minHeight)
            .coerceAtMost(constraints.maxHeight)
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // Track the Y co-ord per column we have placed up to
            val columnY = Array(columns) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val column = index % columns
                placeable.place(
                    x = column * itemWidth,
                    y = columnY[column]
                )
                columnY[column] += placeable.height
            }
        }
    }
}
@Composable
fun ButtonChip(selected: Boolean, text: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.Transparent,
        contentColor = RedInit,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = RedInit
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = typography.body2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

    }
}