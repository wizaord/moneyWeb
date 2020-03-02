package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.services.CategoryService
import com.wizaord.moneyweb.services.FamilyBankAccountServiceFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class DebitCreditLoaderTest {

    @Mock
    lateinit var familyBankAccountServiceFactory: FamilyBankAccountServiceFactory
    @Mock
    lateinit var categoryService: CategoryService

    @InjectMocks
    lateinit var debitCreditLoader: DebitCreditLoader

    @Test
    internal fun `createDateFromString - 2018-01-12 00 00 00`() {
        val dateString = "2018-01-12 00:00:00"

        val createDateFromString = debitCreditLoader.createDateFromString(dateString)

        val localDateExpected = LocalDate.of(2018, 1, 12)
        assertThat(createDateFromString).isNotNull().isEqualTo(localDateExpected)
    }
}