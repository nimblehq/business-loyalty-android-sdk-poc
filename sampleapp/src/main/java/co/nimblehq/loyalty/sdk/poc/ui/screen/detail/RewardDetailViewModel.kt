package co.nimblehq.loyalty.sdk.poc.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardDetailViewModel @Inject constructor(
    private val repository: SampleAppRepository
) : ViewModel() {

    private val _rewardDetailState =
        MutableStateFlow<RewardDetailUiState>(RewardDetailUiState.Loading)
    val rewardDetailState: StateFlow<RewardDetailUiState> = _rewardDetailState

    private val _redeemedRewardState =
        MutableStateFlow<RedeemRewardUiState>(RedeemRewardUiState.Init)
    val redeemedRewardState: StateFlow<RedeemRewardUiState> = _redeemedRewardState

    fun getRewardDetail(rewardId: String) {
        viewModelScope.launch {
            repository
                .getRewardDetail(rewardId = rewardId)
                .onStart { _rewardDetailState.emit(RewardDetailUiState.Loading) }
                .map { result -> _rewardDetailState.emit(RewardDetailUiState.Success(result)) }
                .catch { _rewardDetailState.emit(RewardDetailUiState.Error(it)) }
                .collect()
        }
    }

    fun redeemReward(rewardId: String) {
        viewModelScope.launch {
            repository
                .redeemReward(rewardId = rewardId)
                .onStart { _redeemedRewardState.emit(RedeemRewardUiState.Processing) }
                .map { _redeemedRewardState.emit(RedeemRewardUiState.Success) }
                .catch { _redeemedRewardState.emit(RedeemRewardUiState.Error(it)) }
                .collect()
        }
    }
}

sealed interface RewardDetailUiState {
    object Loading : RewardDetailUiState
    data class Error(val throwable: Throwable) : RewardDetailUiState
    data class Success(val data: Reward) : RewardDetailUiState
}

sealed interface RedeemRewardUiState {
    object Init : RedeemRewardUiState
    object Processing : RedeemRewardUiState
    data class Error(val throwable: Throwable) : RedeemRewardUiState
    object Success : RedeemRewardUiState
}