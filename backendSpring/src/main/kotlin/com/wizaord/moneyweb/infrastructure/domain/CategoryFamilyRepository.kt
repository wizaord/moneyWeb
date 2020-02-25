package com.wizaord.moneyweb.infrastructure.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryFamilyRepository : MongoRepository <CategoryFamily, String> {

}