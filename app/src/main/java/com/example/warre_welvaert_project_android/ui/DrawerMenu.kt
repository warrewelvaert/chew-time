package com.example.warre_welvaert_project_android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warre_welvaert_project_android.R
import kotlinx.coroutines.launch

data class DrawerMenuItem(val route: String, val icon: Int, @StringRes val labelResId: Int)

val drawerMenuItems = listOf(
    DrawerMenuItem("food_overview", R.drawable.nav_food, R.string.nav_food),
    DrawerMenuItem("pet_overview", R.drawable.nav_pet, R.string.nav_pet),
    DrawerMenuItem("timer_overview", R.drawable.nav_clock, R.string.nav_timer)
)

@Composable
fun DrawerMenu(navController: NavController, scaffoldState: ScaffoldState) {
    Column(modifier = Modifier
        .fillMaxHeight(),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = null, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                color = Color.White
            )
        }

        Divider()
        drawerMenuItems.forEach { menuItem ->
            Divider()
            DrawerMenuItem(menuItem, navController, scaffoldState)
        }
        Divider()
    }
}

@Composable
private fun DrawerMenuItem(
    menuItem: DrawerMenuItem,
    navController: NavController,
    scaffoldState: ScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(35.dp)
            .clickable {
                navController.navigate(menuItem.route)
                coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = menuItem.icon),
            contentDescription = null,
            Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(stringResource(menuItem.labelResId))
    }
}