package com.wizaord.moneyweb.init

//import com.wizaord.moneyweb.services.CategoryService
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Component
//import java.io.File
//
//@Component
//class CategoryLoader(
//        @Autowired var categoryService: CategoryService
//) {
//
//    @Value("\${moneyweb.init.initdatabase.fileLocation.categoriesFamily}")
//    lateinit var categoriesFamilyFilePath: String
//
//    private val logger = LoggerFactory.getLogger(CategoryLoader::class.java)
//    private val categories: MutableList<CsvCategorie> = mutableListOf()
//
//    fun loadCategories() {
//        readCategories()
//
//        // extraction des categoriesFamily
//        val categoriesFamily = categories.filter { it.parentId.isNullOrEmpty() }
//                .map { CategoryFamily(it.name, mutableSetOf(), it.id) }
//                .toList()
//
//        // peuplement des sous categories
//        categories.filter { !it.parentId.isNullOrEmpty() }
//                .forEach { itSub ->
//                    val categoryFamily = categoriesFamily.first { it.id == itSub.parentId }
//                    categoryFamily.addSubCategory(SubCategory(itSub.name, itSub.id))
//                }
//
//        // replacement de tous les Ids
//        categoriesFamily.forEach { categoryService.createCategory(it) }
//        logger.info("All categories have been loaded")
//    }
//
//    fun readCategories() {
//        File(categoriesFamilyFilePath).forEachLine {
//            val splitStr = it.split(";")
//            categories.add(CsvCategorie(splitStr[0], splitStr[1], splitStr[3], splitStr[2]))
//        }
//    }
//}
//
//data class CsvCategorie(
//        var id: String,
//        var name: String,
//        var type: String,
//        var parentId: String? = null
//)
