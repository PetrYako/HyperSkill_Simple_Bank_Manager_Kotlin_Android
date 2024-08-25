package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.databinding.FragmentUserMenuBinding


class UserMenuFragment : Fragment() {
    private var _binding: FragmentUserMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val username = arguments?.getString("username")
        binding.userMenuWelcomeTextView.append(" $username")

        binding.userMenuViewBalanceButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_viewBalanceFragment)
        }

        binding.userMenuTransferFundsButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_transferFundsFragment)
        }

        binding.userMenuExchangeCalculatorButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_calculateExchangeFragment)
        }

        binding.userMenuPayBillsButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_payBillsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}