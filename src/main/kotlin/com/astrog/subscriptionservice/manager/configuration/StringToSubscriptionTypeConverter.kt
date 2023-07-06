package com.astrog.subscriptionservice.manager.configuration

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.exception.UnsupportedSubscriptionTypeException
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.core.convert.converter.Converter


private val stringToSubscriptionType = SubscriptionType::class.java.declaredFields
    .mapNotNull { field ->
        field.annotations.filterIsInstance<JsonProperty>().firstOrNull()?.value?.let {
            it to SubscriptionType.valueOf(field.name)
        }
    }.toMap()

class StringToSubscriptionTypeConverter : Converter<String, SubscriptionType> {

    override fun convert(source: String): SubscriptionType {
        return stringToSubscriptionType[source]
            ?: throw UnsupportedSubscriptionTypeException(source)
    }
}