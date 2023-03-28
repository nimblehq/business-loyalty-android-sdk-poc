package co.nimblehq.loyalty.sdk.poc.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RewardHistoryViewModel @Inject constructor(
    repository: SampleAppRepository
) : ViewModel() {

    val uiState: StateFlow<RewardHistoryUiState> = repository
        .redeemedRewards.map { data -> RewardHistoryUiState.Success(data) }
        .catch { RewardHistoryUiState.Error(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RewardHistoryUiState.Loading
        )
}

sealed interface RewardHistoryUiState {
    object Loading : RewardHistoryUiState
    data class Error(val throwable: Throwable) : RewardHistoryUiState
    data class Success(val data: List<RedeemedReward>) : RewardHistoryUiState
}