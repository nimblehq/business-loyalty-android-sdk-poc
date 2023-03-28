package co.nimblehq.loyalty.sdk.poc.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.nimblehq.loyalty.sdk.poc.R
import coil.compose.SubcomposeAsyncImage

@Composable
fun RewardDetailImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        loading = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                CircularProgressIndicator(
                    modifier = modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    strokeWidth = 2.dp
                )
            }
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.ic_nimble_logo),
                contentDescription = null,
                modifier = modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(16.dp)
            )
        },
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
    )
}