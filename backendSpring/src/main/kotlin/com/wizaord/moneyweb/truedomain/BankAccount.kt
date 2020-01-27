package com.wizaord.moneyweb.truedomain

import java.time.LocalDate


data class BankAccount(
        val name: String,
        val bankName: String,
        val dateCreation: LocalDate? = LocalDate.now()) {

    fun solde() = 0
}

