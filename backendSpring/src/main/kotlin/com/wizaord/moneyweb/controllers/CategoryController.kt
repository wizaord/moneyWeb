package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/moneyapi/categories")
class CategoryController(
        @Autowired val categoryService: CategoryService
) {

    @GetMapping("")
    @ResponseBody
    fun getAll(): List<CategoryFamily> = categoryService.getAll()
}