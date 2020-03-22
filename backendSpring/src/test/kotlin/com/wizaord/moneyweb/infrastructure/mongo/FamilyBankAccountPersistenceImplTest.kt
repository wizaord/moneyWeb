package com.wizaord.moneyweb.infrastructure.mongo

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.infrastructure.mongo.domain.BankAccount
import com.wizaord.moneyweb.infrastructure.mongo.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.mongo.domain.FamilyBankAccountsRepository
import com.wizaord.moneyweb.infrastructure.mongo.domain.TransactionsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountPersistenceImplTest {

    @Mock
    lateinit var familyBankAccountsRepository: FamilyBankAccountsRepository

    @Mock
    lateinit var transactionsRepository: TransactionsRepository

    @InjectMocks
    lateinit var familyBankAccountPersistenceImpl: FamilyBankAccountPersistenceImpl

    @Test
    internal fun `loadFamilyBankAccountByFamilyName - when family is found, return family`() {
        // given
        val familyBankAccount = FamilyBankAccount("id", mutableListOf(), mutableListOf())
        given(familyBankAccountsRepository.findById(anyOrNull())).willReturn(Optional.of(familyBankAccount))

        // when
        val loadFamilyBankAccountByFamilyName = familyBankAccountPersistenceImpl.loadFamilyBankAccountByFamilyName("id")

        // then
        assertThat(loadFamilyBankAccountByFamilyName).isNotNull
        assertThat(loadFamilyBankAccountByFamilyName).isEqualTo(familyBankAccount.toDomain())
    }

    @Test
    internal fun `loadFamilyBankAccountByFamilyName - when family is not found, return null`() {
        // given
        given(familyBankAccountsRepository.findById(anyOrNull())).willReturn(Optional.empty())

        // when
        val loadFamilyBankAccountByFamilyName = familyBankAccountPersistenceImpl.loadFamilyBankAccountByFamilyName("id")

        // then
        assertThat(loadFamilyBankAccountByFamilyName).isNull()
    }

    @Test
    internal fun `updateFamily - when updateFamily is called, update the family in database`() {
        // given
        val familyBankAccount = FamilyBankAccountsImpl("id", null)
        given(familyBankAccountsRepository.save(ArgumentMatchers.any(FamilyBankAccount::class.java))).willAnswer { invocation -> invocation.arguments[0] }

        // when
        familyBankAccountPersistenceImpl.updateFamily(familyBankAccount)

        // then
        verify(familyBankAccountsRepository).save(ArgumentMatchers.any(FamilyBankAccount::class.java))
    }

    @Test
    internal fun `updateFamily - when updateFamily is called all transaction are not persisted`() {
        // given
        val familyBankAccount = FamilyBankAccountsImpl("id", null)
        familyBankAccount.registerFamilyMember(FamilyMember(username = "John"))
        val bankAccount = BankAccountImpl("bank", "bank", null);
        bankAccount.addTransaction(Credit("libelle", "banque", "desc", 10.0))
        familyBankAccount.registerAccount(bankAccount)
        given(familyBankAccountsRepository.save(ArgumentMatchers.any(FamilyBankAccount::class.java))).willAnswer { invocation -> invocation.arguments[0] }

        // when
        familyBankAccountPersistenceImpl.updateFamily(familyBankAccount)

        // then
        val argumentCaptor = argumentCaptor<FamilyBankAccount>()
        verify(familyBankAccountsRepository).save(argumentCaptor.capture())
        val familyBankAccountsPersisted = argumentCaptor.firstValue

        assertThat(familyBankAccountsPersisted).isNotNull
        assertThat(familyBankAccountsPersisted.familyId).isEqualTo("id")
        assertThat(familyBankAccountsPersisted.familyMembers).hasSize(1)
        assertThat(familyBankAccountsPersisted.familyMembers[0].username).isEqualTo("John")
        assertThat(familyBankAccountsPersisted.bankAccountsOwners).hasSize(1)
        assertThat(familyBankAccountsPersisted.bankAccountsOwners[0].owners).hasSize(1)
        assertThat(familyBankAccountsPersisted.bankAccountsOwners[0].owners[0].username).isEqualTo("John")
        assertThat(familyBankAccountsPersisted.bankAccountsOwners[0].account).isEqualTo(BankAccount.fromDomain(bankAccount))

    }
}