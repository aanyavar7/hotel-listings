package edu.vanderbilt.client.core.data.model.login

import edu.vanderbilt.client.core.UseCase
import com.udacity.shoestore.core.data.Either
import com.udacity.shoestore.core.data.Failure
import edu.vanderbilt.client.core.models.User

class AddUserUseCase(private val loginRepository: LoginRepository) : UseCase<User, AddUserUseCase.Credentials>() {

    data class Credentials(val email: String, val password: String)

    override suspend fun run(params: Credentials): Either<Failure, User> =
        loginRepository.add(params.email, params.password)
}