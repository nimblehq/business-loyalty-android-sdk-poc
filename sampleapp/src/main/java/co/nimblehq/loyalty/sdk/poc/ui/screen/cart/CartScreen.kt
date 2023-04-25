package co.nimblehq.loyalty.sdk.poc.ui.screen.cart

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.loyalty.sdk.model.Cart
import co.nimblehq.loyalty.sdk.poc.R
import co.nimblehq.loyalty.sdk.poc.ui.composable.RedeemedRewardImage
import co.nimblehq.loyalty.sdk.poc.ui.theme.Black
import co.nimblehq.loyalty.sdk.poc.ui.theme.Grey
import co.nimblehq.loyalty.sdk.poc.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<CartUiState>(
        initialValue = CartUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val selectedCartItem = remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
                selectedCartItem.value = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    setShowDialog(false)
                    viewModel.removeItem(itemId = selectedCartItem.value)
                    selectedCartItem.value = ""
                })
                { Text(text = stringResource(id = android.R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    setShowDialog(false)
                    selectedCartItem.value = ""
                })
                { Text(text = stringResource(id = android.R.string.cancel)) }
            },
            title = { Text(text = stringResource(id = R.string.cart_remove_item_confirmation_title)) },
            text = { Text(text = stringResource(id = R.string.cart_remove_item_confirmation_message)) }
        )
    }

    val removeCartItemUiState by produceState<RemoveCartItemUiState>(
        initialValue = RemoveCartItemUiState.Init,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.removeCartItemState.collect { value = it }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(removeCartItemUiState) {
        when (removeCartItemUiState) {
            is RemoveCartItemUiState.Success -> {
                context.getString(R.string.remove_cart_item_success)
            }
            is RemoveCartItemUiState.Error -> {
                (removeCartItemUiState as RemoveCartItemUiState.Error).throwable.message
            }
            is RemoveCartItemUiState.Processing -> {
                context.getString(R.string.remove_cart_item_processing)
            }
            else -> null
        }?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier.padding(vertical = 8.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.title_cart))
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
        CartContent(
            uiState = uiState,
            showDialog = setShowDialog,
            selectedCartItem = selectedCartItem,
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
internal fun CartContent(
    uiState: CartUiState,
    showDialog: (Boolean) -> Unit,
    selectedCartItem: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as Activity
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is CartUiState.Success -> {
                if (uiState.data.orderLineItems.isNullOrEmpty()) {
                    EmptyCartContent(modifier = modifier)
                } else {
                    CartDetails(
                        cart = uiState.data,
                        onShowDialogRequest = showDialog,
                        selectedCartItem = selectedCartItem,
                        modifier = modifier.fillMaxSize()
                    )
                }
            }
            is CartUiState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Toast.makeText(
                    activity,
                    (uiState as CartUiState.Error).throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
internal fun CartDetails(
    cart: Cart,
    onShowDialogRequest: (Boolean) -> Unit,
    selectedCartItem: MutableState<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(cart.orderLineItems.orEmpty()) { orderItem ->
            CartOrderLineItem(
                currency = cart.currency.orEmpty(),
                orderItem = orderItem,
                selectedCartItem = selectedCartItem,
                onShowDialogRequest = onShowDialogRequest
            )

            Divider(modifier = Modifier.padding(horizontal = 8.dp))
        }
        item {
            CartTotal(
                cart = cart,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = White)
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun CartOrderLineItem(
    currency: String,
    orderItem: Cart.OrderLineItem?,
    onShowDialogRequest: (Boolean) -> Unit,
    selectedCartItem: MutableState<String>,
) {
    Card(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            RedeemedRewardImage(
                imageUrl = orderItem?.product?.imageUrl.orEmpty(),
            )

            Text(
                text = stringResource(R.string.cart_order_item_quantity, orderItem?.quantity ?: 0),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Grey
                ),
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = orderItem?.product?.name.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_remove_cart_item),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                onClick = {
                                    selectedCartItem.value = orderItem?.id.orEmpty()
                                    onShowDialogRequest(true)
                                }
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isDiscountApplied = orderItem?.price != orderItem?.finalPrice

                    AnimatedVisibility(visible = isDiscountApplied) {
                        Text(
                            text = stringResource(
                                id = R.string.cart_order_price_with_currency,
                                orderItem?.price.orEmpty(),
                                currency
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Grey,
                                textDecoration = TextDecoration.LineThrough,
                                fontWeight = FontWeight.Normal
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = stringResource(
                            id = R.string.cart_order_price_with_currency,
                            orderItem?.finalPrice.orEmpty(),
                            currency
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Black
                        ),
                    )
                }
            }
        }
    }
}

@Composable
internal fun EmptyCartContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cart_empty),
            contentDescription = null,
            modifier = Modifier.size(width = 210.dp, height = 210.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.cart_empty_title),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.cart_empty_description),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
internal fun CartTotal(
    cart: Cart,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.cart_sub_total),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(
                    id = R.string.cart_order_price_with_currency,
                    cart.currency.orEmpty(),
                    cart.subtotalPrice.orEmpty()
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.cart_total_shipment_cost),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(
                    id = R.string.cart_order_price_with_currency,
                    cart.currency.orEmpty(),
                    cart.shipmentCost.orEmpty()
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.cart_total),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = stringResource(
                    id = R.string.cart_order_price_with_currency,
                    cart.currency.orEmpty(),
                    cart.finalPrice.orEmpty()
                ),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

