package com.example.videocatalog.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CatalogScreen(state: UiState, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Button(onClick = onRefresh, modifier = Modifier.padding(16.dp)) {
            Text("Refresh")
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (state) {
                UiState.Loading -> CircularProgressIndicator()

                is UiState.Error -> Text(
                    state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                is UiState.Loaded ->
                    if (state.videos.isEmpty()) {
                        Text("No videos found")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.videos, key = { it.fileName }) { info ->
                                VideoItem(info)
                            }
                        }
                    }
            }
        }
    }
}
