package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.*
import com.wizaord.moneyweb.domain.*
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsServiceTest {

    @Mock
    lateinit var bankAccount: BankAccount

    @Mock
    lateinit var familyBankAccountPersistence: FamilyBankAccountPersistence

    @Mock
    lateinit var infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications

    @InjectMocks
    lateinit var familyBankAccountsService: FamilyBankAccountsService

    @Test
    internal fun `constructor - when bean is initialized, FamilyBankAccounts is loaded and notification are disabled`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))
        familyBank.registerFamilyMember(FamilyMember("You"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)

        // when
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

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
        
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

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
        
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

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
        assertThat(bankAccount.dateCreation).isCloseTo(LocalDate.now(), within(1, ChronoUnit.DAYS))
    }

    @Test
    internal fun `transactionUpdate - if transactionId is not the same as transaction then do nothing`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        
        familyBankAccountsService.loadFamilyBankFromPersistence("family")
        val debit = Debit("libelle", "bank", null, 10.0)

        // when
        familyBankAccountsService.transactionUpdate("accountName", "id", debit)

        // then
        verifyZeroInteractions(familyBankAccountPersistence)
    }


    @Test
    internal fun `transactionUpdate - if transaction is not valid, then return an exception`() {
// given
        val familyBank = FamilyBankAccountsImpl("family")
        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        
        familyBankAccountsService.loadFamilyBankFromPersistence("family")
        val debit = Debit("libelle", "bank", null, 10.0)

        // when
        familyBankAccountsService.transactionUpdate("accountName", debit.id, debit)

        // then
        verifyZeroInteractions(familyBankAccountPersistence)
    }

    @Test
    internal fun `transactionUpdate - when valid call replaceTransaction from domain`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.getName()).willReturn("accountName")

        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        val debit = Debit("libelle", "bank", null, 10.0)
        debit.addVentilation(DebitVentilation(10.0))

        // when
        familyBankAccountsService.transactionUpdate("accountName", debit.id, debit)

        // then
        verify(bankAccount, times(1)).updateTransaction(debit)
    }

    @Test
    internal fun `transactionRegistered - if transaction already exist in the same account, then do nothing`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.hasTransactionByProperties(anyOrNull())).willReturn(true)
        given(bankAccount.getName()).willReturn("accountName")

        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        // when
        val transactionCredit = Credit("credit", "bank credit", "", 10.0)
        val result = familyBankAccountsService.transactionRegister("accountName", transactionCredit)
        
        // then
        assertThat(result).isFalse()
        verify(bankAccount, times(0)).addTransaction(anyOrNull())
    }

}