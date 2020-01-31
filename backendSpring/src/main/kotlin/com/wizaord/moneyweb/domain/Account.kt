package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id
import java.util.*

data class Account(
        @Id var id: String?,
        var name: String,
        var bankName: String,
        var openDate: Date,
        val isOpened: Boolean = false,
        var owners: MutableSet<AccountOwner> = mutableSetOf()
        )