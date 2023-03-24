package co.nimblehq.loyalty.sdk.poc.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RewardInfoChip(
    isActive: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            isActive -> MaterialTheme.colorScheme.onSecondary
            else -> Color.Transparent
        },
        contentColor = when {
            isActive -> MaterialTheme.colorScheme.onPrimary
            else -> Color.LightGray
        },
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = when {
                isActive -> MaterialTheme.colorScheme.primary
                else -> Color.LightGray
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )
    }
}