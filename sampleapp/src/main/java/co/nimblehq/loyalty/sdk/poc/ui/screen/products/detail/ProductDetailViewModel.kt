package co.nimblehq.loyalty.sdk.poc.ui.screen.products.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.nimblehq.loyalty.sdk.model.Product
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: SampleAppRepository
) : ViewModel() {

    private val _productDetailState =
        MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val productDetailState: StateFlow<ProductDetailUiState> = _productDetailState

    fun getProductDetail(productId: String) {
        viewModelScope.launch {
            repository
                .getProductDetail(productId = productId)
                .onStart { _productDetailState.emit(ProductDetailUiState.Loading) }
                .map { result -> _productDetailState.emit(ProductDetailUiState.Success(result)) }
                .catch { _productDetailState.emit(ProductDetailUiState.Error(it)) }
                .collect()
        }
    }
}

sealed interface ProductDetailUiState {
    object Loading : ProductDetailUiState
    data class Error(val throwable: Throwable) : ProductDetailUiState
    data class Success(val data: Product) : ProductDetailUiState
}
