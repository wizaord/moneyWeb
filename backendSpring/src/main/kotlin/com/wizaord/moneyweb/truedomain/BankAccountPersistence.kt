package com.wizaord.moneyweb.truedomain

interface BankAccountPersistence {
    fun loadBankAccountByFamilyName(familyName: String): FamilyBankAccounts?

}