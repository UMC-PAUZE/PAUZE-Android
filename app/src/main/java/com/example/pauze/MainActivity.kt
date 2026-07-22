package com.example.pauze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.objectListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.PAUZEAndroidTheme
import com.example.pauze.ui.theme.captionTextMedium

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen(
    //context: Context,
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
                        selected = true,
                        onClick = {},
                        icon = { Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_home_unselected),
                            contentDescription = "홈",
                        ) },
                        label = { NavBarText("홈") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor =  AppTheme.palette.gray.getColor(2),
                            unselectedIconColor = AppTheme.palette.gray.getColor(2),
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_report_unselected),
                            contentDescription = "리포트"
                        ) },
                        label = { NavBarText("리포트") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor =  AppTheme.palette.gray.getColor(2),
                            unselectedIconColor = AppTheme.palette.gray.getColor(2)
                        )
                    )
                    IconButton(
                        onClick = {
                            //                    val intent = Intent(
//                        context,
//                        Class.forName("com.example.pauze.ui.PauzeStartScreen")
//                    )
//                    startActivity(context, intent, null)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(56.dp),
                            painter = painterResource(R.drawable.ic_pauze_btn),
                            contentDescription = "안정하기(pauze)"
                        )
                    }
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_find_unselected),
                            contentDescription = "발견"
                        ) },
                        label = { NavBarText("발견") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor =  AppTheme.palette.gray.getColor(2),
                            unselectedIconColor = AppTheme.palette.gray.getColor(2)
                        )
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = { Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_mypage_unselected),
                            contentDescription = "마이 페이지"
                        ) },
                        label = { NavBarText("마이") },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor =  AppTheme.palette.gray.getColor(2),
                            unselectedIconColor = AppTheme.palette.gray.getColor(2)
                        )
                    )
                }
            }
        },
    ) {
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavBarText(text: String){
    Text(text, style = captionTextMedium, color = AppTheme.palette.gray.getColor(2))
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PAUZEAndroidTheme {
        MainScreen()
    }
}