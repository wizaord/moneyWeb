package com.wizaord.controllers

import com.wizaord.Helpers.startMongoDatabase
import com.wizaord.Helpers.stopMongoDatabase
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType


@QuarkusTest
internal class AuthenticateTest {

    companion object {
        @BeforeAll
        fun beforeAll() = startMongoDatabase()

        @AfterAll
        fun afterAll() = stopMongoDatabase()
    }


    @Test
    fun `when call authenticate endpoint then return user object with token`() {
        given()
                .`when`()
                .body(UserConnect("login", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .post("moneyapi/authenticate")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
    }
}