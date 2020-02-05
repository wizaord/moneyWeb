package com.wizaord.moneyweb.infrastructure

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccount
import com.wizaord.moneyweb.infrastructure.domain.FamilyBankAccountsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountPersistenceImplTest {

    @Mock
    lateinit var familyBankAccountsRepository: FamilyBankAccountsRepository

    @InjectMocks
    lateinit var familyBankAccountPersistenceImpl: FamilyBankAccountPersistenceImpl

    @Test
    internal fun `loadFamilyBankAccountByFamilyName - when family is found, return family`() {
        // given
        val familyBankAccount = FamilyBankAccount("id", mutableListOf())
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
}