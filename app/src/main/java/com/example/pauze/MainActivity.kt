package com.example.pauze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pauze.ui.pauze.PauzeSoundScreen
import com.example.pauze.ui.theme.MainPaletteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                // PauzeSoundScreen 내부에서 모든 상태와 화면 전환이 처리되므로
                // 외부에서는 복잡한 인자 없이 단독으로 호출하면 됩니다.
                PauzeSoundScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}


//package com.example.pauze
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.pauze.ui.login.LoginLayout
//import com.example.pauze.ui.login.LoginScreen
//import com.example.pauze.ui.theme.AppTheme
//import com.example.pauze.ui.theme.MainPaletteTheme
//import com.example.pauze.ui.theme.PAUZEAndroidTheme
//import com.example.pauze.ui.theme.headingLgBold
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MainPaletteTheme {
//                LoginLayout()
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        color = AppTheme.palette.primary.getColor(5),
//        style = headingLgBold,
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PAUZEAndroidTheme {
//        Greeting("Android")
//    }
//}