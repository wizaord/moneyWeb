package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/moneyapi/user")
class User(@Autowired var userService: UserService) {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    @PostMapping("/create")
    @ResponseBody
    fun createUser(@RequestBody user: UserAccount): ResponseEntity<UserAccount> {
        logger.info("Receive create user request")
        if (!user.isValid()) {
            logger.info("User is not valid")
            return ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if(!this.userService.createUser(user.login, user.password, user.email)) {
            logger.info("User is already exist")
            return ResponseEntity(HttpStatus.CONFLICT)
        }

        logger.info("User successfully created")
        return ResponseEntity(user, HttpStatus.CREATED)
    }

}

data class UserAccount(
        var login: String,
        var password: String,
        var email: String,
        var users: List<Username> = listOf()
) {
    fun isValid(): Boolean {
        return when {
            login.isEmpty() -> false
            password.isEmpty() -> false
            email.isEmpty() -> false
            users.isEmpty() -> false
            users.stream().filter { !it.isValid() }.count() > 0L -> false
            else -> true
        }
    }
}


data class Username(var username: String) {
    fun isValid(): Boolean {
        return username.isNotEmpty()
    }
}
