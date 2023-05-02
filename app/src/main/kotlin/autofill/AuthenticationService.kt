package autofill

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class AuthenticationService(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun signIn(username: String, password: String): Boolean {
        return dataStore.data.first()[stringPreferencesKey(username)] == password
    }

    suspend fun signUp(
        username: String,
        password: String,
        confirmPassword: String,
    ): SignUpResult {
        return when {
            password != confirmPassword -> {
                SignUpResult.PasswordsDoNotMatch
            }
            dataStore.data.first().contains(stringPreferencesKey(username)) -> {
                SignUpResult.UsernameTaken
            }
            else -> {
                SignUpResult.Success.also {
                    dataStore.edit { preferences ->
                        preferences[stringPreferencesKey(username)] = password
                    }
                }
            }
        }
    }

    enum class SignUpResult {
        Success,
        PasswordsDoNotMatch,
        UsernameTaken,
    }
}
