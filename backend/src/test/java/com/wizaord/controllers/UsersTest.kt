package com.wizaord.controllers

import com.wizaord.services.JwtGeneratorService
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
internal class UsersTest {

    @Inject
    lateinit var jwtService: JwtGeneratorService

    @Test
    fun `Access to all user with role user is forbidden`() {

        //given
        val jwtToken = "Bearer " + jwtService.generateToken("user", listOf("user"))

        //when
        given()
                .header("Authorization", jwtToken)
                .`when`()
                .get("moneyapi/users")
                .then()
                .statusCode(403)
    }

    @Test
    fun `Access to all user with role admin is allowed`() {
        //given
        val jwtToken = "Bearer " + jwtService.generateToken("user", listOf("admin"))

        //when
        given()
                .header("Authorization", jwtToken)
                .`when`()
                .get("moneyapi/users")
                .then()
                .statusCode(200)
    }
}