package com.astrog.subscriptionservice.manager.model.entity

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class SubscriptionId(
    val id: String,
    val subscriptionType: SubscriptionType,
) : Serializable