package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.CategoryFamily
import com.wizaord.moneyweb.domain.SubCategory
import com.wizaord.moneyweb.services.CategoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
class CategoryLoader(
        @Autowired var categoryService: CategoryService
) {

    private val logger = LoggerFactory.getLogger(CategoryLoader::class.java)

    fun loadCategories(categoryRecords: MutableList<CsvCategorie>) {
        // extraction des categoriesFamily
        val categoriesFamily = categoryRecords.filter { it.parentId.isNullOrEmpty() }
                .map { CategoryFamily(it.name, mutableSetOf(), it.id) }
                .toList()

        // peuplement des sous categories
        categoryRecords.filter { !it.parentId.isNullOrEmpty() }
                .forEach { itSub ->
                    val categoryFamily = categoriesFamily.first { it.id == itSub.parentId }
                    categoryFamily.addSubCategory(SubCategory(itSub.name, itSub.id))
                }

        // replacement de tous les Ids
        categoriesFamily.forEach { categoryService.createCategory(it) }
        logger.info("All categories have been loaded")
    }

    fun readCategories(): MutableList<CsvCategorie> {
        val records: MutableList<CsvCategorie> = mutableListOf()
        File(javaClass.classLoader.getResource("init" + File.separator + "categories.csv").file).forEachLine {
            val splitStr = it.split(";")
            records.add(CsvCategorie(splitStr[0], splitStr[1], splitStr[3], splitStr[2]))
        }
        return records
    }
}

data class CsvCategorie(
        var id: String,
        var name: String,
        var type: String,
        var parentId: String? = null
)
