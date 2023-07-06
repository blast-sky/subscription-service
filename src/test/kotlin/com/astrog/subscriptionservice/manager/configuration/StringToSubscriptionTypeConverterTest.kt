package com.astrog.subscriptionservice.manager.configuration

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.exception.UnsupportedSubscriptionTypeException
import com.astrog.subscriptionservice.manager.configuration.StringToSubscriptionTypeConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class StringToSubscriptionTypeConverterTest {

    private val converter = StringToSubscriptionTypeConverter()

    @ParameterizedTest
    @MethodSource("provideStringAndSubscriptionType")
    fun `convert Must return right SubscriptionType When take right argument`(
        source: String,
        expected: SubscriptionType,
    ) {
        assertEquals(expected, converter.convert(source))
    }

    @Test
    fun `convert Must throw UnsupportedSubscriptionType When source is not valid`() {
        val notValidSubscriptionType = "mms"
        assertThrows<UnsupportedSubscriptionTypeException> { converter.convert(notValidSubscriptionType) }
    }

    companion object {

        @JvmStatic
        private fun provideStringAndSubscriptionType(): List<Arguments> {
            return listOf(
                Arguments.of("telegram", SubscriptionType.TELEGRAM),
                Arguments.of("sms", SubscriptionType.SMS),
                Arguments.of("email", SubscriptionType.EMAIL),
            )
        }
    }
}