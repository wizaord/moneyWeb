package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.Account
import com.wizaord.moneyweb.domain.AccountRepository
import com.wizaord.moneyweb.domain.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
        @Autowired var accountRepository: AccountRepository,
        @Autowired var userRepository: UserRepository,
        @Autowired var userService: UserService
) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    fun create(account: Account): Account {
        val currentUser = userService.getCurrentUser()

        val newAccount = accountRepository.save(account)
        logger.info("Nouveau compte créé avec l'ID {}", newAccount.id)

        account.owners.forEach {
            currentUser.getOwner(it.name).addAccount(newAccount.id!!)
        }
        userRepository.save(currentUser)

        return newAccount
    }

    fun getAllUserAccounts(): List<Account> {
        val currentUser = this.userService.getCurrentUser()

        val accountsIds = currentUser.owners
                .asSequence()
                .map { it.accounts }
                .flatten()
                .distinct()
        return accountsIds
                .map {
                    logger.info("Recuperation du compte avec id {} pour le user courant", it)
                    this.accountRepository.findById(it).get() }
                .toList()
    }

}