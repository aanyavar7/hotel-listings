package edu.vanderbilt.client.login

import com.udacity.shoestore.core.data.Failure

class AddUserFailure {
    class UserAlreadyExists: Failure.FeatureFailure()
}