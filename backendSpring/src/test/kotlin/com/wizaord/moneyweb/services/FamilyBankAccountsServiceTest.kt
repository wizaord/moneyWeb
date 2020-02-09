package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.*
import com.wizaord.moneyweb.domain.BankAccountImpl
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.domain.InfrastructureBankAccountFamilyNotifications
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.concurrent.TimeUnit

@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsServiceTest {

    @Mock
    lateinit var familyBankAccountPersistence: FamilyBankAccountPersistence

    @Mock
    lateinit var infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications


    @Test
    internal fun `constructor - when bean is initialized, FamilyBankAccounts is loaded and notification are disabled`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))
        familyBank.registerFamilyMember(FamilyMember("You"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)

        // when
        FamilyBankAccountsService("family", familyBankAccountPersistence)

        // then
        verifyZeroInteractions(infrastructureBankAccountFamilyNotifications)
    }

    @Test
    internal fun `getOwners - when owners are persisted, I can get the list of the owners`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))
        familyBank.registerFamilyMember(FamilyMember("You"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        val familyBankAccountsService = FamilyBankAccountsService("family", familyBankAccountPersistence)

        // when
        val owners = familyBankAccountsService.owners()

        // then
        assertThat(owners).isNotNull.isNotEmpty.hasSize(2).contains(FamilyMember("Me"), FamilyMember("You"))
    }

    @Test
    internal fun `accountRegister - when function call, create a BankAccountImpl and notification is sent`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        val familyBankAccountsService = FamilyBankAccountsService("family", familyBankAccountPersistence)

        // when
        familyBankAccountsService.accountRegister("name", "bank")

        // then
        val argumentCaptor = argumentCaptor<FamilyBankAccountsImpl>()
        verify(familyBankAccountPersistence).updateFamily(argumentCaptor.capture())
        val familyBankAccountsPersisted = argumentCaptor.firstValue

        assertThat(familyBankAccountsPersisted.bankAccountsOwners).hasSize(1)
        assertThat(familyBankAccountsPersisted.bankAccountsOwners[0].getOwners()).hasSize(1)
        val bankAccount = familyBankAccountsPersisted.bankAccountsOwners[0].bankAccount as BankAccountImpl
        assertThat(bankAccount.accountName).isEqualTo("name")
        assertThat(bankAccount.bankDefinition).isEqualTo("bank")
        assertThat(bankAccount.dateCreation).isCloseTo(LocalDate.now(), within(1, ChronoUnit.DAYS) )
    }

}