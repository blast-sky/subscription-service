package com.astrog.subscriptionservice.manager.service

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.entity.FilterEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionId
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
        val subscriptionEntity = SubscriptionEntity(
            id = userId,
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
        subscriptionRepository.deleteById(SubscriptionId(userId, subscriptionType))
    }
}