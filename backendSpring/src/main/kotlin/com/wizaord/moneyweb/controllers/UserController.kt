package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.AuthentificationService
import com.wizaord.moneyweb.services.FamilyBankAccountsCreateService
import com.wizaord.moneyweb.services.FamilyBankAccountsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyapi/user")
class UserController(@Autowired val authentificationService: AuthentificationService,
                     @Autowired val familyBankAccountsCreateService: FamilyBankAccountsCreateService) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/create")
    @ResponseBody
    fun createUser(@RequestBody user: UserAccount): ResponseEntity<UserAccount> {
        logger.info("Receive create user request")
        if (!user.isValid()) {
            logger.info("User is not valid")
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        val createUser = this.authentificationService.createUser(user.login, user.password, user.email)
        if (createUser == null) {
            logger.info("User is already exist")
            return ResponseEntity(HttpStatus.CONFLICT)
        }

        familyBankAccountsCreateService.initFamily(user.login)

        logger.info("User successfully created")
        return ResponseEntity(user, HttpStatus.CREATED)
    }
}

data class UserAccount(
        var login: String,
        var password: String,
        var email: String
) {
    fun isValid(): Boolean {
        return when {
            login.isEmpty() -> false
            password.isEmpty() -> false
            email.isEmpty() -> false
            else -> true
        }
    }
}
