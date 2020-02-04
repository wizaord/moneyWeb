package com.wizaord.moneyweb.domain

interface InfrastructureBankAccountFamilyNotifications {

    fun notifyFamilyBankAccountUpdate(familyBankAccountsImpl: FamilyBankAccountsImpl)
}