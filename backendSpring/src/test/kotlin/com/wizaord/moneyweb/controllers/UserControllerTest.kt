package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.helpers.mapToJson
import com.wizaord.moneyweb.services.UserService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
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
    lateinit var userService: UserService

    @Test
    internal fun `when create a user the return the user`() {
        //given
        val userAccount = UserAccount("login", "password", "email", listOf(AccountOwner("me")))
        given(userService.createUser(anyString(), anyString(), anyString()))
                .willReturn(User("id", "login", "password", "email"))

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.login", Matchers.`is`(userAccount.login)))

        // then
        Mockito.verify(userService).createUser(anyString(), anyString(), anyString())
    }

    @Test
    internal fun `when create a user with owners then all owners are created`() {
        //given
        val userAccount = UserAccount("login", "password", "email",
                listOf(AccountOwner("login"), AccountOwner("login2")))
        given(userService.createUser(anyString(), anyString(), anyString()))
                .willReturn(User("id", "login", "password", "email"))

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isCreated)

        // then
        Mockito.verify(userService).createUser(anyString(), anyString(), anyString())
        Mockito.verify(userService, Mockito.times(2)).addOwner(anyString(), anyString())
    }

    @Test
    internal fun `when receive a user not valid then return 406`() {
        //given
        val userAccountNotValid = listOf(
                UserAccount("", "password", "email", listOf(AccountOwner("me"))),
                UserAccount("username", "password", "", listOf(AccountOwner("me"))),
                UserAccount("username", "", "email", listOf(AccountOwner("me"))),
                UserAccount("username", "password", "email"))


        // when
        userAccountNotValid.forEach {
            this.mockMvc.perform(post("/moneyapi/user/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapToJson(it)))
                    .andExpect(status().isNotAcceptable)
        }
    }

    @Test
    internal fun `when receive a user already exist then return 502`() {
        // given
        val userAccount = UserAccount("login", "password", "email", listOf(AccountOwner("me")))
        given(userService.createUser(anyString(), anyString(), anyString()))
                .willReturn(null)

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andExpect(status().isConflict)
    }

    @Test
    internal fun `when receive a user with two owners with the same name then return 406`() {
        val userAccountNotValid = UserAccount("login", "password", "email",
                listOf(AccountOwner("me"), AccountOwner("me2"), AccountOwner("me")))

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccountNotValid)))
                .andExpect(status().isNotAcceptable)

        // then
        Mockito.verifyNoInteractions(userService)
    }
}