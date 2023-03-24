package co.nimblehq.loyalty.sdk.poc.ui.screen.rewards

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.extension.toFormattedString
import co.nimblehq.loyalty.sdk.poc.ui.composable.RewardImage
import co.nimblehq.loyalty.sdk.poc.ui.composable.RewardInfoChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardListScreen(
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
        modifier = modifier.padding(vertical = 8.dp),
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
        RewardListContent(
            uiState = uiState,
            onRedeemReward = { reward ->
                viewModel.redeemReward(reward.id.orEmpty())
            },
            modifier = modifier.padding(paddingValues)
        )
    }

}

@Composable
internal fun RewardListContent(
    uiState: RewardListUiState,
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
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(uiState.data) { reward ->
                        RewardItem(reward = reward, onRedeemReward = onRedeemReward)
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
fun RewardItem(reward: Reward, onRedeemReward: (Reward) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column {
            Row(modifier = Modifier.padding(8.dp)) {
                RewardImage(
                    imageUrl = reward.imageUrls?.firstOrNull().orEmpty(),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = reward.name.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = reward.description.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(modifier = Modifier.padding(8.dp)) {
                RewardInfoChip(
                    isActive = "active" == reward.state,
                    text = reward.type.orEmpty(),
                    modifier = Modifier.width(96.dp)
                )

                RewardInfoChip(
                    isActive = true,
                    text = stringResource(
                        R.string.main_reward_expires_on,
                        reward.expiresOn?.toFormattedString().orEmpty()
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onRedeemReward.invoke(reward)
                }
            ) {
                Text(stringResource(R.string.main_redeem_reward, reward.pointCost ?: 0))
            }
        }
    }
}
