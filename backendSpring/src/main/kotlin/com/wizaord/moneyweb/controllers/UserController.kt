package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/moneyapi/user")
class UserController(@Autowired var userService: UserService) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/create")
    @ResponseBody
    fun createUser(@RequestBody user: UserAccount): ResponseEntity<UserAccount> {
        logger.info("Receive create user request")
        if (!user.isValid()) {
            logger.info("User is not valid")
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        val createUser = this.userService.createUser(user.login, user.password, user.email)
        if (createUser == null) {
            logger.info("User is already exist")
            return ResponseEntity(HttpStatus.CONFLICT)
        }

        user.owners.forEach { owner: AccountOwner -> this.userService.addOwner(createUser.id!!, owner.ownerName) }

        logger.info("User successfully created")
        return ResponseEntity(user, HttpStatus.CREATED)
    }

}

data class UserAccount(
        var login: String,
        var password: String,
        var email: String,
        var owners: List<AccountOwner> = listOf()
) {
    fun isValid(): Boolean {
        return when {
            login.isEmpty() -> false
            password.isEmpty() -> false
            email.isEmpty() -> false
            owners.isEmpty() -> false
            owners.stream().filter { !it.isValid() }.count() > 0L -> false
            owners.stream().filter { Collections.frequency(owners, it) > 1 }.count() > 0L -> false  // detection des doublons
            else -> true
        }
    }
}


data class AccountOwner(var ownerName: String) {
    fun isValid(): Boolean {
        return ownerName.isNotEmpty()
    }
}
