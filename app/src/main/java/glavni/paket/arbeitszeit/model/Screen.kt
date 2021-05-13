package glavni.paket.arbeitszeit.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    val route:String,
    val icon: ImageVector
){
    object Home:Screen("home", Icons.Default.Home)
    object Hours:Screen("hours", Icons.Default.AccessTime)
    object Setting:Screen("setting", Icons.Default.Settings)
}