package org.hyperskill.simplebankmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BalanceViewModel : ViewModel() {
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    fun updateBalance(newBalance: Double) {
        _balance.value = newBalance
    }
}