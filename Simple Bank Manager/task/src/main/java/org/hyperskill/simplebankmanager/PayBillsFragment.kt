package org.hyperskill.simplebankmanager

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.simplebankmanager.databinding.FragmentPayBillsBinding

class PayBillsFragment : Fragment() {
    private val balanceViewModel: BalanceViewModel by activityViewModels()

    private var _binding: FragmentPayBillsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPayBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val billInfoMap = getBillInfoMap()

        binding.payBillsShowBillInfoButton.setOnClickListener {
            val billCode = binding.payBillsCodeInputEditText.text.toString()
            val billInfo = billInfoMap[billCode]

            if (billInfo != null) {
                showBillInfoDialog(billInfo)
            } else {
                showWrongCodeDialog()
            }
        }
    }

    private fun showBillInfoDialog(billInfo: Triple<String, String, Double>) {
        AlertDialog.Builder(requireContext())
            .setTitle("Bill info")
            .setMessage(
                """
                Name: ${billInfo.first}
                BillCode: ${billInfo.second}
                Amount: $%.2f
                """.trimIndent().format(billInfo.third)
            )
            .setPositiveButton("Confirm") { _, _ ->
                if (billInfo.third > balanceViewModel.balance.value!!) {
                    showNotEnoughFundsDialog()
                } else {
                    makePayment(billInfo.third, billInfo.first)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun makePayment(amount: Double, name: String) {
        val newBalance = balanceViewModel.balance.value!!.minus(amount)
        balanceViewModel.updateBalance(newBalance)
        Toast.makeText(
            requireContext(),
            "Payment for bill $name, was successful",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showNotEnoughFundsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("Not enough funds")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showWrongCodeDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("Wrong code")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun getBillInfoMap(): Map<String, Triple<String, String, Double>> {
        return (activity as AppCompatActivity).intent.getSerializableExtra("billInfo")
                as? Map<String, Triple<String, String, Double>>
            ?: mapOf(
                "ELEC" to Triple("Electricity", "ELEC", 45.0),
                "GAS" to Triple("Gas", "GAS", 20.0),
                "WTR" to Triple("Water", "WTR", 25.5)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}