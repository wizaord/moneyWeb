package com.wizaord.moneyweb.truedomain

import com.wizaord.moneyweb.truedomain.transactions.Transaction

interface InfrastructureBankAccountNotifications {

    fun notifyNewTransaction(transaction: Transaction)
    fun notifyRemoveTransaction(transaction: Transaction)
}