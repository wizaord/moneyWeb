package com.wizaord.moneyweb.domain.transactions

data class TransactionMatch(
        val transaction: Transaction,
        val matchPoint: Double
)