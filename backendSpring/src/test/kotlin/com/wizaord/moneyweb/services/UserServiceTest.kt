package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.AccountOwner
import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.domain.UserAuthenticated
import com.wizaord.moneyweb.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var securityContext: SecurityContext

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

    @Test
    internal fun `when create user already exist then return null`() {
        //given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findByUsername(ArgumentMatchers.anyString())).willReturn(userMongo)

        // when
        val userCreated = userService.createUser("login", "password", "email")

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
        val userCreated = userService.createUser("login", "password", "email")

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
        val createUser = userService.createUser("login", "password", "email")

        // then
        assertThat(createUser).isNotNull
        assertThat(createUser).isInstanceOf(User::class.java)
    }

    @Test
    internal fun `when add owner then user is updated`() {
        // given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(userMongo))
        given(userRepository.save(ArgumentMatchers.any(User::class.java))).willAnswer { i -> i.getArgument(0) }

        // when
        val userWithOwner = userService.addOwner("id", "owner")

        // then
        assertThat(userWithOwner.owners).isNotEmpty
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User::class.java))
    }

    @Test
    internal fun `when add owner in a user with already this owner then do nothing`() {
        // given
        val userMongo = User("id", "login", "password", "email")
        given(userRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(userMongo))
        given(userRepository.save(ArgumentMatchers.any(User::class.java))).willAnswer { i -> i.getArgument(0) }

        // when
        val userWithOwner = userService.addOwner("id", "owner")
        val userWithOwner2 = userService.addOwner("id", "owner")

        // then
        assertThat(userWithOwner.owners).isNotEmpty
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User::class.java))
    }

    @Test
    internal fun `isKnowOwner - if owner is knew then return true`() {
        // given
        SecurityContextHolder.setContext(securityContext)
        val user = User("id", "username", "password", "email")
        user.owners.add(AccountOwner("owner"))
        given(userRepository.findById(ArgumentMatchers.eq("id")))
                .willReturn(Optional.of(user))
        given(securityContext.authentication).willReturn(UsernamePasswordAuthenticationToken(UserAuthenticated("id", "username"), ""))

        // when
        val knowOwner = userService.isKnowOwner("owner")

        // then
        assertThat(knowOwner).isTrue()
    }

    @Test
    internal fun `isKnowOwner - if owner is not knew then return false`() {
        // given
        SecurityContextHolder.setContext(securityContext)
        val user = User("id", "username", "password", "email")
        user.owners.add(AccountOwner("owner"))
        given(userRepository.findById(ArgumentMatchers.eq("id")))
                .willReturn(Optional.of(user))
        given(securityContext.authentication).willReturn(UsernamePasswordAuthenticationToken(UserAuthenticated("id", "username"), ""))

        // when
        val knowOwner = userService.isKnowOwner("owner2")

        // then
        assertThat(knowOwner).isFalse()
    }
}