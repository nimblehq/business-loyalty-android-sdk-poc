package co.nimblehq.loyalty.sdk.poc.ui.screen.products

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.loyalty.sdk.model.Product
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.ui.composable.ItemImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateProductDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<ProductListUiState>(
        initialValue = ProductListUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    Scaffold(
        modifier = modifier.padding(vertical = 8.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_product_list))
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
        Column(modifier = modifier.padding(paddingValues)) {
            ProductListContent(
                uiState = uiState,
                onProductItemClick = { product ->
                    onNavigateProductDetail.invoke(product.id.orEmpty())
                },
            )
        }
    }
}

@Composable
internal fun ProductListContent(
    uiState: ProductListUiState,
    onProductItemClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is ProductListUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(items = uiState.data) { product ->
                        ProductItem(
                            product = product,
                            onProductItemClick = onProductItemClick,
                        )
                    }
                }
            }
            is ProductListUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as ProductListUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductItemClick: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(
                border = BorderStroke(width = 1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onProductItemClick.invoke(product) },
    ) {
        Column {
            ItemImage(
                imageUrl = product.imageUrl.orEmpty(),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = product.name.orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.aspectRatio(3.25f) // FIXME Stretch items to same height
                )
                Text(
                    text = product.description.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )

                Spacer(modifier = Modifier.padding(top = 8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onProductItemClick.invoke(product)
                    }
                ) {
                    Text(stringResource(R.string.main_product_list_view_details))
                }
            }
        }
    }
}
