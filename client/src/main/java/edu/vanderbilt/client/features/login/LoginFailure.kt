package edu.vanderbilt.client.login

import com.udacity.shoestore.core.data.Failure

class LoginFailure {
    class NotAuthorized: Failure.FeatureFailure()
    class NotRegistered: Failure.FeatureFailure()
}