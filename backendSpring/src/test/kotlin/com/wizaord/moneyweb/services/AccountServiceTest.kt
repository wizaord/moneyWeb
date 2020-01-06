package com.wizaord.moneyweb.services

import com.wizaord.moneyweb.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.BDDMockito.eq
import org.mockito.BDDMockito.given
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
    fun `when create an account, then account is created and relies to all users`() {

        // given
        val user = User("id", "username", "pass", "email")
        user.addOwner("owner1")
        user.addOwner("owner2")

        val accountInput = Account(null, "accountName", "bank", Date(), user.owners)
        val accountOutput = Account("id", "accountName", "bank", Date(), user.owners)

        given(userService.getCurrentUser()).willReturn(user)
        given(accountRepository.save(ArgumentMatchers.any(Account::class.java))).willReturn(accountOutput)

        // when
        val accountCreated = accountService.create(accountInput)

        // then
        assertThat(accountCreated).isNotNull
        assertThat(accountCreated.id).isNotNull()
        assertThat(accountCreated.name).isEqualTo("accountName")
        assertThat(accountCreated.openDate).isNotNull()
        verify(userService).getCurrentUser()

        val userArgumentCaptor = ArgumentCaptor.forClass(User::class.java)
        verify(userRepository).save(userArgumentCaptor.capture())
        val userUpdated = userArgumentCaptor.value
        assertThat(userUpdated).isNotNull
        val nbOwnerUpdated = userUpdated.owners.filter { it.accounts.isNotEmpty() }.count()
        assertThat(nbOwnerUpdated).isEqualTo(2)
    }

    @Test
    fun getAllUserAccounts() {
        // given
        val accountOwner = AccountOwner("me")
        accountOwner.addAccount("id")
        accountOwner.addAccount("id2")
        val accountOwner2 = AccountOwner("me2")
        accountOwner2.addAccount("id2")
        accountOwner2.addAccount("id3")
        val user = User("id", "name", "password", "email")
        user.owners.add(accountOwner)
        user.owners.add(accountOwner2)

        val account1 = Account("id", "name", "bank", Date(), setOf(accountOwner))
        val account2 = Account("id2", "name2", "bank", Date(), setOf(accountOwner))
        val account3 = Account("id3", "name2", "bank", Date(), setOf(accountOwner, accountOwner2))

        given(this.userService.getCurrentUser()).willReturn(user)
        given(this.accountRepository.findById(eq("id"))).willReturn(Optional.of(account1))
        given(this.accountRepository.findById(eq("id2"))).willReturn(Optional.of(account2))
        given(this.accountRepository.findById(eq("id3"))).willReturn(Optional.of(account3))

        // when
        val allAccounts = this.accountService.getAllUserAccounts()

        // then
        assertThat(allAccounts).isNotNull.hasSize(3)
        verify(this.accountRepository, Mockito.times(3)).findById(ArgumentMatchers.anyString())
    }
}