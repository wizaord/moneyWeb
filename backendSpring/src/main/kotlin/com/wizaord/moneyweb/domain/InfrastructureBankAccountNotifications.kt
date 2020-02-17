package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction

interface InfrastructureBankAccountNotifications {

    fun notifyNewTransaction(accountId: String, transaction: Transaction)
    fun notifyRemoveTransaction(transaction: Transaction)
    fun notifyAccountUpdate(accountImpl: BankAccountImpl)
}