package com.udacity.shoestore.core.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.inject
import kotlin.reflect.KProperty

/**
 * This file contains classes that support normal and secure shared preferences
 * that use [] style access. Sensible default values are also returned since the
 * [] syntax precludes providing default values.
 */
open class Preference<T : Any>(
    val default: T,
    val name: String? = null,
    val adapter: Adapter<T>? = null
) {

    val prefs by inject(SharedPreferences::class.java)

    inline operator fun <reified T : Any?> getValue(thisRef: Any?, property: KProperty<*>): T {
        return try {
            get(name ?: "${this::class.java.name}.${property.name}", default as T)
        } catch (e: Exception) {
            default as T
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(name ?: "${this::class.java.name}.${property.name}", value)
    }

    /**
     * Returns the value of the shared preference with the specified [name].
     * or the [default] value if the shared preference has no current value.
     */
    inline fun <reified T> get(name: String, default: T): T {
        with(prefs) {
            @Suppress("UNCHECKED_CAST")
            return when (default) {
                is Int -> getInt(name, default)
                is Long -> getLong(name, default)
                is Float -> getFloat(name, default)
                is Boolean -> getBoolean(name, default)
                is String -> getString(name, default) ?: ""
                else -> {
                    val string = getString(name, null)
                    when {
                        string == null -> default
                        adapter != null -> adapter.decode(string) ?: default
                        else -> Gson().fromJson<T>(string, object : TypeToken<T>() {}.type)
                    }
                }
            } as T
        }
    }

    /**
     * Sets this shared preference instance to the specified [value].
     */
    @SuppressLint("CommitPrefEdits")
    fun set(name: String, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Int -> putInt(name, value)
                is Long -> putLong(name, value)
                is Float -> putFloat(name, value)
                is Boolean -> putBoolean(name, value)
                is String -> putString(name, value)
                else -> {
                    if (adapter != null) {
                        putString(name, adapter.encode(value))
                    } else {
                        putString(name, GsonAdapter<T>().encode(value))
                    }
                }
            }.apply()
        }
    }

    /**
     * Clears the current value of this shared preference instance.
     */
    @SuppressLint("CommitPrefEdits")
    fun clear(name: String) {
        if (prefs.contains(name)) {
            with(prefs.edit()) {
                remove(name)
            }.apply()
        }
    }
}

class SecurePreferenceProvider(
    context: Context,
    private val fileName: String = "secure_shared_prefs"
) : PreferenceProvider(context) {
    override val prefs by lazy {
        val masterKey = MasterKey
            .Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}

open class PreferenceProvider(val context: Context) {
    open val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Registers the passed [listener] to receive
     * shared preference changes notifications.
     */
    fun addListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Unregisters the passed [listener] from receiving
     * shared preference change notifications.
     */
    fun removeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Clears all preferences stored with this provider.
     */
    @SuppressLint("CommitPrefEdits")
    fun clear() {
        // Clear all shared preferences.
        prefs.run {
            with(edit()) {
                clear()
            }.commit()
        }
    }

    /**
     * Return typed object from Json (hides TypeToken<T>(){}.getType()).
     */
    @Suppress("MemberVisibilityCanPrivate")
    inline fun <reified T> Gson.fromJson(json: String): T? =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)

    /**
     * Converts passed Json [json] string to an instance of [clazz].
     */
    inline fun <reified T> fromJson(json: String, clazz: Class<T>): T? =
        Gson().fromJson(json, clazz)

    /**
     * Converts [value] instance to Json. Does not support enum classes.
     * Throws an [IllegalArgumentException] if the [value] cannot be
     * restored from Json.
     */
    @Suppress("MemberVisibilityCanPrivate")
    inline fun <reified T> toJson(value: T): String = Gson().toJson(value)
}

/**
 * Adapter interface to support custom encoding and decoding
 * an object of type [T] to and from a String.
 */
interface Adapter<T> {
    fun encode(value: T): String
    fun decode(string: String): T?
}

class GsonAdapter<T> : Adapter<T> {
    override fun encode(value: T): String = Gson().toJson(value)

    override fun decode(string: String): T? =
        Gson().fromJson<T>(string, object : TypeToken<T>() {}.type)
}

/**
 * Typed preference adapter for Enum types.
 */
open class EnumAdapter<T : Enum<T>>(enumType: Class<T>) : Adapter<T> {
    private val enumConstants: Array<T> = enumType.enumConstants!!

    override fun encode(value: T): String {
        return value.ordinal.toString()
    }

    override fun decode(string: String): T? {
        try {
            return enumConstants[string.toInt()]
        } catch (e: Exception) {
            error("Preferences EnumAdapter decode failed (value=[$string]): $e")
        }
    }
}
