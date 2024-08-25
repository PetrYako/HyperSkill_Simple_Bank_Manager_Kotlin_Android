package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.databinding.FragmentTransferFundsBinding

class TransferFundsFragment : Fragment() {
    private val balanceViewModel: BalanceViewModel by activityViewModels()

    private var _binding: FragmentTransferFundsBinding? = null
    private val binding get() = _binding!!

    private val accountRegex = "(sa|ca)\\d{4}".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferFundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.transferFundsButton.setOnClickListener {
            if (validateInput()) {
                transferFunds()
            }
        }
    }

    private fun validateInput(): Boolean {
        var validationFailed = false

        if (!isAccountNumberValid()) {
            binding.transferFundsAccountEditText.error = "Invalid account number"
            validationFailed = true
        }

        if (!isAmountValid()) {
            binding.transferFundsAmountEditText.error = "Invalid amount"
            validationFailed = true
        }

        return !validationFailed
    }

    private fun isAccountNumberValid(): Boolean {
        val accountNumber = binding.transferFundsAccountEditText.text.toString()
        return accountRegex.matches(accountNumber)
    }

    private fun isAmountValid(): Boolean {
        val amount = binding.transferFundsAmountEditText.text.toString().toDoubleOrNull()
        return amount != null && amount > 0
    }

    private fun transferFunds() {
        val account = binding.transferFundsAccountEditText.text.toString()
        val amount = binding.transferFundsAmountEditText.text.toString().toDouble()
        val balance = balanceViewModel.balance.value!!
        if (amount > balance) {
            Toast.makeText(
                context,
                "Not enough funds to transfer $%.2f".format(amount),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            balanceViewModel.updateBalance(balanceViewModel.balance.value!! - amount)
            Toast.makeText(
                context,
                "Transferred $%.2f to account %s".format(amount, account),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}