package edu.vanderbilt.client.core.data.model.login

import edu.vanderbilt.client.core.UseCase
import com.udacity.shoestore.core.data.Failure
import com.udacity.shoestore.core.data.Either
import edu.vanderbilt.client.core.models.User
import edu.vanderbilt.client.core.data.model.login.LoginUseCase.Credentials

class LoginUseCase(private val loginRepository: LoginRepository) : UseCase<User, Credentials>() {

    data class Credentials(val email: String, val password: String)

    override suspend fun run(params: Credentials): Either<Failure, User> =
        loginRepository.login(params.email, params.password)
}