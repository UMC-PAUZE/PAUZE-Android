package com.example.pauze

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pauze.ui.home.HomeScreen
import com.example.pauze.ui.pauze.PauzeStartScreen
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.captionTextMedium

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                MainScreen(context = this)
            }
        }
    }
}
@Composable
fun MainScreen(
    context: Context,
){
    val navController = rememberNavController()
    Scaffold(
        containerColor = AppTheme.palette.gray.getColor(9),
        bottomBar = {
            NavigationBar(
                containerColor = AppTheme.palette.gray.getColor(9),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    NavigationBarItem(
                        selected = isSelected(navController, BottomNavDestination.Home),
                        onClick = {
                            navController.navigate(BottomNavDestination.Home)
                        },
                        icon = {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(
                                    if(isSelected(navController, BottomNavDestination.Home)) R.drawable.ic_home_selected
                                    else R.drawable.ic_home_unselected
                                ),
                                contentDescription = "홈",
                            )
                        },
                        label = { NavBarText("홈") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                        )
                    )
                    NavigationBarItem(
                        selected = isSelected(navController, BottomNavDestination.Report),
                        onClick = {
                            navController.navigate(BottomNavDestination.Report)
                        },
                        icon = { Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                if(isSelected(navController, BottomNavDestination.Report)) R.drawable.ic_report_selected
                                else R.drawable.ic_report_unselected
                            ),
                            contentDescription = "리포트"
                        ) },
                        label = { NavBarText("리포트") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                        )
                    )
                    IconButton(
                        onClick = {
//                            val intent = Intent(
//                                context,
//                                Class.forName("com.example.pauze.ui.PauzeStartScreen")
//                            )
//                            startActivity(context, intent, null)
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(56.dp),
                            painter = painterResource(R.drawable.ic_pauze_btn),
                            contentDescription = "안정하기(pauze)"
                        )
                    }
                    NavigationBarItem(
                        selected = isSelected(navController, BottomNavDestination.Find),
                        onClick = {
                            navController.navigate(BottomNavDestination.Find)
                        },
                        icon = { Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                if(isSelected(navController, BottomNavDestination.Find)) R.drawable.ic_find_selected
                                else R.drawable.ic_find_unselected
                            ),
                            contentDescription = "발견"
                        ) },
                        label = { NavBarText("발견") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                        )
                    )
                    NavigationBarItem(
                        selected = isSelected(navController, BottomNavDestination.MyPage),
                        onClick = {
                            navController.navigate(BottomNavDestination.MyPage)
                        },
                        icon = { Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(
                                if(isSelected(navController, BottomNavDestination.MyPage)) R.drawable.ic_mypage_selected
                                else R.drawable.ic_mypage_unselected
                            ),
                            contentDescription = "마이 페이지"
                        ) },
                        label = { NavBarText("마이") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                        )
                    )
                }
            }
        },
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
        NavHost(navController = navController, startDestination = BottomNavDestination.Home){
            composable<BottomNavDestination.Home>{
                HomeScreen(context = context)
            }
            composable<BottomNavDestination.Report>{

            }
            composable<BottomNavDestination.Find>{

            }
            composable<BottomNavDestination.MyPage> {

            }
        }
    }
}

@Composable
fun NavBarText(text: String){
    Text(text, style = captionTextMedium, color = AppTheme.palette.gray.getColor(2))
}

@Composable
fun isSelected(navController: NavController, destination: BottomNavDestination): Boolean{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.hasRoute(destination::class) == true
}