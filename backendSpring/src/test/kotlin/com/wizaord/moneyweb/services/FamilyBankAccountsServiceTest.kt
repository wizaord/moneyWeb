package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wizaord.moneyweb.domain.FamilyBankAccountsImpl
import com.wizaord.moneyweb.domain.FamilyMember
import com.wizaord.moneyweb.domain.InfrastructureBankAccountFamilyNotifications
import com.wizaord.moneyweb.infrastructure.FamilyBankAccountPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

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

}