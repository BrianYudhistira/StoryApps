package com.example.storyapps.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapps.api.config.ApiConfig
import com.example.storyapps.databinding.ActivitySignupBinding
import com.example.storyapps.view.login.LoginActivity

import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        regisAction()
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

    private fun regisAction() {
        binding.signupButton.setOnClickListener {
            showloading(true)
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pw = binding.passwordEditText.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty() && pw.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val api = ApiConfig.getApiService(token).register(name, email, pw)
                        val response = api.execute()

                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                // Log the response body for debugging
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle("Selamat Datang")
                                    setMessage("Anda berhasil login.")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        val intent = Intent(context, LoginActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            } else {
                                // Handle null response body
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle("Gagal Login")
                                    setMessage("Server tidak menanggapi")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        binding.passwordEditText.text?.clear()
                                    }
                                    create()
                                    show()
                                }
                            }
                        } else {
                            // Handle error response
                            // You may want to show an error message or handle it in some way
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Gagal Login")
                                setMessage("Error: ${response.code()}")
                                setPositiveButton("Lanjut") { _, _ ->
                                    binding.passwordEditText.text?.clear()
                                }
                                create()
                                show()
                            }
                        }
                    } catch (e: Exception) {
                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle("Gagal Login")
                            setMessage("Harap perika email dan password")
                            setPositiveButton("Lanjut") { _, _ ->
                                binding.passwordEditText.text?.clear()
                            }
                            create()
                            show()
                        }
                    } finally {
                        showloading(false)
                    }
                }
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
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
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