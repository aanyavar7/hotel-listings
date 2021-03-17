package edu.vanderbilt.client.core.data.model.login

import com.udacity.shoestore.core.data.Either
import com.udacity.shoestore.core.data.Failure
import edu.vanderbilt.client.core.models.User

/**
 * Data source for adding and logging in users.
 */
interface LoginDataSource {
    fun addUser(email: String, password: String): Either<Failure, User>
    fun login(email: String, password: String): Either<Failure, User>
    fun find(email: String): User?
    fun logout()
    fun saveUsers() {}
}