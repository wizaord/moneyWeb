package com.wizaord.moneyweb.controllers
//
//import com.nhaarman.mockitokotlin2.anyOrNull
//import com.wizaord.moneyweb.infrastructure.Account
//import com.wizaord.moneyweb.infrastructure.AccountOwner
//import com.wizaord.moneyweb.helpers.mapToJson
//import com.wizaord.moneyweb.services.AccountService
//import com.wizaord.moneyweb.services.UserService
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.ArgumentMatchers
//import org.mockito.BDDMockito.*
//import org.mockito.Mockito.times
//import org.mockito.junit.jupiter.MockitoExtension
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import java.util.*
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension::class)
//internal class AccountControllerTest(
//        @Autowired val mockMvc: MockMvc) {
//
//    @MockBean
//    lateinit var userService: UserService
//    @MockBean
//    lateinit var accountService: AccountService
//
//    @Test
//    internal fun `create - when owners is not know then return not acceptable`() {
//        // given
//        val accountCreate = AccountCreate("accountName", "bank", Date(), listOf("owner", "owner2"))
//        given(userService.isKnowOwner(ArgumentMatchers.anyString())).willReturn(true)
//        given(userService.isKnowOwner(ArgumentMatchers.anyString())).willReturn(false)
//
//        // when
//        this.mockMvc.perform(post("/moneyapi/accounts/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(mapToJson(accountCreate)))
//                .andExpect(MockMvcResultMatchers.status().isNotAcceptable)
//
//        // then
//        verify(userService, times(2)).isKnowOwner(anyString())
//    }
//
//    @Test
//    internal fun `create - when owners is know then call service create account`() {
//        // given
//        val accountCreate = AccountCreate("accountName", "bank", Date(), listOf("owner"))
//        val accountCreated = Account("id", accountCreate.accountName, "bank", accountCreate.dateCreate, true, mutableSetOf(AccountOwner("owner")))
//        given(userService.isKnowOwner(ArgumentMatchers.anyString())).willReturn(true)
//        given(accountService.create(anyOrNull()))
//                .willReturn(accountCreated)
//
//        // when
//        this.mockMvc.perform(post("/moneyapi/accounts/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(mapToJson(accountCreate)))
//                .andExpect(MockMvcResultMatchers.status().isCreated)
//
//        // then
//        verify(userService).isKnowOwner(anyString())
//        verify(accountService).create(anyOrNull())
//    }
//
//}

