package com.wizaord.moneyweb.infrastructure

import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl

interface FamilyBankAccountPersistence {
    fun loadFamilyBankAccountByFamilyName(familyName: String): FamilyBankAccountsImpl?
    fun initFamily(familyBankAccountsImpl: FamilyBankAccountsImpl): FamilyBankAccountsImpl

}