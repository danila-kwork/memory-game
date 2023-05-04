package ru.memorygame.world.words.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.memorygame.world.words.R
import ru.memorygame.world.words.common.openBrowser
import ru.memorygame.world.words.data.user.UserDataStore
import ru.memorygame.world.words.data.user.model.User
import ru.memorygame.world.words.data.user.model.UserRole
import ru.memorygame.world.words.data.utils.UtilsDataStore
import ru.memorygame.world.words.data.utils.model.Utils
import ru.memorygame.world.words.data.withdrawalRequest.WithdrawalRequestDataStore
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequest
import ru.memorygame.world.words.ui.navigation.Screen
import ru.memorygame.world.words.ui.screens.mainScreen.view.InfoAlertDialog
import ru.memorygame.world.words.ui.screens.mainScreen.view.RewardAlertDialog
import ru.memorygame.world.words.ui.theme.primaryText
import ru.memorygame.world.words.ui.theme.tintColor
import ru.memorygame.world.words.ui.view.BaseLottieAnimation
import ru.memorygame.world.words.ui.view.LottieAnimationType

@Composable
fun MainScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    val withdrawalRequestDataStore = remember(::WithdrawalRequestDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }
    var rewardAlertDialog by remember { mutableStateOf(false) }
    var infoAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

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

    if(rewardAlertDialog){
        RewardAlertDialog(
            utils = utils,
            countInterstitialAds = user?.countInterstitialAds ?: 0,
            countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
            countRewardedAds = user?.countRewardedAds ?: 0,
            countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
            onDismissRequest = { rewardAlertDialog = false },
            onSendWithdrawalRequest = { phoneNumber ->
                user ?: return@RewardAlertDialog

                val withdrawalRequest = WithdrawalRequest(
                    countInterstitialAds = user!!.countInterstitialAds,
                    countInterstitialAdsClick = user!!.countInterstitialAdsClick,
                    countRewardedAds = user!!.countRewardedAds,
                    countRewardedAdsClick = user!!.countRewardedAdsClick,
                    phoneNumber = phoneNumber,
                    userEmail = user!!.email,
                    version = 1
                )

                withdrawalRequestDataStore.create(withdrawalRequest) {
                    if (it.isSuccessful) {
                        rewardAlertDialog = false
                        Toast.makeText(context, "Заявка отправлена", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ошибка: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    if(infoAlertDialog){
        InfoAlertDialog(info = utils?.info ?: "") {
            infoAlertDialog = false
        }
    }

    Column {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            )
        ) {
            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.LANGUAGE,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(5.dp)
                )

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        navController.navigate("CountyListScreen")
                    }
                ) {
                    Text(text = "Список стран", color = primaryText)
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        navController.navigate("FlagsScreen")
                    }
                ) {
                    Text(text = "Флаги", color = primaryText)
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        navController.navigate("CapitalsScreen")
                    }
                ) {
                    Text(text = "Столиции", color = primaryText)
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        navController.navigate(Screen.Ads.route)
                    }
                ) {
                    Text(text = "Реклама", color = primaryText)
                }

                Button(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        rewardAlertDialog = true
                    }
                ) {
                    Text(text = "Награда", color = primaryText)
                }

                if(user?.userRole == UserRole.ADMIN){
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 5.dp)
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tintColor
                        ),
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        onClick = {
                            navController.navigate(Screen.Admin.route)
                        }
                    ) {
                        Text(text = "Админ", color = primaryText)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        context.openBrowser("https://t.me/stranymirra")
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.telegram),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(80.dp)
                        )
                    }

                    AnimatedVisibility(visible = utils != null && utils!!.info.isNotEmpty()) {
                        IconButton(onClick = {
                            infoAlertDialog = true
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.info),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}