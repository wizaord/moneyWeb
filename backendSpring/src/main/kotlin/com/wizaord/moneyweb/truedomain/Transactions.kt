package com.wizaord.moneyweb.truedomain

import java.util.*
import kotlin.math.absoluteValue

abstract class Transaction(
        var amount: Double,
        var isPointe: Boolean = false,
        val id: String = UUID.randomUUID().toString()
) {
    fun point() {
        this.isPointe = true
    }
    fun unpoint() {
        this.isPointe = false
    }
}

class Debit(amount: Double) : Transaction(amount.absoluteValue * -1)
class Credit(amount: Double) : Transaction(amount.absoluteValue)
