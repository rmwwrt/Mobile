package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.FavoritesManager

@Composable
fun FavoritesScreen(
    favoritesManager: FavoritesManager,
    onGroupSelected: (String) -> Unit
) {
    val favoriteGroups by favoritesManager.favoriteGroups.collectAsState(initial = emptySet())

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(favoriteGroups.toList()) { group ->
            Text(
                text = group,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGroupSelected(group) }
                    .padding(16.dp)
            )
        }
    }
}
