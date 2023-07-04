package com.astrog.subscriptionservice.service

import com.astrog.subscriptionservice.model.domain.SubscriptionType
import com.astrog.subscriptionservice.model.entity.FilterEntity
import com.astrog.subscriptionservice.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.model.entity.SubscriptionTypeEntity
import com.astrog.subscriptionservice.repository.SubscriptionRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
) {

    fun createSubscription(userId: String, subscriptionType: SubscriptionType, filters: Set<String>) {
        val subscriptionId = createSubscriptionId(userId, subscriptionType)
        val subscriptionEntity = SubscriptionEntity(
            id = subscriptionId,
            subscriptionType = SubscriptionTypeEntity(type = subscriptionType),
            filters = filters.map { FilterEntity(string = it) }.toSet(),
        )
        subscriptionRepository.save(subscriptionEntity)
    }

    fun removeSubscription(userId: String, subscriptionType: SubscriptionType) {
        val subscriptionId = createSubscriptionId(userId, subscriptionType)
        subscriptionRepository.deleteById(subscriptionId)
    }

    fun findSubscriptionsWithSatisfiedFilter(title: String): Set<SubscriptionEntity> {
        return subscriptionRepository.findSatisfiedSubscriptionsByFilters(title)
    }

    private fun createSubscriptionId(userId: String, subscriptionType: SubscriptionType): String {
        if (subscriptionType == SubscriptionType.TELEGRAM) {
            return "$userId:$subscriptionType"
        }
        error("Not implemented yet")
    }
}