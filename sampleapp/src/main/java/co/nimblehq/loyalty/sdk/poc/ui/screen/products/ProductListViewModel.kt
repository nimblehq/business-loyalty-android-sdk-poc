package co.nimblehq.loyalty.sdk.poc.ui.screen.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.Product
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    repository: SampleAppRepository
) : ViewModel() {

    val uiState: SharedFlow<ProductListUiState> = repository
        .products
        .map { data -> ProductListUiState.Success(data) as ProductListUiState }
        .catch { cause: Throwable ->
            emit(ProductListUiState.Error(cause))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ProductListUiState.Loading
        )
}

sealed interface ProductListUiState {
    object Loading : ProductListUiState
    data class Error(val throwable: Throwable) : ProductListUiState
    data class Success(val data: List<Product>) : ProductListUiState
}
