package co.nimblehq.loyalty.sdk.poc.ui.screen.home

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.loyalty.sdk.LoyaltySdk
import co.nimblehq.loyalty.sdk.poc.R

@Composable
fun HomeScreen(
    modifier: Modifier,
    onNavigateToRewardList: () -> Unit,
    onNavigateToRewardHistory: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<HomeUiState>(
        initialValue = HomeUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            // Refresh UI after clear session
            viewModel.invalidateUiState()
            viewModel.uiState.collect { value = it }
        }
    }

    HomeScreenContent(
        uiState = state,
        onNavigateToAuthenticate = {
            LoyaltySdk.getInstance().authenticate(activity)
        },
        onNavigateToRewardList = onNavigateToRewardList,
        onNavigateToRewardHistory = onNavigateToRewardHistory,
        onClearSession = {
            viewModel.clearSession()
        },
        modifier = modifier
    )
}

@Composable
internal fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateToAuthenticate: () -> Unit,
    onNavigateToRewardList: () -> Unit,
    onNavigateToRewardHistory: () -> Unit,
    onClearSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val activity = LocalContext.current as Activity
        when (uiState) {
            is HomeUiState.Success -> {
                val isAuthenticated = uiState.isAuthenticated
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sign In / Sign Out
                    Button(
                        modifier = Modifier
                            .width(256.dp)
                            .padding(16.dp),
                        onClick = {
                            if (isAuthenticated) {
                                onClearSession.invoke()
                            } else {
                                onNavigateToAuthenticate.invoke()
                            }
                        }
                    ) {
                        val text = if (isAuthenticated) {
                            stringResource(id = R.string.main_sign_out)
                        } else {
                            stringResource(id = R.string.main_sign_in)
                        }
                        Text(text = text)
                    }

                    // Reward List
                    Button(
                        enabled = isAuthenticated,
                        modifier = Modifier
                            .width(256.dp)
                            .padding(16.dp),
                        onClick = {
                            onNavigateToRewardList.invoke()
                        }
                    ) {
                        Text(stringResource(id = R.string.main_reward_list))
                    }

                    // Redeemed Reward History
                    Button(
                        enabled = isAuthenticated,
                        modifier = Modifier
                            .width(256.dp)
                            .padding(16.dp),
                        onClick = {
                            onNavigateToRewardHistory.invoke()
                        }
                    ) {
                        Text(stringResource(id = R.string.main_reward_history))
                    }
                }
            }
            is HomeUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as HomeUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
