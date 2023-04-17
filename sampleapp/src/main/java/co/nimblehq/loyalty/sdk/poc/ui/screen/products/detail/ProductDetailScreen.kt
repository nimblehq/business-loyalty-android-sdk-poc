package co.nimblehq.loyalty.sdk.poc.ui.screen.products.detail

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import co.nimblehq.loyalty.sdk.model.Product
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.ui.composable.ItemDetailImage

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<ProductDetailUiState>(
        initialValue = ProductDetailUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.productDetailState.collect { value = it }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getProductDetail(productId)
    }

    ProductDetailContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
internal fun ProductDetailContent(
    uiState: ProductDetailUiState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is ProductDetailUiState.Success -> {
                ProductDetailInformation(
                    product = uiState.data,
                    onNavigateBack = onNavigateBack,
                    modifier = modifier
                )
            }
            is ProductDetailUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as ProductDetailUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
internal fun ProductDetailInformation(
    product: Product,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ItemDetailImage(
                imageUrl = product.imageUrl.orEmpty(),
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
                text = product.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = stringResource(
                    id = R.string.product_details_price,
                    product.displayPrice ?: 0
                ),
                style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF38A169)),
            )

            Divider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = product.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
