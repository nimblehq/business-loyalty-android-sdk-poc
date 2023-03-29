package co.nimblehq.loyalty.sdk.poc.ui.screen.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel @Inject constructor(
    private val repository: SampleAppRepository
) : ViewModel() {

    val uiState: SharedFlow<RewardListUiState> = repository
        .rewards
        .map { data -> RewardListUiState.Success(data) as RewardListUiState }
        .catch { cause: Throwable ->
            emit(RewardListUiState.Error(cause))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RewardListUiState.Loading
        )

    private val _redeemedRewardState =
        MutableStateFlow<RedeemRewardUiState>(RedeemRewardUiState.Init)
    val redeemedRewardState: StateFlow<RedeemRewardUiState> = _redeemedRewardState

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

sealed interface RewardListUiState {
    object Loading : RewardListUiState
    data class Error(val throwable: Throwable) : RewardListUiState
    data class Success(val data: List<Reward>) : RewardListUiState
}

sealed interface RedeemRewardUiState {
    object Init : RedeemRewardUiState
    object Processing : RedeemRewardUiState
    data class Error(val throwable: Throwable) : RedeemRewardUiState
    object Success : RedeemRewardUiState
}
