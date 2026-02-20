package com.example.collegeschedule.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val FAVORITE_GROUPS_KEY = stringSetPreferencesKey("favorite_groups")
    }

    val favoriteGroups: Flow<Set<String>> = dataStore.data.map {
        it[FAVORITE_GROUPS_KEY] ?: emptySet()
    }

    suspend fun addFavorite(group: String) {
        dataStore.edit {
            val currentFavorites = it[FAVORITE_GROUPS_KEY] ?: emptySet()
            it[FAVORITE_GROUPS_KEY] = currentFavorites + group
        }
    }

    suspend fun removeFavorite(group: String) {
        dataStore.edit {
            val currentFavorites = it[FAVORITE_GROUPS_KEY] ?: emptySet()
            it[FAVORITE_GROUPS_KEY] = currentFavorites - group
        }
    }
}
