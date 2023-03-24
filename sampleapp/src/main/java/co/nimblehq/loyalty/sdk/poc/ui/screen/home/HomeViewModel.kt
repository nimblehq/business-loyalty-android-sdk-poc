package co.nimblehq.loyalty.sdk.poc.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SampleAppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    init {
        invalidateUiState()
    }

    fun invalidateUiState() {
        viewModelScope.launch {
            repository
                .isAuthenticated
                .map { isAuthenticated -> HomeUiState.Success(isAuthenticated) }
                .catch {
                    _uiState.emit(HomeUiState.Error(it))
                }
                .collectLatest { state ->
                    _uiState.emit(state)
                }
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            repository
                .clearSession()
                .catch {
                    _uiState.emit(HomeUiState.Error(it))
                }
                .collectLatest {
                    invalidateUiState()
                }
        }
    }
}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Error(val throwable: Throwable) : HomeUiState
    data class Success(val isAuthenticated: Boolean) : HomeUiState
}