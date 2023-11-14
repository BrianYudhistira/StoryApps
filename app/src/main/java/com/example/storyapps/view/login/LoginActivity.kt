package com.example.storyapps.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.storyapps.view.viewModel.ViewModelFactory
import com.example.storyapps.api.LoginResponse
import com.example.storyapps.api.config.ApiConfig
import com.example.storyapps.data.pref.UserModel
import com.example.storyapps.databinding.ActivityLoginBinding
import com.example.storyapps.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        logAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun logAction() {
        binding.loginButton.setOnClickListener {
            showloading(true)
            val email = binding.emailEditText.text.toString()
            val pw = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && pw.isNotEmpty()) {
                val api = ApiConfig.getApiService(token).login(email, pw)
                api.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val responsebody = response.body()
                        if (responsebody != null) {
                            if (!responsebody.error) {
                                showloading(false)
                                viewModel.saveSession(
                                    UserModel(
                                        responsebody.loginResult.userId.toString(),
                                        responsebody.loginResult.name.toString(),
                                        responsebody.loginResult.token.toString()
                                    )
                                )
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle("Selamat Datang")
                                    setMessage("Anda berhasil login.")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        val intent = Intent(context, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle())
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                showloading(false)
                                setTitle("Email atau Password salah")
                                setPositiveButton("Tutup") { _, _ ->
                                    binding.passwordEditText.text?.clear()
                                }
                                create()
                                show()
                                showloading(false)
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        print("ERROR")
                        showloading(false)
                    }
                })
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showloading(loading: Boolean) {
        if (loading) {
            binding.loadbar.visibility = View.VISIBLE
        } else {
            binding.loadbar.visibility = View.INVISIBLE
        }
    }
}