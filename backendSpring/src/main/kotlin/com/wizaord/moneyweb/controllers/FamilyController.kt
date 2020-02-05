package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import com.wizaord.moneyweb.services.FamilyBankAccountsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyapi/family")
class FamilyController(
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {


    @GetMapping("{familyName}/owners")
    @ResponseBody
    fun getOwners(@PathVariable familyName: String): List<Owner> {
        val familyService = familyBankAccountServiceFactory.getServiceBeanForFamily(familyName)
        return familyService.getOwners().map { Owner(it.username) }
    }


    data class Owner(val name: String)

}