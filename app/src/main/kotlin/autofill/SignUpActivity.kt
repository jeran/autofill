package autofill

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.jeran.autofill.R
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var authenticationService: AuthenticationService
    private lateinit var error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        authenticationService = AuthenticationService(dataStore)

        findViewById<Button>(R.id.submit).setOnClickListener { signIn() }
        findViewById<TextView>(R.id.sign_in_cta).setOnClickListener { navigateToSignIn() }
        error = findViewById(R.id.error)
    }

    private fun signIn() {
        lifecycleScope.launch {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()
            val result = authenticationService.signUp(
                username = username,
                password = password,
                confirmPassword = confirmPassword,
            )
            when (result) {
                AuthenticationService.SignUpResult.Success -> handleSuccess()
                AuthenticationService.SignUpResult.PasswordsDoNotMatch -> handleFailure(
                    stringResource = R.string.sign_up_error_passwords_do_not_match,
                )
                AuthenticationService.SignUpResult.UsernameTaken -> handleFailure(
                    stringResource = R.string.sign_up_error_username_taken,
                )
            }
        }
    }

    private fun handleSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleFailure(stringResource: Int) {
        error.text = resources.getString(stringResource)
    }

    private fun navigateToSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
