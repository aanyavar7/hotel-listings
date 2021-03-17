package edu.vanderbilt.client.core.di

import edu.vanderbilt.client.core.data.model.login.*
import edu.vanderbilt.client.login.LoginViewModel
import com.udacity.shoestore.core.prefs.PreferenceProvider
import com.udacity.shoestore.core.prefs.SecurePreferenceProvider
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    /** Stores user list in memory. */
    single(named("MemoryDataSource")) {
        MemoryLoginDataSource()
    } bind LoginDataSource::class

    /** Stores user list in secure shared preferences. */
    single(named("PreferencesDataSource")) {
        PreferencesLoginDataSource(get(named("SecurePreferences")))
    } bind LoginDataSource::class

    single { LoginRepository(get(named("PreferencesDataSource"))) }
    single { LoginUseCase(get()) }
    single { AddUserUseCase(get()) }

    /** Normal shared preferences */
    single(named("PreferenceProvider")) { PreferenceProvider(get()).prefs  }

    /** Secure shared preferences */
    single(named("SecurePreferences")) { SecurePreferenceProvider(get()).prefs }

    viewModel { LoginViewModel(get()) }
}