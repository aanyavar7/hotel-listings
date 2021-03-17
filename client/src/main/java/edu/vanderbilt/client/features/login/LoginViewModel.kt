package edu.vanderbilt.client.login

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.vanderbilt.client.core.data.model.login.AddUserUseCase
import edu.vanderbilt.client.core.data.model.login.LoginUseCase
import edu.vanderbilt.client.core.models.User
import edu.vanderbilt.client.features.BaseViewModel
import edu.vanderbilt.client.common.Event
import com.udacity.shoestore.core.data.Either
import com.udacity.shoestore.core.data.Error
import com.udacity.shoestore.core.data.Failure
import com.udacity.shoestore.core.data.Success
import edu.vanderbilt.client.R
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class LoginViewModel(app: Application) : BaseViewModel(app) {

    private val addUserUseCase by inject(AddUserUseCase::class.java)
    private val loginUseCase by inject(LoginUseCase::class.java)

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState>
        get() = _loginForm

    private val _loginResult = MutableLiveData<Event<Either<Failure, User>>>()
    val loginResult: LiveData<Event<Either<Failure, User>>>
        get() = _loginResult

    var email = "a@b.c"
    var password = "abcdef"

    fun afterPasswordTextChanged(editable: Editable?) {
        if (password != editable.toString()) {
            password = editable.toString()
            _loginForm.value = when {
                password.isEmpty() -> LoginFormState(isDataValid = true)
                !isPasswordValid(password) ->
                    LoginFormState(passwordError = R.string.invalid_password)
                else -> LoginFormState(isDataValid = true)
            }
        }
    }

    fun afterUsernameTextChanged(editable: Editable?) {
        if (email != editable.toString()) {
            email = editable.toString()
            _loginForm.value = when {
                email.isEmpty() -> LoginFormState(isDataValid = true)
                !isUserNameValid(email) ->
                    LoginFormState(emailError = R.string.invalid_email)
                else ->
                    LoginFormState(isDataValid = true)
            }
        }
    }

    fun onLogin() {
        if (!isUserNameValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
            return
        }

        if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            return
        }

        login(email, password)
    }

    fun onRegister() {
        if (!isUserNameValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
            return
        }

        if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            return
        }

        runWithProgress {
            addUserUseCase.run(AddUserUseCase.Credentials(email, password)).either(
                onSuccess = { _loginResult.postValue(Event(Success(it))) },
                onError = { _loginResult.postValue(Event(Error(it))) }
            )
        }
    }

    init {
        Timber.d("Creating a new LoginViewModel instance")
    }

    fun login(email: String, password: String) {
        runWithProgress {
            loginUseCase.run(LoginUseCase.Credentials(email, password)).either(
                onSuccess = { _loginResult.postValue(Event(Success(it))) },
                onError = { _loginResult.postValue(Event(Error(it))) }
            )
        }
    }

    private fun isUserNameValid(email: String): Boolean {
        return if (email.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    /** Data validation state of the login form. */
    data class LoginFormState(
        val emailError: Int? = null,
        val passwordError: Int? = null,
        val registerError: Int? = null,
        val isDataValid: Boolean = false
    )

    private fun authenticate() {
        val accountManager: AccountManager = AccountManager.get(context)
        val options = Bundle()

        val accountType = "edu.vanderbilt.server"
        val account = accountManager.getAccountsByType(accountType).firstOrNull()
        val authTokenType = accountType

        if (account == null) {

        } else {
//            public AccountManagerFuture<Bundle> getAuthToken(
//                    final Account account, final String authTokenType, final Bundle options,
//            final boolean notifyAuthFailure,
//            AccountManagerCallback<Bundle> callback, Handler handler) {
            accountManager.getAuthToken(
                account,
                authTokenType,
                options,
                true,
                OnTokenAcquired(),
                Handler(Looper.myLooper()!!) { msg -> true }
            )
        }
    }

    private class OnTokenAcquired : AccountManagerCallback<Bundle> {

        override fun run(result: AccountManagerFuture<Bundle>) {
            // Get the result of the operation from the AccountManagerFuture.
            val bundle: Bundle = result.result

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            val token: String? = bundle.getString(AccountManager.KEY_AUTHTOKEN)
        }
    }
}
