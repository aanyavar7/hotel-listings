package edu.vanderbilt.client.core.data.model.login

import android.os.SystemClock.sleep
import com.udacity.shoestore.core.data.Either
import com.udacity.shoestore.core.data.Error
import com.udacity.shoestore.core.data.Failure
import com.udacity.shoestore.core.data.Success
import edu.vanderbilt.client.core.models.User
import edu.vanderbilt.client.login.AddUserFailure
import edu.vanderbilt.client.login.LoginFailure
import kotlin.random.Random

/**
 * In memory version of LoginDataSource.
 */
open class MemoryLoginDataSource : LoginDataSource {

    open val users = mutableListOf<User>()

    override fun addUser(email: String, password: String): Either<Failure, User> =
        try {
            if (users.firstOrNull { it.name == email.trim() } != null) {
                Error(AddUserFailure.UserAlreadyExists())
            } else {
                users.add(User(name = "", email = email, password = password))
                saveUsers()
                login(email, password)
            }
        } catch (t: Throwable) {
            Error(Failure.UnexpectedFailure(t))
        }

    override fun login(email: String, password: String): Either<Failure, User> =
        try {
            val user = users.firstOrNull { it.email == email }
            when {
                user == null -> Error(LoginFailure.NotRegistered())
                user.password != password.trim() -> Error(LoginFailure.NotAuthorized())
                else -> {
                    /** Don't allow password to leave this secure class */
                    Success(user.copy(password = user.password.replace(".".toRegex(), "*")))
                }
            }
        } catch (t: Throwable) {
            Error(Failure.UnexpectedFailure(t))
        }

    override fun find(email: String): User? = users.firstOrNull { it.email == email }

    override fun logout() {
        TODO("Not yet implemented.")
    }

    @Suppress("unused") // For testing only.
    private fun simulateNetworkDelay() {
        sleep(Random.nextLong(from = 1000, until = 2000))
    }
}