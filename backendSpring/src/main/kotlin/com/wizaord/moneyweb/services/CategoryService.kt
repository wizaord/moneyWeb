package com.wizaord.moneyweb.services
//
//import com.wizaord.moneyweb.infrastructure.CategoryFamily
//import com.wizaord.moneyweb.infrastructure.CategoryFamilyRepository
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//
//@Service
//@Transactional
//class CategoryService(
//        @Autowired var categoryFamilyRepository: CategoryFamilyRepository
//) {
//    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)
//
//    fun createCategory(categoryFamily: CategoryFamily): CategoryFamily {
//        return this.categoryFamilyRepository.save(categoryFamily)
//    }
//
//    fun getAll(): List<CategoryFamily> {
//        return this.categoryFamilyRepository.findAll()
//    }
//}