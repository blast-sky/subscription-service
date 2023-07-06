package com.astrog.subscriptionservice.manager.service

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.entity.FilterEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.manager.model.exception.SubscriptionAlreadyExistException
import com.astrog.subscriptionservice.manager.repository.SubscriptionRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
) {

    @Transactional
    fun createSubscription(userId: String, subscriptionType: SubscriptionType, filters: Set<String>) {
        val subscriptionId = createSubscriptionId(userId, subscriptionType)
        val subscriptionEntity = SubscriptionEntity(
            id = subscriptionId,
            subscriptionType = subscriptionType,
            filters = mutableListOf(),
        )
        subscriptionEntity.filters += filters.map { filter ->
            FilterEntity(
                string = filter,
                subscription = subscriptionEntity,
            )
        }

        try {
            subscriptionRepository.save(subscriptionEntity)
        } catch (ex: DataIntegrityViolationException) {
            throw SubscriptionAlreadyExistException(userId, subscriptionType)
        }
    }

    @Transactional
    fun removeSubscription(userId: String, subscriptionType: SubscriptionType) {
        val subscriptionId = createSubscriptionId(userId, subscriptionType)
        subscriptionRepository.deleteById(subscriptionId)
    }

    companion object {

        fun createSubscriptionId(userId: String, subscriptionType: SubscriptionType): String {
            if (subscriptionType == SubscriptionType.TELEGRAM) {
                return "$userId:$subscriptionType"
            }
            error("Not implemented yet")
        }
    }
}