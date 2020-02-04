package com.wizaord.moneyweb.domain.transactions.ventilations

import kotlin.math.absoluteValue

class DebitVentilation(amount: Double): Ventilation(amount.absoluteValue * -1)