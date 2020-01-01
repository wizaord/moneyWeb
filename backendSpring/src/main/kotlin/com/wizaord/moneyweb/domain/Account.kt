package com.wizaord.moneyweb.domain

import org.springframework.data.annotation.Id
import java.util.*

data class Account(
        @Id var id: String?,
        var name: String,
        var openDate: Date
        )