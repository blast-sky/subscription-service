package com.astrog.subscriptionservice.manager.model.dto

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import jakarta.validation.constraints.NotBlank

data class CreateSubscriptionDto(
    @NotBlank
    val userId: String,
    val subscriptionType: SubscriptionType,
    val filters: Set<String>,
)
