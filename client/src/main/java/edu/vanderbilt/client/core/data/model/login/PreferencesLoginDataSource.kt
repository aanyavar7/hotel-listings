package edu.vanderbilt.client.core.data.model.login


import android.content.SharedPreferences
import com.google.gson.Gson
import edu.vanderbilt.client.core.models.User

/**
 * Secure shared preferences version of LoginDataSource.
 */
class PreferencesLoginDataSource(private val sharedPrefs: SharedPreferences) : MemoryLoginDataSource() {
    /** Lazy load from secure preferences and keep in memory */
    override val users: MutableList<User> by lazy {
        sharedPrefs.getString(USER_LIST_KEY, null)?.let { json ->
            Gson().fromJson(json, Array<User>::class.java).toMutableList()
        } ?: mutableListOf()
    }

    override fun saveUsers() {
        sharedPrefs
            .edit()
            .putString(
                USER_LIST_KEY,
                Gson().toJson(users.toTypedArray(), Array<User>::class.java)
            )
            .apply()
    }

    companion object {
        const val USER_LIST_KEY: String = "user_list_key"
    }
}