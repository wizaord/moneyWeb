package com.wizaord.controllers

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import io.quarkus.test.junit.QuarkusTest
import de.flapdoodle.embed.process.runtime.Network;
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.jboss.logging.Logger
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.IOException
import javax.ws.rs.core.MediaType


@QuarkusTest
internal class AuthenticateTest {

    companion object {
        private val LOGGER: Logger = Logger.getLogger(AuthenticateTest::class.java)
        private lateinit var MONGO: MongodExecutable

        @BeforeAll
        @Throws(IOException::class)
        @JvmStatic
        fun startMongoDatabase() {
            val version: Version.Main = Version.Main.V4_0
            val port = 27018
            LOGGER.infof("Starting Mongo %s on port %s", version, port)
            val config: IMongodConfig = MongodConfigBuilder()
                    .version(version)
                    .net(Net(port, Network.localhostIsIPv6()))
                    .build()
            MONGO = MongodStarter.getDefaultInstance().prepare(config)
            MONGO.start()
        }

        @AfterAll
        @JvmStatic
        fun stopMongoDatabase() {
            MONGO.stop()
        }
    }


    @Test
    fun `when call authenticate endpoint then return user object with token`() {

        given()
                .`when`()
                    .body(UserConnect("login", "password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .post("/authenticate")
                .then()
                    .statusCode(200)
                    .body("token", notNullValue())
    }
}