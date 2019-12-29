package com.wizaord.moneyweb.controllers

import com.wizaord.moneyweb.helpers.mapToJson
import com.wizaord.moneyweb.services.UserService
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [User::class])
internal class UserTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var userService: UserService

    @Test
    internal fun `when create a user the return the user`() {
        //given
        val userAccount = UserAccount("login", "password", "email", listOf(AccountOwner("me")))
        given(userService.createUser(anyString(), anyString(), anyString()))
                .willReturn(true)

        // when
        this.mockMvc.perform(post("/moneyapi/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(userAccount)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.login", Matchers.`is`(userAccount.login)))

        // then
        Mockito.verify(userService).createUser(anyString(), anyString(), anyString())
    }

    @Test
    internal fun `when receive a user not valid then return 406`() {
        //given
        val userAccountNotValid = listOf(UserAccount("", "password", "email", listOf(AccountOwner("me"))),
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
                .willReturn(false)

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