package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.FavoritesManager
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    repository: ScheduleRepository,
    favoritesManager: FavoritesManager,
    selectedGroup: String,
    onGroupSelected: (String) -> Unit
) {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var scheduleLoading by remember { mutableStateOf(false) }
    var scheduleError by remember { mutableStateOf<String?>(null) }

    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var groupsLoading by remember { mutableStateOf(true) }
    var groupsError by remember { mutableStateOf<String?>(null) }

    val favoriteGroups by favoritesManager.favoriteGroups.collectAsState(initial = emptySet())
    val isFavorite = selectedGroup in favoriteGroups
    val scope = rememberCoroutineScope()

    // Fetch groups
    LaunchedEffect(Unit) {
        try {
            groups = repository.getGroups()
        } catch (e: Exception) {
            groupsError = e.message
        } finally {
            groupsLoading = false
        }
    }

    // Fetch schedule for the selected group
    LaunchedEffect(selectedGroup) {
        if (selectedGroup.isNotEmpty()) {
            try {
                scheduleLoading = true
                schedule = repository.loadSchedule(selectedGroup)
                scheduleError = null
            } catch (e: Exception) {
                scheduleError = e.message
            } finally {
                scheduleLoading = false
            }
        }
    }

    Column {
        Spacer(modifier = Modifier.height(56.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (groupsLoading) {
                CircularProgressIndicator()
            } else if (groupsError != null) {
                Text("Ошибка загрузки групп: $groupsError")
            } else {
                SearchableDropdown(
                    options = groups,
                    selectedOption = selectedGroup,
                    onOptionSelected = onGroupSelected
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                scope.launch {
                    if (isFavorite) {
                        favoritesManager.removeFavorite(selectedGroup)
                    } else {
                        favoritesManager.addFavorite(selectedGroup)
                    }
                }
            }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Добавить в избранное"
                )
            }
        }

        when {
            scheduleLoading -> CircularProgressIndicator()
            scheduleError != null -> Text("Ошибка: $scheduleError")
            else -> ScheduleList(schedule)
        }
    }
}
