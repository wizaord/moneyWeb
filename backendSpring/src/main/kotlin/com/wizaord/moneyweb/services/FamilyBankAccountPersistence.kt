package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.FamilyBankAccounts
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl

interface FamilyBankAccountPersistence {
    fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl?

}