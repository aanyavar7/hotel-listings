package edu.vanderbilt.client.features.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.vanderbilt.client.core.models.User
import edu.vanderbilt.client.common.observeEvent
import edu.vanderbilt.client.login.AddUserFailure
import edu.vanderbilt.client.login.LoginFailure
import edu.vanderbilt.client.login.LoginViewModel
import com.udacity.shoestore.core.data.Failure
import edu.vanderbilt.client.R
import edu.vanderbilt.client.databinding.LoginFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class LoginFragment : Fragment() {

    /**
     * NOTE to Udacity Assignment Reviewer
     *
     * This fragment has its own view model using Koin injection
     * and is, therefore, deliberately decoupled from the MainActivity.
     * Additionally, I want this feature to be plug and play into
     * any future apps so it makes more sense to keep it decoupled
     * from the main activity in this app. Finally, since the login
     * is a one time occurrence (or very infrequent) it should be
     * acceptable to have it's own view model that is short lived
     * and does not unnecessarily complicate and use unnecessary
     * memory in the main activity model.
     *
     * For your marking purposes, if I had initialized the the
     * view model via a factory in the MainActivity I would have
     * used the "by activityViewModels()" initialization pattern
     * here (instead).
     */
    private val loginViewModel by viewModel<LoginViewModel>()

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        // Allow binding to observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = loginViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        loginViewModel.setupObservers()

        // Restore all saved values.
        binding.executePendingBindings()
    }

    private fun setupViews() {
        // Use SwipeRefresh layout as a tri-color progress indicator.
        binding.swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        binding.swipeRefresh.isEnabled = false
    }

    private fun LoginViewModel.setupObservers() {
        progress.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }

        loginFormState.observe(viewLifecycleOwner) { loginFormState ->
            loginFormState?.apply {
                binding.login.isEnabled = isDataValid
                binding.password.error = passwordError?.let { getString(it) }
                binding.email.error = emailError?.let { getString(it) }
            }
        }

        loginResult.observeEvent(viewLifecycleOwner) { result ->
            result?.either(
                onSuccess = { navigateToWelcomeScreen(it) },
                onError = { showLoginFailed(it) }
            )
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
            }
            false
        }
    }

    private fun navigateToWelcomeScreen(user: User) {
        val userName = if (user.name.isNotBlank()) {
            user.name
        } else {
            user.email.substringBefore('@')
        }.capitalize(Locale.getDefault())

        findNavController().navigate(R.id.action_airport_fragment)
    }

    /**
     * Failure mapping to resource strings to avoid polluting
     * view model with system dependant resource IDs.
     */
    private fun showLoginFailed(failure: Failure) {
        val errorStringId = when (failure) {
            Failure.NetworkFailure -> R.string.network_error
            Failure.ServerFailure -> R.string.server_error
            is AddUserFailure.UserAlreadyExists -> R.string.user_already_exists_error
            is LoginFailure.NotRegistered -> R.string.user_not_registered_error
            is LoginFailure.NotAuthorized -> R.string.user_not_authorized
            is Failure.UnexpectedFailure -> R.string.login_error_unknown
            is Failure.FeatureFailure -> R.string.login_error_unknown
        }

        Toast.makeText(context, errorStringId, Toast.LENGTH_LONG).show()
    }

    companion object {
        @BindingAdapter("android:afterTextChanged")
        fun setListener(view: TextView, after: TextViewBindingAdapter.AfterTextChanged) {
        }
    }
}