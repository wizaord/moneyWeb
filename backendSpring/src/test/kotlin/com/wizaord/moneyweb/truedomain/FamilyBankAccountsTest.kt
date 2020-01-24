package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsTest {

    lateinit var familyBankAccounts: FamilyBankAccounts

    @BeforeEach
    internal fun injectMocks() {
        familyBankAccounts = FamilyBankAccounts("family")
    }

    @Test
    internal fun `When I register an account, thus it exists in accountManager`() {
        // given
        val bankAccount = BankAccount("name", "bankName")

        // when
        familyBankAccounts.registerAccount(bankAccount)

        // then
        assertThat(familyBankAccounts.bankAccounts).hasSize(1)
    }

    @Test
    internal fun `When I register accounts, I can retrieve them`() {
        // given
        val bankAccount1 = BankAccount("name", "bankName")
        familyBankAccounts.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName")
        familyBankAccounts.registerAccount(bankAccount2)

        // when
        val bankAccounts = familyBankAccounts.accessToAccounts()

        // then
        assertThat(bankAccounts).hasSize(2)
        assertThat(bankAccounts).contains(bankAccount1)
        assertThat(bankAccounts).contains(bankAccount2)
    }

    @Test
    internal fun `If I ask to access to the bank attached to SG, the others bank accounts are not retrieved`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        familyBankAccounts.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName2")
        familyBankAccounts.registerAccount(bankAccount2)

        // when
        val bankAccounts = familyBankAccounts.accessToAccountsByBankname("SG")

        // then
        assertThat(bankAccounts).hasSize(1)
        assertThat(bankAccounts).contains(bankAccount1)
    }

    @Test
    internal fun `I can retrieve managed account based on his name`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        familyBankAccounts.registerAccount(bankAccount1)

        // when
        val accountRetrieve = familyBankAccounts.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isEqualTo(bankAccount1)
    }

    @Test
    internal fun `If I try to retrieve an unkown bankAccount, a null object is retured`() {
        // given

        // when
        val accountRetrieve = familyBankAccounts.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isNull()
    }

    @Test
    internal fun `I cannot create an account with the same name of another account`() {
        // given
        val accountCreate = BankAccount("name", "bank")
        familyBankAccounts.registerAccount(accountCreate)

        val newBankAccount = BankAccount("name", "bank2")

        try {// when
            familyBankAccounts.registerAccount(newBankAccount)
            fail { "An exception should has been raised" }
        } catch (e: BankAccountWithTheSameNameException) {

        }
    }

    @Test
    internal fun `A family is identify by his name`() {
        // given
        val familyName = "family"

        // when
        val familyBankAccounts = FamilyBankAccounts(familyName)

        // then
        assertThat(familyBankAccounts.familyName).isEqualTo(familyName)
    }

}