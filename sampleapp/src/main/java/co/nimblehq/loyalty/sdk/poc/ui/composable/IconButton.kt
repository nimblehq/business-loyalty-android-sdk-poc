package co.nimblehq.loyalty.sdk.poc.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            Text(text = text, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}