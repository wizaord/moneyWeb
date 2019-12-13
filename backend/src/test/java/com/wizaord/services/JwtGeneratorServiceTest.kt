package com.wizaord.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JwtGeneratorServiceTest {

    @InjectMocks
    lateinit var jwtGeneratorService: JwtGeneratorService

    @BeforeEach
    fun initMock() {
        jwtGeneratorService.privateKeyFilePath = "/samplePrivateKey.pem"
        jwtGeneratorService.iss = "https://moneyWeb.com"
    }

    @Test
    fun `with my jwt service, I want to generate a JWT Token with a username`() {
        // given
        val username = "username"

        // when
        val jwtToken = jwtGeneratorService.generateToken(username)

        //then
        println(jwtToken)
        assertThat(jwtToken).isNotBlank()
    }
}