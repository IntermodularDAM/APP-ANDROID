package dam.intermodular.app.core.dataStore

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
val APPLICATION_TOKEN_KEY = stringPreferencesKey("refresh_token")
val ACCESS_ROLE_KEY = stringPreferencesKey("access_role")
val FAVORITES_KEY = stringSetPreferencesKey("favorites")
