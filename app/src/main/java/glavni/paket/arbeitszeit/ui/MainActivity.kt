package glavni.paket.arbeitszeit.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import glavni.paket.arbeitszeit.db.Day
import glavni.paket.arbeitszeit.model.Screen
import glavni.paket.arbeitszeit.ui.theme.*
import glavni.paket.arbeitszeit.ui.viewwmodels.MainViewModel
import glavni.paket.arbeitszeit.uitel.HomeScreen
import glavni.paket.arbeitszeit.uitel.HoursScreen
import glavni.paket.arbeitszeit.uitel.SettingScreen
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArbeitszeitTheme {
                val navController = rememberNavController()
                val titless = remember{ mutableStateOf("Home") }
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    /**set TopApp And Bottom Bar*/
                    Scaffold (
                        topBar = {
                            TopAppBar(
                                title = {
                                    Row (
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                            ){
                                        Text(
                                            text = titless.value,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                },
                                backgroundColor = RedInit
                            )
                        },
                        bottomBar = {
                            val items = listOf<Screen>(
                                Screen.Home,
                                Screen.Hours,
                                Screen.Setting
                            )
                            /**set design*/
                            BottomNavigation (
                                elevation = 15.dp,
                                backgroundColor = Color.Transparent,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        color = RedInit,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            ){
                                val navBackStackEntry by navController
                                    .currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry
                                    ?.arguments?.getString(KEY_ROUTE)
                                items.forEach {
                                    BottomNavigationItem(
                                        unselectedContentColor = Color.White,
                                        selectedContentColor = Color.Green,
                                        icon = { Icon(
                                            imageVector = it.icon,
                                            contentDescription = "Home"
                                        )},
                                        selected = currentRoute == it.route ,
                                        onClick = {
                                            navController.popBackStack(
                                                navController.graph.startDestination,
                                                false
                                            )
                                            if (currentRoute != it.route){
                                                navController.navigate(it.route)
                                            }
                                        },
                                        modifier = Modifier
                                            .background(
                                                color = RedInit,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                    )
                                }
                            }

                        }
                    ){
                        viewModel.getAllDays().observe(this, Observer {
                            for(dani: Day in it) {
                                Log.d("lista", "ID: ${dani.id}      LogIn: ${dani.timeLogIn}        LogOut: ${dani.timeLogOut}")
                            }
                        })
                        SreenController(
                            lifecycleOwner = this,
                            viewModel = viewModel,
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
    lifecycleOwner: LifecycleOwner,
    viewModel: MainViewModel,
    navController: NavHostController,
    topTitleBar: MutableState<String>
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    )
    {
        composable("home") {
            HomeScreen(lifecycleOwner, viewModel)
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


