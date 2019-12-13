package com.wizaord.controllers

import com.wizaord.Helpers.startMongoDatabase
import com.wizaord.Helpers.stopMongoDatabase
import com.wizaord.services.JwtGeneratorService
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.jboss.logging.Logger
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import javax.inject.Inject

@QuarkusTest
internal class UsersTest {

    companion object {
        @BeforeAll
        fun beforeAll() = startMongoDatabase()

        @AfterAll
        fun afterAll() = stopMongoDatabase()
    }

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
                .get("/users")
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
                .get("/users")
                .then()
                .statusCode(200)
    }
}