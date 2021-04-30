package glavni.paket.arbeitszeit.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen (
    val route:String,
    val label:String,
    val icon: ImageVector
){
    object Home:Screen("home","Home", Icons.Default.Home)
    object Hours:Screen("hours","Hours", Icons.Default.AccessTime)
    object Setting:Screen("setting","Setting", Icons.Default.Settings)
}