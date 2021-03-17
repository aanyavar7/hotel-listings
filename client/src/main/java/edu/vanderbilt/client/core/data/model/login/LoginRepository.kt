package edu.vanderbilt.client.core.data.model.login

import com.udacity.shoestore.core.data.Either
import com.udacity.shoestore.core.data.Failure
import com.udacity.shoestore.core.data.Success
import edu.vanderbilt.client.core.models.User

/**
 * Class that requests authentication and user information from
 * the remote data source and maintains an in-memory cache of
 * login status and user credentials information.
 */
class LoginRepository(private val dataSource: LoginDataSource) {
    private var user: User? = null

    init {
        user = null
    }

    @Suppress("unused") //TODO: for future use
    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(email: String, password: String): Either<Failure, User> =
        dataSource.login(email, password).also { result ->
            if (result is Success) {
                user = result.value
            }
        }

    fun add(email: String, password: String): Either<Failure, User> =
        dataSource.addUser(email, password).also { result ->
            if (result is Success) {
                user = result.value
            }
        }

    fun find(email: String): User? = dataSource.find(email)
}