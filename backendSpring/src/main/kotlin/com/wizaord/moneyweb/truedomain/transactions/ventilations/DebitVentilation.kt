package com.wizaord.moneyweb.truedomain.transactions.ventilations

import kotlin.math.absoluteValue

class DebitVentilation(amount: Double): Ventilation(amount.absoluteValue * -1)