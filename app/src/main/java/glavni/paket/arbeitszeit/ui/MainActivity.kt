package glavni.paket.arbeitszeit.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import glavni.paket.arbeitszeit.BaseApplication
import glavni.paket.arbeitszeit.model.Screen
import glavni.paket.arbeitszeit.ui.theme.*
import glavni.paket.arbeitszeit.ui.viewmodels.MainViewModel
import glavni.paket.arbeitszeit.util.HomeScreen
import glavni.paket.arbeitszeit.util.HoursScreen
import glavni.paket.arbeitszeit.util.SettingScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArbeitszeitTheme(){
                val navController = rememberNavController()
                val titless = remember{ mutableStateOf("Home") }
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold (
                        bottomBar = {
                            val items = listOf (
                                Screen.Home,
                                Screen.Hours,
                                Screen.Setting
                            )
                            BottomNavigation (
                                modifier = Modifier
                                    .background(color = MaterialTheme.colors.background),
                                backgroundColor = MaterialTheme.colors.background
                            ){
                                val navBackStackEntry by navController
                                    .currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry
                                    ?.arguments?.getString(KEY_ROUTE)
                                items.forEach {
                                    BottomNavigationItem(
                                        unselectedContentColor = MaterialTheme.colors.background,
                                        selectedContentColor = MaterialTheme.colors.primaryVariant,
                                        icon = {
                                            Icon(
                                            imageVector = it.icon,
                                            contentDescription = "Home"
                                        )},
                                        selected = currentRoute == it.route ,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(
                                                color = MaterialTheme.colors.primary,
                                                shape = RoundedCornerShape(20.dp)
                                            ),
                                        onClick = {
                                            navController.popBackStack(
                                                navController.graph.startDestination,
                                                false
                                            )
                                            if (currentRoute != it.route){
                                                navController.navigate(it.route)
                                            }
                                        }
                                    )
                                }
                            }

                        }
                    ){
                        SreenController(
                            navController = navController,
                            topTitleBar = titless
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SreenController(
    navController: NavHostController,
    topTitleBar: MutableState<String>
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    )
    {
        composable("home") {
            HomeScreen()
            topTitleBar.value = "Home"
        }
        composable("hours") {
            HoursScreen()
            topTitleBar.value = "Hours"
        }
        composable("setting") {
            SettingScreen()
            topTitleBar.value = "Setting"
        }
    }
}


