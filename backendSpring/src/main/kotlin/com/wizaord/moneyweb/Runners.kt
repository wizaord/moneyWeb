package com.wizaord.moneyweb

import com.wizaord.moneyweb.domain.CategoryFamily
import com.wizaord.moneyweb.domain.CategoryFamilyRepository
import com.wizaord.moneyweb.domain.SubCategory
import com.wizaord.moneyweb.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.util.*


@Component
@Profile("init-categorie")
class RunnerLoaderCategories (
        @Autowired var categoryService: CategoryService
): CommandLineRunner {

    override fun run(vararg args: String?) {
        val records: MutableList<CsvCategorie> = ArrayList()
        File(javaClass.classLoader.getResource("init" + File.separator + "categories.csv").file).forEachLine {
           val splitStr = it.split(";")
            records.add(CsvCategorie(splitStr[0], splitStr[1], splitStr[3], splitStr[2]))
        }

        // extraction des categoriesFamily
        val categoriesFamily = records.filter { it.parentId.isNullOrEmpty() }
                .map { CategoryFamily(it.name, mutableSetOf(), it.id) }
                .toList()

        // peuplement des sous categories
        records.filter { ! it.parentId.isNullOrEmpty() }
                .forEach { itSub ->
                    val categoryFamily = categoriesFamily.first { it.id == itSub.parentId }
                    categoryFamily.addSubCategory(SubCategory(itSub.name))
                }

        // replacement de tous les Ids
        categoriesFamily.map { it.id == UUID.randomUUID().toString() }
        categoriesFamily.forEach{ categoryService.createCategory(it)}
    }
}

data class CsvCategorie(
        var id: String,
        var name: String,
        var type: String,
        var parentId: String? = null
)