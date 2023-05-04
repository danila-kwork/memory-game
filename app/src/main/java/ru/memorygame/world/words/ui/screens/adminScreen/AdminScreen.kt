package ru.memorygame.world.words.ui.screens.adminScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.memorygame.world.words.ui.navigation.Screen
import ru.memorygame.world.words.ui.theme.primaryText
import ru.memorygame.world.words.ui.theme.tintColor
import ru.memorygame.world.words.R
import ru.memorygame.world.words.data.utils.UtilsDataStore
import ru.memorygame.world.words.data.utils.model.Utils
import ru.memorygame.world.words.data.withdrawalRequest.model.WithdrawalRequestStatus
import ru.memorygame.world.words.ui.screens.adminScreen.view.AddInfoAlertDialog

@Composable
fun AdminScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var addInfoAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get({utils = it})
    })

    Surface {

        if(addInfoAlertDialog){
            AddInfoAlertDialog(
                info = utils?.info ?: "",
                onDismissRequest = { addInfoAlertDialog = false },
                onUpdateInfo = {
                    utilsDataStore.updateInfo(it){
                        addInfoAlertDialog = false
                        utilsDataStore.get({utils = it})
                    }
                }
            )
        }

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
            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(
                        "WithdrawalRequestsScreen/${WithdrawalRequestStatus.PAID}"
                    )
                }
            ) {
                Text(
                    text = "Заявки статус '${WithdrawalRequestStatus.PAID.text}'",
                )
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(
                        "WithdrawalRequestsScreen/${WithdrawalRequestStatus.WAITING}"
                    )
                }
            ) {
                Text(
                    text = "Заявки статус '${WithdrawalRequestStatus.WAITING.text}'",
                )
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    addInfoAlertDialog = true
                }
            ) {
                Text(text = "Изменить информацию", color = primaryText)
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(Screen.Settings.route)
                }
            ) {
                Text(text = "Настройки", color = primaryText)
            }
        }
    }
}