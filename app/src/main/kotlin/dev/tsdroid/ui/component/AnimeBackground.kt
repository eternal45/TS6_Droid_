package dev.tsdroid.ui.component

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun AnimeBackground(enabled: Boolean) {
    if (!enabled) return

    var imageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL("https://www.loliapi.com/acg/pe/")
                val conn = url.openConnection() as HttpURLConnection
                conn.instanceFollowRedirects = false
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                val redirect = conn.getHeaderField("Location")
                if (!redirect.isNullOrBlank()) {
                    imageUrl = redirect
                } else {
                    imageUrl = conn.url.toString()
                }
                conn.disconnect()
            } catch (_: Exception) {
            }
        }
    }

    val url = imageUrl
    if (url.isNullOrBlank()) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.25f)
                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.blur(30.dp)
                    } else {
                        Modifier
                    }
                )
        )
    }
}
