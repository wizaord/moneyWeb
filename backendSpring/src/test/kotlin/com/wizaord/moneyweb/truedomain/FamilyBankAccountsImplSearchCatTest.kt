package com.wizaord.moneyweb.truedomain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wizaord.moneyweb.truedomain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.truedomain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.truedomain.transactions.Credit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsImplSearchCatTest {

    @Mock
    lateinit var infrastructureBankAccountFamilyNotifications: InfrastructureBankAccountFamilyNotifications

    @Mock
    lateinit var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications

    lateinit var familyBankAccountsImpl: FamilyBankAccountsImpl

    @BeforeEach
    internal fun injectMocks() {
        familyBankAccountsImpl = FamilyBankAccountsImpl("family", infrastructureBankAccountFamilyNotifications)
    }

    @Test
    internal fun `getCategoryConfiguredByPreviousFamilyTransactions - If transactions with the same name are found, then return the category of the most recent transaction `() {
        // given
        val transaction1 = Credit("lib", "libBank", "desc", 10.0)

        // when

        // then
    }

    private fun createDummyAccount(): BankAccountImpl =
            BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications)

}