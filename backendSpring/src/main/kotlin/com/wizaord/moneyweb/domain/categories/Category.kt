package com.wizaord.moneyweb.domain.categories

import java.util.*

open class Category(
        var name: String,
        var id: String? = UUID.randomUUID().toString()
)