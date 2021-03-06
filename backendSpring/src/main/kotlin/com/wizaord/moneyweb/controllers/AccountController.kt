package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.configuration.toLocalDate
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.BankAccountOwners
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/family/{familyName}/accounts")
class AccountController(
        @Autowired private val familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
) {
    @PostMapping("")
    fun create(@PathVariable familyName: String, @RequestBody account: AccountCreate) {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        familyService.accountRegister(account.accountName, account.bankName, account.dateCreate.toLocalDate(), account.owners)
    }

    @RequestMapping("/sortByLastTransaction")
    @ResponseBody
    fun accountsSorted(@PathVariable familyName: String): List<Account> {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithTransactions(familyName)
        return familyService.bankAccountsSortByLastTransactions().map { Account.fromDomain(it) }
    }

    @RequestMapping("")
    @ResponseBody
    fun accounts(@PathVariable familyName: String): List<Account> {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        return familyService.bankAccounts().map { Account.fromDomain(it) }
    }

    @PatchMapping("/{accountName}/close")
    fun accountClose(@PathVariable familyName: String,
                     @PathVariable accountName: String) {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        familyService.accountClose(accountName)
    }

    @PatchMapping("/{accountName}/open")
    fun accountOpen(@PathVariable familyName: String,
                    @PathVariable accountName: String) {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        familyService.accountOpen(accountName)
    }

    @PatchMapping("/{accountName}/solde")
    fun accountRefreshSolde(@PathVariable familyName: String,
                    @PathVariable accountName: String) {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithTransactions(familyName)
        familyService.refreshSolde(accountName)
    }

    @DeleteMapping("/{accountName}")
    fun accountDelete(@PathVariable familyName: String,
                      @PathVariable accountName: String) {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        familyService.accountDelete(accountName)
    }

    @PutMapping("/{accountName}")
    @ResponseBody
    fun accountUpdate(@PathVariable familyName: String,
                      @PathVariable accountName: String,
                      @RequestBody accountToUpdate: Account): Account {
        val familyService = familyBankAccountServiceFactory.getFamilyServiceWithoutTransactions(familyName)
        familyService.accountUpdateBankName(accountName, accountToUpdate.bankName)
        familyService.accountUpdateDateCreation(accountName, accountToUpdate.dateCreate.toLocalDate())
        familyService.accountUpdateOwners(accountName, accountToUpdate.owners.map { FamilyMember(it) })
        familyService.accountUpdateName(accountName, accountToUpdate.accountName)
        return Account.fromDomain(familyService.bankAccount(accountToUpdate.accountName)!!)
    }

}


data class AccountCreate(var accountName: String,
                         var bankName: String,
                         var dateCreate: Date,
                         var owners: List<String>) {

}

data class Account(var accountName: String,
                   var bankName: String,
                   var dateCreate: Date,
                   var isOpened: Boolean,
                   var solde: Double,
                   var owners: List<String>) {


    companion object {
        fun fromDomain(bao: BankAccountOwners): Account {
            val bankAccount = bao.bankAccount as BankAccountImpl
            return Account(bankAccount.getName(),
                    bankAccount.getBankName(),
                    bankAccount.dateCreation.toDate(),
                    bankAccount.isOpen,
                    bankAccount.solde,
                    bao.getOwners().map { it.username })
        }
    }
}





