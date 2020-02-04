package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.transactions.Transaction

interface InfrastructureBankAccountNotifications {

    fun notifyNewTransaction(transaction: Transaction)
    fun notifyRemoveTransaction(transaction: Transaction)
}