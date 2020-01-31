package com.wizaord.moneyweb.truedomain.transactions

import kotlin.math.absoluteValue

class Credit(amount: Double) : Transaction(amount.absoluteValue)
