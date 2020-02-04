package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.infrastructure.User
import com.wizaord.moneyweb.helpers.mapToJson
import com.wizaord.moneyweb.services.AuthentificationService
import com.wizaord.moneyweb.services.FamilyBankAccountsCreateService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class UserControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var authenticationService: AuthentificationService
    @MockBean
    lateinit var familyBankAccountsCreateService: FamilyBankAccountsCreateService

    @Test
    internal fun `when create a user the return the user and create a family`() {
        //given
        val userAccount = UserAccount("login", "password", "email")
        given(authenticationService.createUser(anyString(), anyString(), anyString()))
                .willReturn(User("id", "login", "password", "email"))

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.login", Matchers.`is`(userAccount.login)))

        // then
        verify(authenticationService).createUser(anyString(), anyString(), anyString())
        verify(familyBankAccountsCreateService).initFamily(anyString())
    }

    @ParameterizedTest
    @CsvSource(", pass, email",
            "username, password, ",
            "username, , email")
    internal fun `when receive a user not valid then return 406`(login: String?, password: String?, email: String?) {
        //given
        val userAccount = UserAccount(login.orEmpty(), password.orEmpty(), email.orEmpty())


        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isNotAcceptable)
    }

    @Test
    internal fun `when receive a user already exist then return 502`() {
        // given
        val userAccount = UserAccount("login", "password", "email")
        given(authenticationService.createUser(anyString(), anyString(), anyString()))
                .willReturn(null)

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isConflict)
    }
}