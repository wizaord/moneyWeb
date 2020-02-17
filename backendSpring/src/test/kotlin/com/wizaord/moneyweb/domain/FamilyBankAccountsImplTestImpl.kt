package com.wizaord.moneyweb.domain

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wizaord.moneyweb.domain.exceptions.BankAccountWithTheSameNameException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberAlreadyExistException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberNotKnowException
import com.wizaord.moneyweb.domain.exceptions.FamilyMemberOwnerException
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


@ExtendWith(MockitoExtension::class)
internal class FamilyBankAccountsImplTestImpl {

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
    internal fun `constructor - A family is identify by his name`() {
        // given
        val familyName = "family"

        // when
        val familyBankAccounts = FamilyBankAccountsImpl(familyName, infrastructureBankAccountFamilyNotifications)

        // then
        assertThat(familyBankAccounts.familyName).isEqualTo(familyName)
    }

    @Test
    internal fun `registerFamilyMember - I can register a new member to my family`() {
        // given
        val familyMember = FamilyMember("cedric")
        val familyBankAccounts = FamilyBankAccountsImpl("family", infrastructureBankAccountFamilyNotifications)

        // when
        familyBankAccounts.registerFamilyMember(familyMember)

        // then
        assertThat(familyBankAccounts.getFamily()).contains(familyMember)
        assertThat(familyBankAccounts.getFamily()).hasSize(1)
        verify(infrastructureBankAccountFamilyNotifications).notifyFamilyBankAccountUpdate(familyBankAccounts)
    }

    @Test
    internal fun `registerFamilyMember - I cannot register a new member with the same name as an existing member`() {
        // given
        val familyMember = FamilyMember("cedric")

        // when
        familyBankAccountsImpl.registerFamilyMember(familyMember)
        Assertions.assertThrows(FamilyMemberAlreadyExistException::class.java) {
            familyBankAccountsImpl.registerFamilyMember(familyMember)
        }
        verify(infrastructureBankAccountFamilyNotifications).notifyFamilyBankAccountUpdate(familyBankAccountsImpl)
    }

    @Test
    internal fun `removeFamilyMember - When I remove a unknown member family, do nothing`() {
        // given

        // when
        familyBankAccountsImpl.removeFamilyMember(FamilyMember("unknowUser"))

        // then
        verify(infrastructureBankAccountFamilyNotifications).notifyFamilyBankAccountUpdate(familyBankAccountsImpl)

    }

    @Test
    internal fun `removeFamilyMember - When I remove a member, member is removed`() {
        // given
        val refFamily = mutableListOf<FamilyMember>()
        refFamily.addAll(familyBankAccountsImpl.getFamily())
        familyBankAccountsImpl.registerFamilyMember(FamilyMember("John"))

        // when
        familyBankAccountsImpl.removeFamilyMember(FamilyMember("John"))

        // then
        assertThat(refFamily).isEqualTo(familyBankAccountsImpl.getFamily())
        verify(infrastructureBankAccountFamilyNotifications, times(2)).notifyFamilyBankAccountUpdate(familyBankAccountsImpl)
    }

    @Test
    internal fun `removeFamilyMember - It is not allowed to remove a familyMember if this member is owner of bankAccounts`() {
        // given
        val bankAccount = createDummyAccount()
        familyBankAccountsImpl.registerFamilyMember(FamilyMember("Do"))
        familyBankAccountsImpl.registerAccount(bankAccount)

        // when
        Assertions.assertThrows(FamilyMemberOwnerException::class.java) {
            familyBankAccountsImpl.removeFamilyMember(FamilyMember("Do"))
        }

        // then
        verify(infrastructureBankAccountFamilyNotifications, times(2)).notifyFamilyBankAccountUpdate(familyBankAccountsImpl)
    }

    @Test
    internal fun `registerAccount - When I register an account with owner, thus it exists in bankAccountManager`() {
        // given
        val bankAccount = createDummyAccount()
        val familyMember = FamilyMember("John")
        familyBankAccountsImpl.registerFamilyMember(familyMember)


        // when
        familyBankAccountsImpl.registerAccount(bankAccount, listOf(familyMember))

        // then
        assertThat(familyBankAccountsImpl.bankAccountsOwners).hasSize(1)
        verify(infrastructureBankAccountFamilyNotifications, times(2)).notifyFamilyBankAccountUpdate(familyBankAccountsImpl)
    }

    @Test
    internal fun `registerAccount - If a register an account to an unknow user, then raise exception`() {
        // given
        val bankAccount = createDummyAccount()
        val familyMember = FamilyMember("John")

        // when
        Assertions.assertThrows(FamilyMemberNotKnowException::class.java) {
            familyBankAccountsImpl.registerAccount(bankAccount, listOf(familyMember))
        }
        // then
        verifyZeroInteractions(infrastructureBankAccountFamilyNotifications)
    }

