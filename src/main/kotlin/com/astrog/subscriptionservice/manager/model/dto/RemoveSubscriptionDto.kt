package com.astrog.subscriptionservice.manager.model.dto

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType

data class RemoveSubscriptionDto(
    val userId: String,
    val type: SubscriptionType,
)
