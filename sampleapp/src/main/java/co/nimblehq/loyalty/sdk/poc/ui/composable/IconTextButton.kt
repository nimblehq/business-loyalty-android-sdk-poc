package co.nimblehq.loyalty.sdk.poc.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun IconTextButton(
    text: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .width(256.dp)
            .padding(16.dp),
        onClick = {
            onClick.invoke()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Text(
                text = text, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
    }
}