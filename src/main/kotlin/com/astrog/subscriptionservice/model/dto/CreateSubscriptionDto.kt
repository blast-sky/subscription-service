package com.astrog.subscriptionservice.model.dto

import com.astrog.subscriptionservice.model.domain.SubscriptionType

data class CreateSubscriptionDto(
    val userId: String,
    val type: SubscriptionType,
    val filters: Set<String>,
)
