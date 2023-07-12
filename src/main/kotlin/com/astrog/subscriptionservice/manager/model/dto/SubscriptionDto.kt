package com.astrog.subscriptionservice.manager.model.dto

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import java.time.OffsetDateTime

data class SubscriptionDto(
    val userId: String,
    val subscriptionType: SubscriptionType,
    val filters: Set<String>,
    val createdAt: OffsetDateTime,
)