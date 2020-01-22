package com.wizaord.moneyweb.truedomain

import lombok.extern.slf4j.Slf4j

@Slf4j
class BankAccountManager {

    val bankAccounts = mutableListOf<BankAccount>()

    fun registerAccount(bankAccount: BankAccount) {
        this.bankAccounts.add(bankAccount)
    }

    fun accessToAccounts() = this.bankAccounts
    fun accessToAccountsByBankname(bankName: String)= accessToAccounts().filter { it.bankName == bankName }
    fun accessToAccountByAccountName(accountName: String)= accessToAccounts().firstOrNull { it.name == accountName }

}