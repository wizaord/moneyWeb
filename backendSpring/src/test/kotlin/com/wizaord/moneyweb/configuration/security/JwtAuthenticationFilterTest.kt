package com.wizaord.moneyweb.configuration.security

import com.wizaord.moneyweb.services.JwtService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder


@ExtendWith(MockitoExtension::class)
internal class JwtAuthenticationFilterTest {

    @Mock
    lateinit var jwtServiceMock: JwtService

    @InjectMocks
    lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Spy
    lateinit var jwtService: JwtService

    @BeforeEach
    fun initService() {
        jwtService.secretKey = "BalIky73YVVSAvJc6DqO3BJQlXNVf2QVkpE+Y7ft/Z0c5XGsN++jX6/HbkH7zwEasCThmSdQkqVoHmaN3ucuxg=="
    }

    @Test
    internal fun `when httprequest contain jwt token then return jwt`() {
        // given
        val dummyRequest = MockHttpServletRequest()
        dummyRequest.addHeader("Authorization", "Bearer plopplop")

        // when
        val jwtFromRequest = jwtAuthenticationFilter.getJwtFromRequest(dummyRequest)

        // then
        Assertions.assertThat(jwtFromRequest).isNotNull().isNotEmpty().isNotBlank().isEqualTo("plopplop")
    }

    @Test
    internal fun `when httprequest does not contain jwt header then return null`() {
        assertThat(jwtAuthenticationFilter.getJwtFromRequest(MockHttpServletRequest())).isNull()
    }

    @Test
    internal fun `when receive request without jwt token then do nothing`() {
        // given
        val dummyRequest = MockHttpServletRequest()
        val dummyResponse = MockHttpServletResponse()
        val dummyFilterChain = MockFilterChain()

        val securityContext: SecurityContext = Mockito.mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)

        // when
        jwtAuthenticationFilter.doFilter(dummyRequest, dummyResponse, dummyFilterChain)

        // then
        Mockito.verifyNoInteractions(securityContext)

    }

    @Test
    internal fun `when receive request with a valid token then user is registered in security context`() {
        val token = jwtService.generateToken("me")
        val dummyRequest = MockHttpServletRequest()
        dummyRequest.addHeader("Authorization", "Bearer $token")
        val dummyResponse = MockHttpServletResponse()
        val dummyFilterChain = MockFilterChain()

        val securityContext: SecurityContext = Mockito.mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)

        // when
        jwtAuthenticationFilter.doFilter(dummyRequest, dummyResponse, dummyFilterChain)

        // then
        Mockito.verify(securityContext).authentication = ArgumentMatchers.any(UsernamePasswordAuthenticationToken::class.java)
    }

    @Test
    internal fun `when receive request with a not valid token then do nothing`() {
        val token = jwtService.generateToken("me")
        val dummyRequest = MockHttpServletRequest()
        dummyRequest.addHeader("Authorization", "Bearer s$token")
        val dummyResponse = MockHttpServletResponse()
        val dummyFilterChain = MockFilterChain()

        val securityContext: SecurityContext = Mockito.mock(SecurityContext::class.java)
        SecurityContextHolder.setContext(securityContext)

        // when
        jwtAuthenticationFilter.doFilter(dummyRequest, dummyResponse, dummyFilterChain)

        // then
        Mockito.verifyNoInteractions(securityContext)
    }
}