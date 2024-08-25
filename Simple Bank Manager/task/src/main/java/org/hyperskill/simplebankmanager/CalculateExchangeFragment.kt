package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import org.hyperskill.simplebankmanager.databinding.FragmentCalculateExchangeBinding

class CalculateExchangeFragment : Fragment() {
    private var _binding: FragmentCalculateExchangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculateExchangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val exchangeMap =
            (activity as AppCompatActivity).intent.getSerializableExtra("exchangeMap") as? Map<String, Map<String, Double>>
                ?: mapOf(
                    "EUR" to mapOf(
                        "GBP" to 0.5,
                        "USD" to 2.0
                    ),
                    "GBP" to mapOf(
                        "EUR" to 2.0,
                        "USD" to 4.0
                    ),
                    "USD" to mapOf(
                        "EUR" to 0.5,
                        "GBP" to 0.25
                    )
                )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            exchangeMap.keys.toTypedArray()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.calculateExchangeFromSpinner.adapter = adapter
        binding.calculateExchangeToSpinner.adapter = adapter

        binding.calculateExchangeToSpinner.setSelection(1)

        addSpinnerListener(binding.calculateExchangeFromSpinner, binding.calculateExchangeToSpinner)
        addSpinnerListener(binding.calculateExchangeToSpinner, binding.calculateExchangeFromSpinner)

        binding.calculateExchangeButton.setOnClickListener {
            val amount = binding.calculateExchangeAmountEditText.text.toString()

            if (amount.isNotBlank()) {
                val result = calculate(amount.toDouble(), exchangeMap)

                val fromSymbol =
                    getSymbol(binding.calculateExchangeFromSpinner.selectedItem as String)
                val toSymbol = getSymbol(binding.calculateExchangeToSpinner.selectedItem as String)
                binding.calculateExchangeDisplayTextView.text =
                    "$fromSymbol%.2f = $toSymbol%.2f".format(amount.toDouble(), result)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter amount",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun calculate(amount: Double, exchangeMap: Map<String, Map<String, Double>>): Double {
        val from = binding.calculateExchangeFromSpinner.selectedItem as String
        val to = binding.calculateExchangeToSpinner.selectedItem as String
        val rate = exchangeMap[from]?.get(to)!!.toDouble()
        return amount * rate
    }

    private fun getSymbol(currency: String): String {
        return when (currency) {
            "EUR" -> "€"
            "GBP" -> "£"
            "USD" -> "$"
            else -> ""
        }
    }

    private fun addSpinnerListener(
        spinner: Spinner,
        secondSpinner: Spinner
    ) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                val secondSpinnerItem = secondSpinner.selectedItem as String

                if (selectedItem == secondSpinnerItem) {
                    Toast.makeText(
                        requireContext(),
                        "Cannot convert to same currency",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (binding.calculateExchangeToSpinner.selectedItemPosition == binding.calculateExchangeToSpinner.count.minus(
                            1
                        )
                    ) {
                        binding.calculateExchangeToSpinner.setSelection(0)
                    } else {
                        binding.calculateExchangeToSpinner.setSelection(binding.calculateExchangeToSpinner.selectedItemPosition + 1)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}