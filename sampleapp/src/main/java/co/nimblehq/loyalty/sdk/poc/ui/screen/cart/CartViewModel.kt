package co.nimblehq.loyalty.sdk.poc.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.Cart
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: SampleAppRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = repository
        .cart
        .map { data -> CartUiState.Success(data) as CartUiState }
        .catch { cause: Throwable ->
            emit(CartUiState.Error(cause))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CartUiState.Loading
        )

    private val _removeCartItemState =
        MutableStateFlow<RemoveCartItemUiState>(RemoveCartItemUiState.Init)
    val removeCartItemState: StateFlow<RemoveCartItemUiState> = _removeCartItemState

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            repository
                .removeCartItem(itemId = itemId)
                .onStart { _removeCartItemState.emit(RemoveCartItemUiState.Processing) }
                .map { _removeCartItemState.emit(RemoveCartItemUiState.Success(it)) }
                .catch { _removeCartItemState.emit(RemoveCartItemUiState.Error(it)) }
                .collect()
        }
    }
}

sealed interface CartUiState {
    object Loading : CartUiState
    data class Error(val throwable: Throwable) : CartUiState
    data class Success(val data: Cart) : CartUiState
}

sealed interface RemoveCartItemUiState {
    object Init : RemoveCartItemUiState
    object Processing : RemoveCartItemUiState
    data class Error(val throwable: Throwable) : RemoveCartItemUiState
    data class Success(val data: Cart) : RemoveCartItemUiState
}
