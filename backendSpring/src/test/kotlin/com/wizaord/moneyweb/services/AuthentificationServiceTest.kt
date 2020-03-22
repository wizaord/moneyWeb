package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.same
import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.infrastructure.UserPersistence
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
    lateinit var userPersistence: UserPersistence

    @InjectMocks
    lateinit var userAuthentificationService: AuthentificationService

    @Test
    fun `when getUsernameAndPassword when user is not found then return null`() {
        // given
        given(userPersistence.findByUsername(ArgumentMatchers.anyString())).willReturn(null)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found but password is incorrect then return null`() {
        // given
        val userMongo = User("id", "username", "password", "email")
        given(userPersistence.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password2")

        // then
        assertThat(userFromBdd).isNull()
    }

    @Test
    fun `when getUsernameAndPassword when user is found and password is correct then return User`() {
        // given
        val userMongo = User( "username", "password", "email", "id")
        given(userPersistence.findByUsername(same("username"))).willReturn(userMongo)

        // when
        val userFromBdd = userAuthentificationService.getUserByUsernameAndPassword("username", "password")

        // then
        assertThat(userFromBdd).isNotNull
    }

    @Test
    internal fun `when create user already exist then return null`() {
        //given
        val userMongo = User("id", "login", "password", "email")
        given(userPersistence.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userCreated = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(userCreated).isNull()
    }

    @Test
    internal fun `when create user already email exist then return null`() {
        //given
        val userMongo = User("id", "login", "password", "email")
        given(userPersistence.findByUsername(ArgumentMatchers.anyString())).willReturn(null)
        given(userPersistence.findByEmail(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userCreated = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(userCreated).isNull()
    }

    @Test
    internal fun `when create a user then return the user`() {
        // given
        given(userPersistence.findByUsername(ArgumentMatchers.anyString())).willReturn(null)
        given(userPersistence.findByEmail(ArgumentMatchers.anyString())).willReturn(null)
        given(userPersistence.save(anyOrNull())).willAnswer { it.arguments[0] }

        // when
        val createUser = userAuthentificationService.createUser("login", "password", "email")

        // then
        assertThat(createUser).isNotNull
        assertThat(createUser).isInstanceOf(User::class.java)
    }

}