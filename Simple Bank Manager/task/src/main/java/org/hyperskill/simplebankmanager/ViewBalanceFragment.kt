package org.hyperskill.simplebankmanager

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.hyperskill.simplebankmanager.databinding.FragmentViewBalanceBinding
import java.util.Locale

class ViewBalanceFragment : Fragment() {
    private val balanceViewModel: BalanceViewModel by activityViewModels()

    private var _binding: FragmentViewBalanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        balanceViewModel.balance.observe(viewLifecycleOwner) { balance ->
            binding.viewBalanceAmountTextView.text = "$${formatBalance(balance)}"
        }
    }

    private fun formatBalance(balance: Double?): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2

        return formatter.format(balance)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}