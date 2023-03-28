package co.nimblehq.loyalty.sdk.poc.ui.screen.detail

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.extension.toFormattedString
import co.nimblehq.loyalty.sdk.poc.ui.composable.RewardDetailImage

@Composable
fun RewardDetailScreen(
    rewardId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: RewardDetailViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<RewardDetailUiState>(
        initialValue = RewardDetailUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.rewardDetailState.collect { value = it }
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

    LaunchedEffect(Unit) {
        viewModel.getRewardDetail(rewardId)
    }

    RewardDetailContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRedeemReward = { reward ->
            viewModel.redeemReward(reward.id.orEmpty())
        },
        modifier = modifier
    )
}

@Composable
internal fun RewardDetailContent(
    uiState: RewardDetailUiState,
    onNavigateBack: () -> Unit,
    onRedeemReward: (Reward) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is RewardDetailUiState.Success -> {
                RewardDetailInformation(
                    reward = uiState.data,
                    onNavigateBack = onNavigateBack,
                    onRedeemReward = onRedeemReward,
                    modifier = modifier
                )
            }
            is RewardDetailUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as RewardDetailUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
internal fun RewardDetailInformation(
    reward: Reward,
    onNavigateBack: () -> Unit,
    onRedeemReward: (Reward) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            RewardDetailImage(
                imageUrl = reward.imageUrls?.firstOrNull().orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(281.dp),
            )

            Image(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clickable {
                        onNavigateBack.invoke()
                    },
            )
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = reward.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = stringResource(
                    id = R.string.reward_details_point_cost,
                    reward.pointCost ?: 0
                ),
                style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF38A169)),
            )

            val expiresOn = reward.expiresOn?.toFormattedString()
            Text(
                text = if (expiresOn.isNullOrEmpty()) {
                    stringResource(R.string.reward_details_no_due_date)
                } else {
                    stringResource(R.string.reward_details_due_date, expiresOn)
                },
                style = MaterialTheme.typography.labelMedium,
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = reward.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(id = R.string.reward_details_terms),
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = if (reward.terms.isNullOrEmpty()) {
                    stringResource(id = R.string.reward_details_terms_not_available)
                } else {
                    reward.terms.orEmpty()
                },
                style = MaterialTheme.typography.bodyMedium,
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onRedeemReward.invoke(reward)
                }
            ) {
                Text(stringResource(R.string.reward_details_redeem, reward.pointCost ?: 0))
            }
        }
    }
}
