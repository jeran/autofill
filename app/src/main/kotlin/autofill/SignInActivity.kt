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

class SignInActivity : AppCompatActivity() {

    private lateinit var authenticationService: AuthenticationService
    private lateinit var error: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        authenticationService = AuthenticationService(dataStore)

        findViewById<Button>(R.id.submit).setOnClickListener { signIn() }
        findViewById<TextView>(R.id.sign_up_cta).setOnClickListener { navigateToSignUp() }
        error = findViewById(R.id.error)
    }

    private fun signIn() {
        lifecycleScope.launch {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            when (authenticationService.signIn(username = username, password = password)) {
                true -> handleSuccess()
                else -> handleFailure()
            }
        }
    }

    private fun handleSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleFailure() {
        error.text = resources.getString(R.string.sign_in_error)
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }
}
