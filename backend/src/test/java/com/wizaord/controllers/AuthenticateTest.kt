package com.wizaord.controllers

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType


@QuarkusTest
internal class AuthenticateTest {

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