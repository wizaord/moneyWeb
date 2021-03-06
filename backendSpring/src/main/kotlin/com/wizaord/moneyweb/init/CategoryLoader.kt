package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.domain.categories.Category
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.services.CategoryService
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class CategoryLoader(
        @Autowired val categoryService: CategoryService,
        @Autowired val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {

    @Value("\${moneyweb.init.initdatabase.fileLocation.categoriesFamily}")
    lateinit var categoriesFamilyFilePath: String

    private val logger = LoggerFactory.getLogger(CategoryLoader::class.java)
    private val categories: MutableList<CsvCategorie> = mutableListOf()

    fun loadCategories() {
        readCategories()

        // extraction des categoriesFamily
        val categoriesFamily = categories.filter { it.parentId.isNullOrEmpty() }
                .map { CategoryFamily(it.name, it.id) }
                .toList()

        // peuplement des sous categories
        categories.filter { !it.parentId.isNullOrEmpty() }
                .forEach { itSub ->
                    val categoryFamily = categoriesFamily.first { it.id == itSub.parentId }
                    categoryFamily.addSubCategory(Category(itSub.name, itSub.id))
                }

        // replacement de tous les Ids
        categoriesFamily.forEach { categoryService.createCategory(it) }
        // creation d'une dernière category VIREMENT INTERNE
        createInternalVirements()
        logger.info("All categories have been loaded")
    }

    private fun createInternalVirements() {
        val categoryFamily = CategoryFamily("VIREMENT INTERNE", CategoryFamily.VIREMENT_INTERNE_ID)
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions("mouilleron")
        familyService.bankAccounts()
                .forEach { bao -> categoryFamily.addSubCategory(Category("VIREMENT - ${bao.bankAccount.getName()}", bao.bankAccount.getInternalId())) }

        categoryService.createCategory(categoryFamily)
    }

    fun readCategories() {
        File(categoriesFamilyFilePath).forEachLine {
            val splitStr = it.split(";")
            categories.add(CsvCategorie(splitStr[0], splitStr[1], splitStr[3], splitStr[2]))
        }
    }
}

data class CsvCategorie(
        var id: String,
        var name: String,
        var type: String,
        var parentId: String? = null
)
