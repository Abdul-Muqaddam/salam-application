package com.webinane.salam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ZakatState(
    val cashInHand: String = "",
    val bankBalance: String = "",
    val goldValue: String = "",
    val silverValue: String = "",
    val businessAssets: String = "",
    val otherSavings: String = "",
    val debts: String = "",
    val totalAssets: Double = 0.0,
    val totalLiabilities: Double = 0.0,
    val netWealth: Double = 0.0,
    val zakatPayable: Double = 0.0,
    val isNisabReached: Boolean = false,
    val showResult: Boolean = false,
    // Updated to correct PKR values (Feb 2026)
    val goldNisab: Double = 3960000.0,  // PKR for 87.48g gold
    val silverNisab: Double = 472000.0,  // PKR for 612.36g silver
    val goldPricePerGram: Double = 45280.0,  // PKR per gram
    val silverPricePerGram: Double = 771.0,  // PKR per gram
    val lastPriceUpdate: Long = 0L,
    val isLoadingPrices: Boolean = false
)

class ZakatViewModel(
    private val metalPriceService: com.webinane.salam.data.remote.MetalPriceService
) : ViewModel() {
    private val _state = MutableStateFlow(ZakatState())
    val state: StateFlow<ZakatState> = _state.asStateFlow()

    init {
        // Fetch prices asynchronously - fetchMetalPrices already uses viewModelScope
        // Falls back to hardcoded values if API fails
        fetchMetalPrices()
    }

    fun onCashInHandChange(value: String) = _state.update { it.copy(cashInHand = value) }
    fun onBankBalanceChange(value: String) = _state.update { it.copy(bankBalance = value) }
    fun onGoldValueChange(value: String) = _state.update { it.copy(goldValue = value) }
    fun onSilverValueChange(value: String) = _state.update { it.copy(silverValue = value) }
    fun onBusinessAssetsChange(value: String) = _state.update { it.copy(businessAssets = value) }
    fun onOtherSavingsChange(value: String) = _state.update { it.copy(otherSavings = value) }
    fun onDebtsChange(value: String) = _state.update { it.copy(debts = value) }

    fun fetchMetalPrices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingPrices = true) }
            
            val result = metalPriceService.fetchMetalPrices()
            
            if (result.isSuccess) {
                val prices = result.getOrNull()!!
                _state.update { 
                    it.copy(
                        goldNisab = prices.goldNisab,
                        silverNisab = prices.silverNisab,
                        goldPricePerGram = prices.goldPricePerGram,
                        silverPricePerGram = prices.silverPricePerGram,
                        lastPriceUpdate = prices.lastUpdated,
                        isLoadingPrices = false
                    )
                }
            } else {
                // Use fallback values on error
                val fallback = metalPriceService.getFallbackPrices()
                _state.update { 
                    it.copy(
                        goldNisab = fallback.goldNisab,
                        silverNisab = fallback.silverNisab,
                        goldPricePerGram = fallback.goldPricePerGram,
                        silverPricePerGram = fallback.silverPricePerGram,
                        lastPriceUpdate = fallback.lastUpdated,
                        isLoadingPrices = false
                    )
                }
            }
        }
    }

    fun calculateZakat() {
        _state.update { currentState ->
            val totalAssets = (currentState.cashInHand.toDoubleOrNull() ?: 0.0) +
                    (currentState.bankBalance.toDoubleOrNull() ?: 0.0) +
                    (currentState.goldValue.toDoubleOrNull() ?: 0.0) +
                    (currentState.silverValue.toDoubleOrNull() ?: 0.0) +
                    (currentState.businessAssets.toDoubleOrNull() ?: 0.0) +
                    (currentState.otherSavings.toDoubleOrNull() ?: 0.0)
            
            val totalLiabilities = currentState.debts.toDoubleOrNull() ?: 0.0
            val netWealth = totalAssets - totalLiabilities
            
            // Convert Nisab from PKR to USD (1 USD ≈ 280 PKR)
            // User enters values in USD, so we need to compare in USD
            val silverNisabUSD = currentState.silverNisab / 280.0
            val goldNisabUSD = currentState.goldNisab / 280.0
            
            // Using Silver Nisab as default recommendation as per design
            val isNisabReached = netWealth >= silverNisabUSD
            val zakatPayable = if (isNisabReached) netWealth * 0.025 else 0.0
            
            currentState.copy(
                totalAssets = totalAssets,
                totalLiabilities = totalLiabilities,
                netWealth = netWealth,
                zakatPayable = zakatPayable,
                isNisabReached = isNisabReached,
                showResult = true
            )
        }
    }
}
