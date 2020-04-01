package com.wizaord.moneyweb.services

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.given
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class StatistiquesServiceTest {

    @Mock
    lateinit var familyBankAccountServiceFactory: FamilyBankAccountServiceFactory

    @Mock
    lateinit var familyBankAccountsService: FamilyBankAccountsService

    @InjectMocks
    lateinit var statistiquesService: StatistiquesService

    @Test
    internal fun `getAccountSoldeByMonth - return for all accounts the amount of credits and debits for each month`() {
        // given
        given(familyBankAccountServiceFactory.getFamilyServiceWithTransactions("family")).willReturn(familyBankAccountsService)
        given(familyBankAccountsService.bankAccountsName()).willReturn(listOf("account1", "account2"))
        given(familyBankAccountsService.bankAccountOwners(anyOrNull())).willReturn(listOf("family"))
        given(familyBankAccountsService.transactions("account1")).willReturn(listOf(
                Debit("debit", "", "", 10.0, dateCreation = LocalDate.of(2020, 10, 10)),
                Debit("debit2", "", "", 10.0, dateCreation = LocalDate.of(2020, 10, 12)),
                Credit("credit", "", "", 5.0, dateCreation = LocalDate.of(2020, 10, 13))
        ))
        given(familyBankAccountsService.transactions("account2")).willReturn(listOf(
                Debit("debit", "", "", 11.0, dateCreation = LocalDate.of(2020, 11, 10)),
                Debit("debit", "", "", 9.0, dateCreation = LocalDate.of(2020, 9, 10)),
                Debit("debit", "", "", 8.0, dateCreation = LocalDate.of(2020, 8, 10))
        ))

        // when
        val results = statistiquesService.getAccountSoldeByMonth("family")

        // then
        assertThat(results).hasSize(2)
        val resultAccount1 = results.first { it.accountName == "account1" }
        val resultAccount2 = results.first { it.accountName == "account2" }

        assertThat(resultAccount1.monthStatistiques).hasSize(1)
        assertThat(resultAccount1.owners).containsExactly("family")
        assertThat(resultAccount1.monthStatistiques[0].month).isEqualTo(LocalDate.of(2020, 10, 1))
        assertThat(resultAccount1.monthStatistiques[0].revenus).isEqualTo(5.0)
        assertThat(resultAccount1.monthStatistiques[0].depenses).isEqualTo(20.0)

        assertThat(resultAccount2.monthStatistiques).hasSize(3)
        assertThat(resultAccount2.monthStatistiques[0].month).isEqualTo(LocalDate.of(2020, 8, 1))
        assertThat(resultAccount2.monthStatistiques[0].revenus).isEqualTo(0.0)
        assertThat(resultAccount2.monthStatistiques[0].depenses).isEqualTo(8.0)
        assertThat(resultAccount2.monthStatistiques[1].month).isEqualTo(LocalDate.of(2020, 9, 1))
        assertThat(resultAccount2.monthStatistiques[1].revenus).isEqualTo(0.0)
        assertThat(resultAccount2.monthStatistiques[1].depenses).isEqualTo(9.0)
        assertThat(resultAccount2.monthStatistiques[2].month).isEqualTo(LocalDate.of(2020, 11, 1))
        assertThat(resultAccount2.monthStatistiques[2].revenus).isEqualTo(0.0)
        assertThat(resultAccount2.monthStatistiques[2].depenses).isEqualTo(11.0)
    }
}