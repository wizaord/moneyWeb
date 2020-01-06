package com.wizaord.moneyweb.services

import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JwtServiceTest {

    @InjectMocks
    lateinit var jwtService: JwtService

    @BeforeEach
    fun initService() {
        jwtService.secretKey = "BalIky73YVVSAvJc6DqO3BJQlXNVf2QVkpE+Y7ft/Z0c5XGsN++jX6/HbkH7zwEasCThmSdQkqVoHmaN3ucuxg=="
    }

    @Test
    fun `generateToken - when call generateToken with username then return generated token with USER role`() {
        // given

        // when
        val generateToken = jwtService.generateToken("id", "username")

        // then
        assertThat(generateToken).isNotEmpty()
        val jwtToken = Jwts.parser().setSigningKey(jwtService.secretKey)
                .parseClaimsJws(generateToken)
        assertThat(jwtToken).isNotNull
        assertThat(jwtToken.body.issuer).isEqualTo(JwtService.ISSUER)
        assertThat(jwtToken.body.subject).isEqualTo("username")
        assertThat(jwtToken.body.get("USERID")).isEqualTo("id")
    }

    @Test
    internal fun `isTokenValid - when token is valid then return true`() {
        val generateToken = jwtService.generateToken("id", "username")
        assertThat(jwtService.isTokenValid(generateToken)).isTrue()
    }

    @Test
    internal fun `isTokenValid - when token is not valid then return false`() {
        assertThat(jwtService.isTokenValid("youhouhou")).isFalse()
    }
}