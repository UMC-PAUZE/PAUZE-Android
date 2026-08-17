package com.example.pauze

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pauze.data.repository.TokenRepository
import com.example.pauze.ui.curation.CurationBoardScreen
import com.example.pauze.ui.curation.toCurationPostIdOrNull
import com.example.pauze.ui.home.HomeScreen
import com.example.pauze.ui.login.LoginActivity
import com.example.pauze.ui.mypage.AccountInfoScreen
import com.example.pauze.ui.mypage.MyPageNavDestination
import com.example.pauze.ui.mypage.MyPageScreen
import com.example.pauze.ui.mypage.ProfileEditScreen
import com.example.pauze.ui.pauze.PauzeStartActivity
import com.example.pauze.ui.report.ReportScreen
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.captionTextMedium
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var pendingCurationPostId by mutableStateOf<Long?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateCurationDeepLink(intent)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                val navController = rememberNavController()
                // PauzeOverloadScreen에서 발견 탭으로 이동
                val destination = intent.getStringExtra("Bottom Navigation Destination")
                LaunchedEffect(destination) {
                    if(destination == "Find"){
                        navController.navigate(BottomNavDestination.Find){
                            popUpTo(BottomNavDestination.Find)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                MainScreen(
                    context = this,
                    navController = navController,
                    deepLinkPostId = pendingCurationPostId,
                    onDeepLinkConsumed = ::consumeCurationDeepLink,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateCurationDeepLink(intent)
    }

    private fun updateCurationDeepLink(intent: Intent) {
        pendingCurationPostId = intent.data?.toCurationPostIdOrNull()
    }

    private fun consumeCurationDeepLink() {
        pendingCurationPostId = null
        intent.data = null
    }
}
@Composable
fun MainScreen(
    context: Context,
    navController: NavHostController,
    deepLinkPostId: Long? = null,
    onDeepLinkConsumed: () -> Unit = {},
){
    LaunchedEffect(deepLinkPostId) {
        if (deepLinkPostId != null) {
            navController.navigate(BottomNavDestination.Find) {
                launchSingleTop = true
            }
        }
    }

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
                            navController.navigate(BottomNavDestination.Home){
                                popUpTo(BottomNavDestination.Home)
                                launchSingleTop = true
                                restoreState = true
                            }
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
                            navController.navigate(BottomNavDestination.Report){
                                popUpTo(BottomNavDestination.Report)
                                launchSingleTop = true
                                restoreState = true
                            }
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
                            context.startActivity(
                                Intent(context, PauzeStartActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }
                            )
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
                            navController.navigate(BottomNavDestination.Find){
                                popUpTo(BottomNavDestination.Find)
                                launchSingleTop = true
                                restoreState = true
                            }
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
                            navController.navigate(BottomNavDestination.MyPage){
                                popUpTo(BottomNavDestination.MyPage)
                                launchSingleTop = true
                                restoreState = true
                            }
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
        val goToLoginAndClearSession = {
            TokenRepository.accessToken = null
            context.startActivity(
                Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = BottomNavDestination.Home
        ){
            composable<BottomNavDestination.Home>{
                HomeScreen(context = context, navController = navController)
            }
            composable<BottomNavDestination.Report>{
                ReportScreen(context = context, isGuest = TokenRepository.accessToken == null)
            }
            composable<BottomNavDestination.Find>{
                CurationBoardScreen(
                    deepLinkPostId = deepLinkPostId,
                    onDeepLinkConsumed = onDeepLinkConsumed,
                )
            }
            composable<BottomNavDestination.MyPage> {
                MyPageScreen(navController = navController, isGuest = TokenRepository.accessToken == null)
            }
            composable<MyPageNavDestination.ProfileEdit> {
                ProfileEditScreen(navController = navController)
            }
            composable<MyPageNavDestination.AccountInfo> {
                AccountInfoScreen(
                    onBackClick = { navController.popBackStack() },
                    onLogoutClick = goToLoginAndClearSession,
                    onWithdrawClick = goToLoginAndClearSession
                )
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
