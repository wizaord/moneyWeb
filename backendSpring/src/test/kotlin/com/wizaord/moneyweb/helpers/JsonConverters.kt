package com.wizaord.moneyweb.helpers

import com.fasterxml.jackson.databind.ObjectMapper


fun mapToJson(obj: Any?): String {
    return ObjectMapper().writeValueAsString(obj)
}

fun <T> mapFromJson(json: String?, clazz: Class<T>?): T {
    return ObjectMapper().readValue(json, clazz)
}