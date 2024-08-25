package org.hyperskill.simplebankmanager

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val balanceViewModel: BalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBalance()
    }

    private fun initializeBalance() {
        val initialBalance = intent.getDoubleExtra("balance", 100.0)
        balanceViewModel.updateBalance(initialBalance)
    }
}