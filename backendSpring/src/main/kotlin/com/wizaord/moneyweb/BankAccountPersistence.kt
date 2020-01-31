package com.wizaord.moneyweb

import com.wizaord.moneyweb.truedomain.FamilyBankAccountsImpl

interface BankAccountPersistence {
    fun loadBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl?

}