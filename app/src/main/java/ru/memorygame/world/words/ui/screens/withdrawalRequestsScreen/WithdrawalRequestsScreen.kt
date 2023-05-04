package ru.memorygame.world.words.ui.screens.withdrawalRequestsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.memorygame.world.words.R
import ru.memorygame.world.words.common.setClipboard
import ru.memorygame.world.words.data.user.model.userSumMoneyVersion2
import ru.memorygame.world.words.data.utils.UtilsDataStore
import ru.memorygame.world.words.data.utils.model.Utils
import ru.memorygame.world.words.data.withdrawalRequest.WithdrawalRequestDataStore
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequest
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequestStatus
import ru.memorygame.world.words.ui.theme.tintColor
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WithdrawalRequestsScreen(
    navController: NavController,
    withdrawalRequestStatus: WithdrawalRequestStatus
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var deleteWithdrawalRequestId by remember { mutableStateOf("") }
    var utils by remember { mutableStateOf<Utils?>(null) }
    var withdrawalRequests by remember { mutableStateOf(listOf<WithdrawalRequest>()) }
    val withdrawalRequestDataStore = remember(::WithdrawalRequestDataStore)
    val utilsDataStore = remember(::UtilsDataStore)

    LaunchedEffect(key1 = Unit, block = {
        withdrawalRequestDataStore.getAll({
            withdrawalRequests = it.filter {
                withdrawalRequestStatus == it.status
            }
        }, {})
        utilsDataStore.get({utils = it})
    })

    if (deleteWithdrawalRequestId.isNotEmpty()) {
        DeleteWithdrawalRequestAlertDialog(
            onDismissRequest = {
                deleteWithdrawalRequestId = ""
            },
            confirm = {
                withdrawalRequestDataStore.updateStatus(
                    id = deleteWithdrawalRequestId,
                    status = when(withdrawalRequestStatus){
                        WithdrawalRequestStatus.WAITING -> WithdrawalRequestStatus.PAID
                        WithdrawalRequestStatus.PAID -> WithdrawalRequestStatus.WAITING
                    },
                    onSuccess = {
                        deleteWithdrawalRequestId = ""

                        withdrawalRequestDataStore.getAll({
                            withdrawalRequests = it.filter {
                                withdrawalRequestStatus == it.status
                            }
                        }, {})
                    },
                    onError = {
                        Toast.makeText(context, "Ошибка: $it", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }


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

        LazyColumn(
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            )
        ) {
            items(withdrawalRequests) { item ->

                Card(
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Индификатор пользователя : ${item.userId}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.userId)
                                    })
                                }
                        )

                        Text(
                            text = "Электронная почта : ${item.userEmail}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.userEmail)
                                    })
                                }
                        )

                        Text(
                            text = "Номер телефона : ${item.phoneNumber}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.phoneNumber)
                                    })
                                }
                        )

                        Text(
                            text = "Полноэкраная : ${item.countInterstitialAds}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, (item.countInterstitialAds.toString()))
                                    })
                                }
                        )

                        Text(
                            text = "Полноэкраная переход 10 сек : ${item.countInterstitialAdsClick}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countInterstitialAdsClick.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Вознаграждением : ${item.countRewardedAds}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countRewardedAds.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Вознаграждением переход на 10 сек: ${item.countRewardedAdsClick}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countRewardedAdsClick.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Vpn: ${if(item.vpn) "Да" else "Нет"}",
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.achievementPrice.toString())
                                    })
                                }
                        )

                        utils?.let {

                            val sum = userSumMoneyVersion2(
                                utils = it,
                                countInterstitialAds = item.countInterstitialAds,
                                countInterstitialAdsClick = item.countInterstitialAdsClick,
                                countRewardedAds = item.countRewardedAds,
                                countRewardedAdsClick = item.countRewardedAdsClick,
                                countBannerAds = 0,
                                countBannerAdsClick = 0
                            )

                            Text(
                                text = "Сумма для ввывода : $sum",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(onLongPress = {
                                            setClipboard(context, sum.toString())
                                        })
                                    }
                            )
                        }

                        item.date?.let { date ->
                            Text(
                                text = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(date)),
                                color = tintColor,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .pointerInput(Unit) {
                                        detectTapGestures(onLongPress = {
                                            setClipboard(context, item.achievementPrice.toString())
                                        })
                                    }
                            )
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            onClick = { deleteWithdrawalRequestId = item.id },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = tintColor
                            )
                        ) {
                            Text(text = "Сменить статус")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun DeleteWithdrawalRequestAlertDialog(
    onDismissRequest: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog(
        shape = AbsoluteRoundedCornerShape(15.dp),
        onDismissRequest = onDismissRequest,
        buttons = {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = confirm
            ) {
                Text(text = "Подтвердить", color = Color.Red)
            }
        }
    )
}