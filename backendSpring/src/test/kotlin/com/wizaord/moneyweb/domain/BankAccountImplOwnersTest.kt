package com.wizaord.moneyweb.domain

import com.wizaord.moneyweb.domain.exceptions.AccountWithoutOwnerException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class BankAccountImplOwnersTest {

    @Mock
    lateinit var infrastructureBankAccountNotifications: InfrastructureBankAccountNotifications

    @Test
    internal fun `constructor - An BankAccountOwners has at least one owner`() {
        // given
        val bankAccount = BankAccountImpl("name", "bank", infrastructureBankAccountNotifications)
        val familyMember = FamilyMember("John")

        // when
        val bankAccountOwners = BankAccountOwners(bankAccount, mutableListOf(familyMember))

        // then
        assertThat(bankAccountOwners.hasOwner(familyMember)).isTrue()
    }


    @Test
    internal fun `hasOwner - When familyMember is unknow then return false`() {
        // given
        val familyMember = FamilyMember("John")
        val familyMember2 = FamilyMember("JohnDo")
        val bankAccount = BankAccountImpl("name", "username", infrastructureBankAccountNotifications)
        val bankAccountOwners = BankAccountOwners(bankAccount, mutableListOf(familyMember))

        // then
        val hasOwner = bankAccountOwners.hasOwner(familyMember2)

        // then
        assertThat(hasOwner).isFalse()
    }

    @Test
    internal fun `constructor - An bankAccountOwners can have more than one owners`() {
        // given
        val familyMembers = mutableListOf(
                FamilyMember("John"),
                FamilyMember("Do"))
        val bankAccount = BankAccountImpl("name", "username", infrastructureBankAccountNotifications)

        // when
        val bankAccountOwners = BankAccountOwners(bankAccount, familyMembers)

        // then
        assertThat(bankAccountOwners.hasOwner(FamilyMember("John"))).isTrue()
        assertThat(bankAccountOwners.hasOwner(FamilyMember("Do"))).isTrue()
    }

    @Test
    internal fun `removeOwner - When function is called, owner is removed from bankAccount`() {
        // given
        val bankAccount = BankAccountImpl("name", "username", infrastructureBankAccountNotifications)
        val john = FamilyMember("John")
        val john2 = FamilyMember("John2")
        val bankAccountOwners = BankAccountOwners(bankAccount, mutableListOf(john, john2))

        // when
        bankAccountOwners.removeOwner(john)

        // then
        assertThat(bankAccountOwners.getOwners()).hasSize(1)
    }

    @Test
    internal fun `removeOwner - You cannot remove all owners from an account`() {
        // given
        val bankAccount = BankAccountImpl("name", "username", infrastructureBankAccountNotifications)
        val familyMember = FamilyMember("John")
        val bankAccountOwners = BankAccountOwners(bankAccount, mutableListOf(familyMember))

        // when
        Assertions.assertThrows(AccountWithoutOwnerException::class.java) {
            bankAccountOwners.removeOwner(FamilyMember("John"))
        }
    }

    @Test
    internal fun `removeOwner - When a not know familyOwner is removed from a BankAccountOwners, then do nothing`() {
        // given
        val bankAccount = BankAccountImpl("name", "username", infrastructureBankAccountNotifications)
        val familyMember = FamilyMember("John")
        val bankAccountOwners = BankAccountOwners(bankAccount, mutableListOf(familyMember))

        // when
        bankAccountOwners.removeOwner(FamilyMember("Do"))
    }


}