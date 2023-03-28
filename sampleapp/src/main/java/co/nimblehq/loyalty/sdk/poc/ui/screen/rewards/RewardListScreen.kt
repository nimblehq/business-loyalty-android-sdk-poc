package co.nimblehq.loyalty.sdk.poc.ui.screen.rewards

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
import androidx.compose.runtime.*
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
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.extension.toFormattedString
import co.nimblehq.loyalty.sdk.poc.ui.composable.RewardImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardListScreen(
    onNavigateRewardDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: RewardListViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<RewardListUiState>(
        initialValue = RewardListUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val redeemRewardUiState by produceState<RedeemRewardUiState>(
        initialValue = RedeemRewardUiState.Init,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.redeemedRewardState.collect { value = it }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(redeemRewardUiState) {
        when (redeemRewardUiState) {
            is RedeemRewardUiState.Success -> {
                context.getString(R.string.redeem_reward_success)
            }
            is RedeemRewardUiState.Error -> {
                (redeemRewardUiState as RedeemRewardUiState.Error).throwable.message
            }
            is RedeemRewardUiState.Processing -> {
                context.getString(R.string.redeem_reward_processing)
            }
            else -> null
        }?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_reward_list))
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
            RewardListContent(
                uiState = uiState,
                onRewardItemClick = { reward ->
                    onNavigateRewardDetail.invoke(reward.id.orEmpty())
                },
                onRedeemReward = { reward ->
                    viewModel.redeemReward(reward.id.orEmpty())
                },
            )
        }
    }
}

@Composable
internal fun RewardListContent(
    uiState: RewardListUiState,
    onRewardItemClick: (Reward) -> Unit,
    onRedeemReward: (Reward) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is RewardListUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(items = uiState.data) { reward ->
                        RewardItem(
                            reward = reward,
                            onRewardItemClick = onRewardItemClick,
                            onRedeemReward = onRedeemReward
                        )
                    }
                }
            }
            is RewardListUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as RewardListUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun RewardItem(
    reward: Reward,
    onRewardItemClick: (Reward) -> Unit,
    onRedeemReward: (Reward) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(
                border = BorderStroke(width = 1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onRewardItemClick.invoke(reward) },
    ) {
        Column {
            RewardImage(
                imageUrl = reward.imageUrls?.firstOrNull().orEmpty(),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = reward.name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.aspectRatio(3.25f) // FIXME Stretch items to same height
                )
                Text(
                    text = reward.description.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                val expiresOn = reward.expiresOn?.toFormattedString()
                Text(
                    text = if (expiresOn.isNullOrEmpty()) {
                        stringResource(R.string.reward_details_no_due_date)
                    } else {
                        stringResource(R.string.reward_details_due_date, expiresOn)
                    },
                    style = MaterialTheme.typography.titleSmall,
                )

                Spacer(modifier = Modifier.padding(top = 8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onRedeemReward.invoke(reward)
                    }
                ) {
                    Text(stringResource(R.string.main_redeem_reward, reward.pointCost ?: 0))
                }
            }
        }
    }
}
