package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun `when getUsernameAndPassword when user is not found then return null`() {
        // given
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(null)

        // when
        val userFromBdd = userService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found but password is incorrect then return null`() {
        // given
        val userMongo = User("id", "username", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userFromBdd = userService.getUserByUsernameAndPassword("username", "password2")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found and password is correct then return User`() {
        // given
        val userMongo = User("id", "username", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userFromBdd = userService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNotNull
    }
}