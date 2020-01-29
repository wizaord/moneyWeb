package com.wizaord.moneyweb.truedomain

import kotlin.math.absoluteValue

open class Transaction(
        val amount: Double
)

class Debit(amount: Double) : Transaction(amount.absoluteValue * -1)
class Credit(amount: Double) : Transaction(amount.absoluteValue)
