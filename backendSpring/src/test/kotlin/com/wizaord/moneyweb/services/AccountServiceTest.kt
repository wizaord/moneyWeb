package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.Account
import com.wizaord.moneyweb.domain.AccountRepository
import com.wizaord.moneyweb.domain.User
import com.wizaord.moneyweb.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class AccountServiceTest {

    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var accountRepository: AccountRepository
    @Mock
    lateinit var userService: UserService

    @InjectMocks
    lateinit var accountService: AccountService

    @Test
    fun `when create an account, then account is created and relies to main user`() {

        // given
        val accountInput = Account(null, "accountName", Date())
        val accountOutput = Account("id", "accountName", Date())
        val owner = "owner"
        val user = User("id", "username", "pass", "email")
        user.addOwner(owner)

        given(userService.getCurrentUser()).willReturn(user)
        given(accountRepository.save(ArgumentMatchers.any(Account::class.java))).willReturn(accountOutput)

        // when
        val accountCreated = accountService.create(accountInput, owner)

        // then
        assertThat(accountCreated).isNotNull
        assertThat(accountCreated.id).isNotNull()
        assertThat(accountCreated.name).isEqualTo("accountName")
        assertThat(accountCreated.openDate).isNotNull()
        verify(userService).getCurrentUser()

        val userArgumentCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userArgumentCaptor.capture())
        val userUpdated = userArgumentCaptor.getValue()
        assertThat(userUpdated).isNotNull
        val accountOwner = userUpdated.owners.first { it.name == "owner" }
        assertThat(accountOwner).isNotNull
        assertThat(accountOwner.accounts).isNotNull
        assertThat(accountOwner.accounts).hasSize(1)
        assertThat(accountOwner.accounts.elementAt(0)).isEqualTo("id")

    }
}