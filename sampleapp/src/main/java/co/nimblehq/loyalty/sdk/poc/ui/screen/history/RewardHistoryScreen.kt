package co.nimblehq.loyalty.sdk.poc.ui.screen.history

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import co.nimblehq.loyalty.sdk.poc.ui.composable.RewardImage

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
        Column(modifier = modifier.padding(paddingValues)) {
            RewardHistoryContent(
                uiState = uiState,
//                onRewardItemClick = { reward ->
//                    onNavigateRewardDetail.invoke(reward.id.orEmpty())
//                },
//                onRedeemReward = { reward ->
//                    viewModel.redeemReward(reward.id.orEmpty())
//                },
            )
        }
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(uiState.data) { reward ->
                        RedeemedRewardItem(redeemedReward = reward, onRewardItemClick = {})
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
fun RedeemedRewardItem(
    redeemedReward: RedeemedReward,
    onRewardItemClick: (RedeemedReward) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(
                border = BorderStroke(width = 1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onRewardItemClick.invoke(redeemedReward) },
    ) {
        Column {
            RewardImage(
                imageUrl = redeemedReward.images.orEmpty(),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = redeemedReward.reward?.name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = redeemedReward.reward?.description.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                val expiresOn = redeemedReward.reward?.expiresOn?.toFormattedString()
                Text(
                    text = if (expiresOn.isNullOrEmpty()) {
                        stringResource(R.string.reward_details_no_due_date)
                    } else {
                        stringResource(R.string.reward_details_due_date, expiresOn)
                    },
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}