package com.astrog.subscriptionservice.model.dto

import com.astrog.subscriptionservice.model.domain.SubscriptionType

data class RemoveSubscriptionDto(
    val userId: String,
    val type: SubscriptionType,
)