    @Test
    internal fun `registerAccount - If a register a bankAccount without owner, all family members are owners`() {
        // given
        val bankAccount = createDummyAccount()
        familyBankAccountsImpl.registerFamilyMember(FamilyMember("Do"))

        // when
        familyBankAccountsImpl.registerAccount(bankAccount)

        // then
        assertThat(familyBankAccountsImpl.bankAccountsOwners).hasSize(1)
        assertThat(familyBankAccountsImpl.bankAccountsOwners[0].getOwners()).hasSize(1)

    }

    @Test
    internal fun `accessToAccounts - When I register accounts, I can retrieve them`() {
        // given
        val bankAccount1 = createDummyAccount()
        familyBankAccountsImpl.registerAccount(bankAccount1)

        // when
        val bankAccounts = familyBankAccountsImpl.accessToAccounts()

        // then
        assertThat(bankAccounts).hasSize(1)
    }

    @Test
    internal fun `accessToAccountsByBankname - If I ask to access to the bank attached to SG, the others bank accounts are not retrieved`() {
        // given
        val bankAccount1 = BankAccountImpl("name", "SG", infrastructureBankAccountNotifications)
        familyBankAccountsImpl.registerAccount(bankAccount1)

        // when
        val bankAccounts = familyBankAccountsImpl.accessToAccountsByBankname("SG")

        // then
        assertThat(bankAccounts).hasSize(1)
    }

    @Test
    internal fun `accessToAccountByAccountName - I can retrieve managed account based on his name`() {
        // given
        val bankAccount = createDummyAccount()
        familyBankAccountsImpl.registerAccount(bankAccount)

        // when
        val accountRetrieve = familyBankAccountsImpl.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve?.bankAccount).isEqualTo(bankAccount)
    }

    @Test
    internal fun `accessToAccountByAccountName - If I try to retrieve an unkown bankAccount, a null object is retured`() {
        // given

        // when
        val accountRetrieve = familyBankAccountsImpl.accessToAccountByAccountName("name")

        // then
        assertThat(accountRetrieve).isNull()
    }

    @Test
    internal fun `registerAccount - I cannot create an account with the same name of another account`() {
        // given
        val accountCreate = createDummyAccount()
        familyBankAccountsImpl.registerAccount(accountCreate)

        val newBankAccount = createDummyAccount()

        try {// when
            familyBankAccountsImpl.registerAccount(newBankAccount)
            fail { "An exception should has been raised" }
        } catch (e: BankAccountWithTheSameNameException) {

        }
    }

    @Test
    internal fun `changeBankAccountOwners - when owners are changed, notification is sent`() {
        // given
        val accountCreate = createDummyAccount()
        val familyMemberJohn = FamilyMember("John")
        val familyMemberDo = FamilyMember("Do")
        familyBankAccountsImpl.registerFamilyMember(familyMemberJohn)
        familyBankAccountsImpl.registerFamilyMember(familyMemberDo)
        familyBankAccountsImpl.registerAccount(accountCreate, listOf(familyMemberJohn))

        // when
        familyBankAccountsImpl.changeBankAccountOwners(accountCreate.getName(), listOf(familyMemberDo, familyMemberJohn))
        
        // then
        assertThat(familyBankAccountsImpl.accessToAccountByAccountName(accountCreate.getName())!!.getOwners()).hasSize(2)
        verify(infrastructureBankAccountFamilyNotifications, times(4)).notifyFamilyBankAccountUpdate(anyOrNull())
    }

    @Test
    internal fun `removeAccount - if account does not exist, then do nothing`() {
        // given

        // when
        familyBankAccountsImpl.removeAccount("NOT KNOW")

        // then
        verifyZeroInteractions(infrastructureBankAccountFamilyNotifications)
        verifyZeroInteractions(infrastructureBankAccountNotifications)
    }

    @Test
    internal fun `removeAccount - delete all transaction before update the account family`() {
        // given
        val bankAccount = BankAccountImpl("accountName", "bankName")
        bankAccount.addTransaction(Credit("credit", "credit", null, 10.0))
        bankAccount.addTransaction(Debit("credit", "credit", null, 10.0))
        bankAccount.registerInfrastructureBankAccountNotification(infrastructureBankAccountNotifications)

        familyBankAccountsImpl.registerAccount(bankAccount)

        // when
        familyBankAccountsImpl.removeAccount("accountName")

        // then
        assertThat(familyBankAccountsImpl.accessToAccountByAccountName("accountName")).isNull()
        verify(infrastructureBankAccountNotifications, times(2)).notifyRemoveTransaction(anyOrNull())
        verify(infrastructureBankAccountFamilyNotifications, times(2)).notifyFamilyBankAccountUpdate(anyOrNull())
    }

    private fun createDummyAccount(): BankAccount =
            BankAccountImpl("name", "bankName", infrastructureBankAccountNotifications)

}