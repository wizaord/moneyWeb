package com.wizaord.moneyweb.domain.categories

import java.util.*

open class Category(
        open var name: String,
        open var id: String = UUID.randomUUID().toString()
)