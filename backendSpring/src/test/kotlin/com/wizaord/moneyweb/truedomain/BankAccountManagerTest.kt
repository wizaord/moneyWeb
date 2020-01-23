package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class BankAccountManagerTest {

    @InjectMocks
    lateinit var bankAccountManager: BankAccountManager

    @Test
    internal fun `When I register an account, thus it exists in accountManager`() {
        // given
        val bankAccount = BankAccount("name", "bankName")

        // when
        bankAccountManager.registerAccount(bankAccount)

        // then
        assertThat(bankAccountManager.bankAccounts).hasSize(1)
    }

    @Test
    internal fun `When I register accounts, I can retrieve them`() {
        // given
        val bankAccount1 = BankAccount("name", "bankName")
        bankAccountManager.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName")
        bankAccountManager.registerAccount(bankAccount2)

        // when
        val bankAccounts = bankAccountManager.accessToAccounts()

        // then
        assertThat(bankAccounts).hasSize(2)
        assertThat(bankAccounts).contains(bankAccount1)
        assertThat(bankAccounts).contains(bankAccount2)
    }

    @Test
    internal fun `If I ask to access to the bank attached to SG, the others bank accounts are not retrieved`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        bankAccountManager.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName2")
        bankAccountManager.registerAccount(bankAccount2)

        // when
        val bankAccounts = bankAccountManager.accessToAccountsByBankname("SG")

        // then
        assertThat(bankAccounts).hasSize(1)
        assertThat(bankAccounts).contains(bankAccount1)
    }

    @Test
    internal fun `I can retrieve managed account based on his name`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        bankAccountManager.registerAccount(bankAccount1)

        // when
        val accountRetrieve = bankAccountManager.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isEqualTo(bankAccount1)
    }

    @Test
    internal fun `If I try to retrieve an unkown bankAccount, a null object is retured`() {
        // given

        // when
        val accountRetrieve = bankAccountManager.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isNull()
    }

    @Test
    internal fun `I cannot create an account with the same name of another account`() {
        // given
        val accountCreate = BankAccount("name", "bank")
        bankAccountManager.registerAccount(accountCreate)

        val newBankAccount = BankAccount("name", "bank2")

        try {// when
            bankAccountManager.registerAccount(newBankAccount)
            fail { "An exception should has been raised" }
        } catch (e: BankAccountWithTheSameNameException) {

        }
    }
}