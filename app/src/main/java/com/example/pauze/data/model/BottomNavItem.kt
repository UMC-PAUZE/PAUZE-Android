package com.example.pauze.data.model

import androidx.annotation.DrawableRes
import com.example.pauze.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val title: String
) {
    object Home: BottomNavItem(
        "home",
        R.drawable.ic_home_selected,
        R.drawable.ic_home_unselected,
        "홈"
    )
    object Report: BottomNavItem(
        "report",
        R.drawable.ic_report_selected,
        R.drawable.ic_report_unselected,
        "리포트"
    )
    object Find: BottomNavItem(
        "find",
        R.drawable.ic_find_selected,
        R.drawable.ic_find_unselected,
        "발견"
    )
    object MyPage: BottomNavItem(
        "mypage",
        R.drawable.ic_mypage_selected,
        R.drawable.ic_mypage_unselected,
        "마이"
    )
}