package com.astrog.subscriptionservice

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.dto.CreateSubscriptionDto
import com.astrog.subscriptionservice.manager.model.dto.RemoveSubscriptionDto
import java.util.UUID

val randomString: String
    get() = UUID.randomUUID().toString()

val randomCreateSubscriptionDto: CreateSubscriptionDto
    get() = CreateSubscriptionDto(
        userId = randomString,
        subscriptionType = SubscriptionType.TELEGRAM,
        filters = emptySet(),
    )

val randomRemoveSubscriptionDto: RemoveSubscriptionDto
    get() = RemoveSubscriptionDto(
        userId = randomString,
        type = SubscriptionType.TELEGRAM,
    )