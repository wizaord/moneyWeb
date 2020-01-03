package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.helpers.mapToJson
import com.wizaord.moneyweb.services.JwtService
import com.wizaord.moneyweb.services.UserService
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class AuthenticateControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean lateinit var jwtService: JwtService

    @MockBean
    lateinit var userService: UserService


    @Test
    fun `fun authenticate - when authenticate with a not know user then return 403`() {
        // given
        given(userService.getUserByUsernameAndPassword(anyString(), anyString())).willReturn(null)

        // when
        this.mockMvc.perform(post("/moneyapi/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(UserConnect("username", "password"))))
                .andExpect(status().isForbidden)
    }

    @Test
    fun `fun authenticate - when authenticate with a correct user then return a JSonToken`() {
        // given
        val uri = "/moneyapi/authenticate"
        val user = UserConnect("login", "password")

        given(userService.getUserByUsernameAndPassword(anyString(), anyString()))
                .willReturn(User("id", "login", "password", "email", "USER"))

        Mockito.`when`(jwtService.generateToken(anyString(), anyString())).thenReturn("MyJWTToken")

        // when
        this.mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(user)))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.token", `is`("MyJWTToken")))
    }

}