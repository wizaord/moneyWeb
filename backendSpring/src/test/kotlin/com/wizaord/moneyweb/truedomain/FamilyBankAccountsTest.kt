package com.wizaord.moneyweb.truedomain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsTest {

    @Mock
    lateinit var bankAccountPersistence: BankAccountPersistence

    lateinit var familyBankAccounts: FamilyBankAccounts

    @BeforeEach
    internal fun injectMocks() {
        familyBankAccounts = FamilyBankAccounts("family")
        familyBankAccounts.registerFamilyMember(FamilyMember("Do"))
    }

    @Test
    internal fun `constructor - A family is identify by his name`() {
        // given
        val familyName = "family"

        // when
        val familyBankAccounts = FamilyBankAccounts(familyName)

        // then
        assertThat(familyBankAccounts.familyName).isEqualTo(familyName)
    }

    @Test
    internal fun `registerFamilyMember - I can register a new member to my family`() {
        // given
        val familyMember = FamilyMember("cedric")
        val familyBankAccounts = FamilyBankAccounts("family")

        // when
        familyBankAccounts.registerFamilyMember(familyMember)

        // then
        assertThat(familyBankAccounts.familyMembers).contains(familyMember)
        assertThat(familyBankAccounts.familyMembers).hasSize(1)
    }

    @Test
    internal fun `registerFamilyMember - I cannot register a new member with the same name as an existing member`() {
        // given
        val familyMember = FamilyMember("cedric")

        // when
        familyBankAccounts.registerFamilyMember(familyMember)
        Assertions.assertThrows(FamilyMemberAlreadyExistException::class.java) {
            familyBankAccounts.registerFamilyMember(familyMember)
        }
    }

    @Test
    internal fun `removeFamilyMember - When I remove a unknown member family, do nothing`() {
        // given

        // when
        familyBankAccounts.removeFamilyMember(FamilyMember("unknowUser"))

        // then
    }

    @Test
    internal fun `removeFamilyMember - When I remove a member, member is removed`() {
        // given
        val refFamily = mutableListOf<FamilyMember>()
        refFamily.addAll(familyBankAccounts.familyMembers)
        familyBankAccounts.registerFamilyMember(FamilyMember("John"))

        // when
        familyBankAccounts.removeFamilyMember(FamilyMember("John"))

        // then
        assertThat(refFamily).isEqualTo(familyBankAccounts.familyMembers)
    }

    @Test
    internal fun `removeFamilyMember - It is not allowed to remove a familyMember if this member is owner of bankAccounts`() {
        // given
        val bankAccount = BankAccount("name", "bankName")
        familyBankAccounts.registerAccount(bankAccount)

        // when
        Assertions.assertThrows(FamilyMemberOwnerException::class.java) {
            familyBankAccounts.removeFamilyMember(FamilyMember("Do"))
        }

        // then
    }

    @Test
    internal fun `registerAccount - When I register an account with owner, thus it exists in bankAccountManager`() {
        // given
        val bankAccount = BankAccount("name", "bankName")
        val familyMember = FamilyMember("John")
        familyBankAccounts.registerFamilyMember(familyMember)

        // when
        familyBankAccounts.registerAccount(bankAccount, listOf(familyMember))

        // then
        assertThat(familyBankAccounts.bankAccountsOwners).hasSize(1)
    }

    @Test
    internal fun `registerAccount - If a register an account to an unknow user, then raise exception`() {
        // given
        val bankAccount = BankAccount("name", "bankName")
        val familyMember = FamilyMember("John")

        // when
        Assertions.assertThrows(FamilyMemberNotKnowException::class.java) {
            familyBankAccounts.registerAccount(bankAccount, listOf(familyMember))
        }
        // then
    }

    @Test
    internal fun `registerAccount - If a register a bankAccount without owner, all family members are owners`() {
        // given
        val bankAccount = BankAccount("name", "bankName")

        // when
        familyBankAccounts.registerAccount(bankAccount)

        // then
        assertThat(familyBankAccounts.bankAccountsOwners).hasSize(1)
        assertThat(familyBankAccounts.bankAccountsOwners[0].owners).hasSize(1)

    }

    @Test
    internal fun `accessToAccounts - When I register accounts, I can retrieve them`() {
        // given
        val bankAccount1 = BankAccount("name", "bankName")
        familyBankAccounts.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName")
        familyBankAccounts.registerAccount(bankAccount2)

        // when
        val bankAccounts = familyBankAccounts.accessToAccounts()

        // then
        assertThat(bankAccounts).hasSize(2)
    }

    @Test
    internal fun `accessToAccountsByBankname - If I ask to access to the bank attached to SG, the others bank accounts are not retrieved`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        familyBankAccounts.registerAccount(bankAccount1)
        val bankAccount2 = BankAccount("name2", "bankName2")
        familyBankAccounts.registerAccount(bankAccount2)

        // when
        val bankAccounts = familyBankAccounts.accessToAccountsByBankname("SG")

        // then
        assertThat(bankAccounts).hasSize(1)
    }

    @Test
    internal fun `accessToAccountByAccountName - I can retrieve managed account based on his name`() {
        // given
        val bankAccount1 = BankAccount("name", "SG")
        familyBankAccounts.registerAccount(bankAccount1)

        // when
        val accountRetrieve = familyBankAccounts.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve?.bankAccount).isEqualTo(bankAccount1)
    }

    @Test
    internal fun `accessToAccountByAccountName - If I try to retrieve an unkown bankAccount, a null object is retured`() {
        // given

        // when
        val accountRetrieve = familyBankAccounts.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isNull()
    }

    @Test
    internal fun `registerAccount - I cannot create an account with the same name of another account`() {
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

}