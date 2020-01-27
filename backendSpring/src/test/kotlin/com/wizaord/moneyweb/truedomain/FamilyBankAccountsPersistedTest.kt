package com.wizaord.moneyweb.truedomain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsPersistedTest{

    @Mock
    lateinit var bankAccountPersistence: BankAccountPersistence

    lateinit var familyBankAccounts: FamilyBankAccounts

    @Test
    internal fun `init - At init, bankAccounts are loaded from interface`() {
        // given
        val familyBankAccountsStored = FamilyBankAccounts("family")
        familyBankAccountsStored.registerFamilyMember(FamilyMember("John"))
        familyBankAccountsStored.registerAccount(BankAccount("name", "bankName"))

        given(bankAccountPersistence.loadBankAccountByFamilyName(anyOrNull())).willReturn(familyBankAccountsStored)

        // when
        familyBankAccounts = FamilyBankAccountsPersisted("family", bankAccountPersistence)

        // then
        assertThat(familyBankAccounts.familyName).isEqualTo("family")
        assertThat(familyBankAccounts.familyMembers).contains(FamilyMember("John"))
        assertThat(familyBankAccounts.accessToAccounts()).hasSize(1)
    }

}