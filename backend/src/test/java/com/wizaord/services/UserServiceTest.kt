package com.wizaord.services

import com.wizaord.domain.User
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.ArgumentMatchers.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing

@QuarkusTest
@ExtendWith(MockitoExtension::class)
internal class UserServiceTest {

    @AfterEach
    fun cleanDatabase() {
        User.deleteAll()
    }

    @InjectMocks
    @Spy
    lateinit var userRepository: UserService

    @Test
    fun `function getUser, if user is found by his name and password, return user`() {
        // given
        val username = "username"
        val password = "password"
        User(username, password, "mail").persist()

        // when
        val user = userRepository.getUser(username, password)

        // then
        user!!
        assertThat(user.username).isEqualTo(username)
        assertThat(user.password).isEqualTo(password)
        Mockito.verify(userRepository).getUser(anyString(), anyString())

    }

    @Test
    fun `function getUser, if user is not found by his name or password, return nothing`() {
        // given
        val username = "username"
        val password = "password"

        // when
        val user = userRepository.getUser(username, password)

        // then
        assertThat(user).isNull()

    }

    @Test
    fun `function getUser, if username is ok but password is not correct, return nothing`() {
        // given
        User("username", "password2", "mail").persist()

        // when
        val user = userRepository.getUser("username", "password")

        // then
        assertThat(user).isNull()
    }

}

private fun <T> OngoingStubbing<T>.thenReturn(user: User) {

}
