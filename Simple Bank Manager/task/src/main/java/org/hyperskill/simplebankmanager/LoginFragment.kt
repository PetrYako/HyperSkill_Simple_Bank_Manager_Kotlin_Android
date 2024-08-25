package org.hyperskill.simplebankmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataFromIntent()
        setupUI()
    }

    private fun initDataFromIntent() {
        val intent = (activity as AppCompatActivity).intent
        username = intent.getStringExtra("username") ?: "Lara"
        password = intent.getStringExtra("password") ?: "1234"
    }

    private fun setupUI() {
        binding.loginUsername.setText(username)
        binding.loginPassword.setText(password)

        binding.loginButton.setOnClickListener {
            if (validateCredentials()) {
                logIn()
            } else {
                showInvalidCredentialsToast()
            }
        }
    }

    private fun showInvalidCredentialsToast() {
        Toast.makeText(context, "invalid credentials", Toast.LENGTH_SHORT).show()
    }

    private fun logIn() {
        Toast.makeText(context, "logged in", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            R.id.action_loginFragment_to_userMenuFragment,
            bundleOf("username" to username)
        )
    }

    private fun validateCredentials(): Boolean {
        return binding.loginUsername.text.toString() == username &&
                binding.loginPassword.text.toString() == password
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}