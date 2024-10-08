package com.mevi.ui.screens.loading

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.ui.R
import com.mevi.ui.screens.state.UIScreenState
import com.mevi.ui.theme.MeviTheme

@Composable
fun StartScreen(
    state: UIScreenState<Boolean>,
    checkInternet: () -> Boolean,
    onInternetUnavailable: (callback: () -> Unit) -> Unit,
    checkAuthorization: () -> Unit,
    onUserAuthorized: () -> Unit,
    onUserUnauthorized: () -> Unit,
    initializeGlobalNetworkCallback: () -> Unit,
) {
    val internetState = rememberSaveable {
        mutableStateOf(checkInternet())
    }
    val onInternetRestored = {
        internetState.value = true
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = stringResource(R.string.LOGO_CONTENT_DESCRIPTION)
        )
        Text(text = stringResource(R.string.LOADING_TEXT))
        Spacer(modifier = Modifier.height(10.dp))
        CircularProgressIndicator()
        if (internetState.value) {
            initializeGlobalNetworkCallback()
            if (state.data != null && !state.isLoading) {
                if (state.data) {
                    onUserAuthorized()
                } else {
                    onUserUnauthorized()
                }
            } else {
                LaunchedEffect(Unit) {
                    checkAuthorization()
                }
            }
        } else {
            onInternetUnavailable {
                onInternetRestored()
            }
        }
    }
}

@Preview(
    showBackground = true, device = Devices.PIXEL_3, showSystemUi = true
)
@Preview(
    showBackground = true,
    device = Devices.PIXEL_3,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun LoadingScreenPreview() {
    MeviTheme {
        StartScreen(
            UIScreenState(false, null, null),
            { false },
            {}, {}, {}, {}, {}
        )
    }
}