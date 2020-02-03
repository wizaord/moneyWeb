package com.wizaord.moneyweb.truedomain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.wizaord.moneyweb.truedomain.transactions.Credit
import com.wizaord.moneyweb.truedomain.transactions.TransactionMatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsImplSearchCatTest {

    @Mock
    lateinit var infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications

    @Mock
    lateinit var bankAccount: BankAccount

    lateinit var familyBankAccountsImpl: FamilyBankAccounts

    @BeforeEach
    internal fun injectMocks() {
        familyBankAccountsImpl = FamilyBankAccountsImpl("family", infrastructureBankAccountFamilyNotifications)
        familyBankAccountsImpl.registerFamilyMember(FamilyMember("Do"))

        given(bankAccount.getName()).willReturn("bank")
        familyBankAccountsImpl.registerAccount(bankAccount)
    }

    @Test
    internal fun `getCategoryConfiguredByPreviousFamilyTransactions - If transactions with the same name are found, then return the category of the most recent transaction `() {
        // given
        val transaction1 = Credit("lib", "libBank", "desc", 10.0)
        val transaction2 = Credit("lib", "libBank", "desc 2", 10.0)

        given(bankAccount.getTransactionsMatched(anyOrNull())).willReturn(
                listOf(TransactionMatch(transaction1, 1.0),
                        TransactionMatch(transaction2, 0.9)))

        val newTransaction = Credit("lib", "libBank", "desc", 10.0)

        // when
        val transactionRetrieved = familyBankAccountsImpl.getCategoryConfiguredByPreviousFamilyTransaction(newTransaction)

        // then
        assertThat(transactionRetrieved).isEqualTo(transaction2)
    }

}