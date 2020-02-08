package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.springframework.beans.factory.annotation.Autowired
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
        return familyService.owners().map { Owner(it.username) }
    }

    @PostMapping("{familyName}/owners")
    @ResponseBody
    fun createOwner(@PathVariable familyName: String, @RequestBody owner: Owner): Owner {
        val familyService = familyBankAccountServiceFactory.getServiceBeanForFamily(familyName)
        return Owner.fromDomain(familyService.ownerCreate(owner.toDomain()))
    }

}

data class Owner(val name: String) {
    companion object {
        fun fromDomain(familyMember: FamilyMember) = Owner(familyMember.username)
    }
    fun toDomain() = FamilyMember(name)
}
