package com.wizaord.moneyweb.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.format.support.FormattingConversionService
import java.time.format.DateTimeFormatter

@Configuration
class DateTimeConfig {

    @Bean
    fun conversionService(): FormattingConversionService {
        val conversionService = DefaultFormattingConversionService(false)

        val dateTimeFormatterRegistrar = DateTimeFormatterRegistrar()

        //"2013-07-17T22:00:00.000+0000"
        dateTimeFormatterRegistrar.setDateFormatter(DateTimeFormatter.ISO_LOCAL_DATE)
        dateTimeFormatterRegistrar.setDateTimeFormatter(DateTimeFormatter.ISO_LOCAL_DATE)
        dateTimeFormatterRegistrar.registerFormatters(conversionService)

        return conversionService
    }
}