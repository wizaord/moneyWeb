package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.*
import com.wizaord.moneyweb.domain.*
import com.wizaord.moneyweb.domain.categories.CategoryFamily
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.TransactionMatch
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
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

    @Mock
    lateinit var categoryService: CategoryService

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
    internal fun `transactionUpdate - when transaction is Pointed and contains virement to another account, then add a new transaction in another account`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))
        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)

        val bankAccountRef = bankAccount
        val bankAccountDestination = Mockito.mock(BankAccount::class.java)
        given(bankAccountRef.getName()).willReturn("accountName")
        given(bankAccountDestination.getName()).willReturn( "bankAccountRefName")

        familyBank.registerAccount(bankAccountRef)
        familyBank.registerAccount(bankAccountDestination)

        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        val debit = Debit("libelle", "bank", null, 10.0, isPointe = true, id = "1")
        debit.addVentilation(DebitVentilation(10.0, "1"))

        val debitRef = Debit("libelle", "bank", null, 10.0,isPointe = false, id = "1")
        debitRef.addVentilation(DebitVentilation(10.0, "1"))

        given(categoryService.isVirementCategory("1")).willReturn {true}
        given(categoryService.getAccountNameVirementDestination("1")).willReturn { "bankAccountRefName" }

        // when
        familyBankAccountsService.transactionUpdate("accountName", debit.id, debit)

        // then
        verify(bankAccountDestination).addTransaction(anyOrNull())
        verify(bankAccountRef).updateTransaction(anyOrNull())
    }


    internal fun `transactionUpdate - when transaction is already Pointed and contains virement to another account, then do nothing in another account`() {
        // given

        // when

        // then
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
        assertThat(result).isNull()
        verify(bankAccount, times(0)).addTransaction(anyOrNull())
    }

    @Test
    internal fun `getLastTransactionWithBetterMatchScore - return the best score`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.getTransactionsMatched(anyOrNull())).willReturn(listOf(
                TransactionMatch(Debit("debit1", "bank", null, 10.0), 0.9),
                TransactionMatch(Debit("debit2", "bank", null, 10.0), 1.0),
                TransactionMatch(Debit("debit3", "bank", null, 10.0), 0.8)
        ))
        given(bankAccount.getName()).willReturn("accountName")

        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        val myTransaction = Debit("debit", "bank", null, 10.0)

        // when
        val lastT = familyBankAccountsService.getLastTransactionWithBetterMatchScore(myTransaction)

        // then
        assertThat(lastT!!.userLibelle).isEqualTo("debit2")
    }

    @Test
    internal fun `getLastTransactionWithBetterMatchScore - If equal return the most recent`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.getTransactionsMatched(anyOrNull())).willReturn(listOf(
                TransactionMatch(Debit("debit1", "bank", null, 10.0, dateCreation = LocalDate.of(2020, 1, 1)), 1.0),
                TransactionMatch(Debit("debit2", "bank", null, 10.0, dateCreation = LocalDate.of(2019, 1, 1)), 1.0),
                TransactionMatch(Debit("debit3", "bank", null, 10.0, dateCreation = LocalDate.of(2021, 1, 1)), 0.8)
        ))
        given(bankAccount.getName()).willReturn("accountName")

        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        val myTransaction = Debit("debit", "bank", null, 10.0)

        // when
        val lastT = familyBankAccountsService.getLastTransactionWithBetterMatchScore(myTransaction)

        // then
        assertThat(lastT!!.userLibelle).isEqualTo("debit1")
    }

    @Test
    internal fun `accountUpdateName - when name is changed then virement category is changed`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.getName()).willReturn("accountName")

        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        // when
        familyBankAccountsService.accountUpdateName("oldName", "new name")

        // then
        verify(categoryService).renameCategoryVirement(anyOrNull(), anyOrNull())
    }

    @Test
    internal fun `transactionsNotInteral - return only non internal transaction`() {
        // given
        val familyBank = FamilyBankAccountsImpl("family")
        familyBank.registerFamilyMember(FamilyMember("Me"))

        given(familyBankAccountPersistence.loadFamilyBankAccountByFamilyName(anyOrNull())).willReturn(familyBank)
        given(bankAccount.getName()).willReturn("accountName")
        familyBank.registerAccount(bankAccount)
        familyBankAccountsService.loadFamilyBankFromPersistence("family")

        val creditInterne = Credit("ko", "", "", 10.0)
        creditInterne.addVentilation(CreditVentilation(10.0, CategoryFamily.VIREMENT_INTERNE_ID))

        val creditNonInterne = Credit("ok", "", "", 10.0)
        creditNonInterne.addVentilation(CreditVentilation(10.0, "2"))

        given(bankAccount.getTransactions()).willReturn(listOf(creditInterne, creditNonInterne))
        given(categoryService.getAll()).willReturn(listOf(
                CategoryFamily("family", "1"),
                CategoryFamily("family", "2")
        ))

        // when
        val transactionsNotInternal = familyBankAccountsService.transactionsNotInternal("accountName")

        // then
        assertThat(transactionsNotInternal).hasSize(1)
        assertThat(transactionsNotInternal[0].userLibelle).isEqualTo("ok")

    }
}