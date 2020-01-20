package com.wizaord.moneyweb.init

import com.wizaord.moneyweb.services.AccountService
import com.wizaord.moneyweb.services.UserService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AccountLoaderTest {

    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var accountService: AccountService

    @InjectMocks
    lateinit var accountLoader: AccountLoader

    @Test
    fun `when createDateFromString receive correct date then return date object`() {
        // given
        val dateStr = "2013-07-22"

        // when
        val date = accountLoader.createDateFromString(dateStr)

        // then
        assertThat(date).isNotNull()
        assertThat(date).hasDayOfMonth(22).hasMonth(7).hasYear(2013)
    }
}