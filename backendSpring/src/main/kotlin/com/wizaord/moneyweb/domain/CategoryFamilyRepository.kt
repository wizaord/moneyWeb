package com.wizaord.moneyweb.domain

import org.springframework.data.mongodb.repository.MongoRepository

interface CategoryFamilyRepository : MongoRepository<CategoryFamily, String> {

}
