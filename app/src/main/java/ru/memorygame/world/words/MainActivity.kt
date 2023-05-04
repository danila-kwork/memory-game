package ru.memorygame.world.words

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import ru.memorygame.world.words.ui.navigation.Screen
import ru.memorygame.world.words.ui.screens.adminScreen.AdminScreen
import ru.memorygame.world.words.ui.screens.adsScreen.AdsScreen
import ru.memorygame.world.words.ui.screens.authScreen.AuthScreen
import ru.memorygame.world.words.ui.screens.mainScreen.MainScreen
import ru.memorygame.world.words.ui.screens.settingsScreen.SettingsScreen
import ru.memorygame.world.words.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.memorygame.world.words.ui.theme.tintColor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val auth = remember { FirebaseAuth.getInstance() }

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "splash",
                    builder = {
                        composable("splash"){
                            val context = LocalContext.current

                            val screenWidthDp = LocalConfiguration.current.screenWidthDp
                            val screenHeightDp = LocalConfiguration.current.screenHeightDp

                            LaunchedEffect(key1 = Unit, block = {
                                delay(1000L)
                                navController.navigate(if(auth.currentUser == null)
                                    Screen.Auth.route
                                else
                                    Screen.Main.route
                                )
                            })

                            Surface {
                                Image(
                                    bitmap = Bitmap.createScaledBitmap(
                                        BitmapFactory.decodeResource(context.resources, R.drawable.main_background),
                                        screenWidthDp,
                                        screenHeightDp,
                                        false
                                    ).asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.size(
                                        width = screenWidthDp.dp,
                                        height = screenHeightDp.dp
                                    )
                                )

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = tintColor)
                                }
                            }
                        }
                        composable(Screen.Auth.route){
                            AuthScreen(navController = navController)
                        }
                        composable(Screen.Main.route){
                            MainScreen(navController = navController)
                        }
                        composable(Screen.Admin.route){
                            AdminScreen(navController = navController)
                        }
                        composable(Screen.Settings.route){
                            SettingsScreen(navController = navController)
                        }
                        composable(Screen.Ads.route){
                            AdsScreen()
                        }
                        composable(
                            "WithdrawalRequestsScreen/{status}",
                            arguments = listOf(
                                navArgument("status"){
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            WithdrawalRequestsScreen(
                                navController = navController,
                                withdrawalRequestStatus = enumValueOf(
                                    it.arguments!!.getString("status").toString()
                                )
                            )
                        }
                    }
                )
            }
        }
    }

    fun fullScreencall() {
        //for new api versions.
        val decorView = window.decorView
        val uiOptions: Int =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}