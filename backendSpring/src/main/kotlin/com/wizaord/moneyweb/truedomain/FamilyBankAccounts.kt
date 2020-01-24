package com.wizaord.moneyweb.truedomain

import lombok.extern.slf4j.Slf4j

@Slf4j
class FamilyBankAccounts(
        val familyName: String
) {

    val bankAccounts = mutableListOf<BankAccount>()

    @Throws(BankAccountWithTheSameNameException::class)
    fun registerAccount(bankAccount: BankAccount) {
        accessToAccountByAccountName(bankAccount.name)?.apply { throw BankAccountWithTheSameNameException() }
        this.bankAccounts.add(bankAccount)
    }

    fun accessToAccounts() = this.bankAccounts
    fun accessToAccountsByBankname(bankName: String)= accessToAccounts().filter { it.bankName == bankName }
    fun accessToAccountByAccountName(accountName: String)= accessToAccounts().firstOrNull { it.name == accountName }

}

class BankAccountWithTheSameNameException : Exception()