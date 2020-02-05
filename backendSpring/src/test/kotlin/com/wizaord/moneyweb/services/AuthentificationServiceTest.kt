package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.infrastructure.domain.User
import com.wizaord.moneyweb.infrastructure.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AuthentificationServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userAuthentificationService: AuthentificationService

    @Test
    fun `when getUsernameAndPassword when user is not found then return null`() {
        // given
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(null)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found but password is incorrect then return null`() {
        // given
        val userMongo = User("id", "username", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password2")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found and password is correct then return User`() {
        // given
        val userMongo = User("id", "username", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNotNull
    }

    @Test
    internal fun `when create user already exist then return null`() {
        //given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userCreated = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(userCreated).isNull()
    }

    @Test
    internal fun `when create user already email exist then return null`() {
        //given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(null)
        given(userRepository.findByEmail(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userCreated = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(userCreated).isNull()
    }

    @Test
    internal fun `when create a user then return the user`() {
        // given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(null)
        given(userRepository.findByEmail(ArgumentMatchers.anyString())).willReturn(null)
        given(userRepository.save(ArgumentMatchers.any(User::class.java))).willReturn(userMongo)

        // when
        val createUser = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(createUser).isNotNull
        assertThat(createUser).isInstanceOf(User::class.java)
    }

}