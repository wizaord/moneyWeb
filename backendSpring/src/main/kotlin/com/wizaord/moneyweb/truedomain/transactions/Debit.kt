package com.wizaord.moneyweb.truedomain.transactions

import kotlin.math.absoluteValue

class Debit(amount: Double) : Transaction(amount.absoluteValue * -1)
