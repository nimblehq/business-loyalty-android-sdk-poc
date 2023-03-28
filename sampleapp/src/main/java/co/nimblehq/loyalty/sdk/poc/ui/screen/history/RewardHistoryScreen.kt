package co.nimblehq.loyalty.sdk.poc.ui.screen.history

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.extension.toFormattedString
import co.nimblehq.loyalty.sdk.poc.ui.composable.RedeemedRewardImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardHistoryScreen(
    modifier: Modifier,
    viewModel: RewardHistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<RewardHistoryUiState>(
        initialValue = RewardHistoryUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    Scaffold(modifier = modifier.padding(vertical = 8.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_reward_history))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack.invoke()
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        }) { paddingValues ->
        RewardHistoryContent(
            uiState = uiState,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
internal fun RewardHistoryContent(
    uiState: RewardHistoryUiState,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is RewardHistoryUiState.Success -> {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    itemsIndexed(uiState.data) { index, reward ->
                        RedeemedRewardItem(redeemedReward = reward)

                        if (index < uiState.data.lastIndex) {
                            Divider(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
            is RewardHistoryUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as RewardHistoryUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun RedeemedRewardItem(redeemedReward: RedeemedReward) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RedeemedRewardImage(
                imageUrl = redeemedReward.reward?.imageUrls?.firstOrNull().orEmpty(),
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = redeemedReward.reward?.name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                val expiresOn = redeemedReward.reward?.expiresOn?.toFormattedString()
                Text(
                    text = if (expiresOn.isNullOrEmpty()) {
                        stringResource(R.string.reward_details_no_due_date)
                    } else {
                        stringResource(R.string.reward_details_due_date, expiresOn)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }
    }
}
