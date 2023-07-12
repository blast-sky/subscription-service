package com.astrog.subscriptionservice.manager.service

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.dto.SubscriptionDto
import com.astrog.subscriptionservice.manager.model.entity.FilterEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionId
import com.astrog.subscriptionservice.manager.model.exception.SubscriptionAlreadyExistException
import com.astrog.subscriptionservice.manager.repository.SubscriptionRepository

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
) {

    @Transactional
    fun createSubscription(userId: String, subscriptionType: SubscriptionType, filters: Set<String>) {
        val subscriptionEntity = SubscriptionEntity(
            userId = userId,
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
        } catch (ex: DuplicateKeyException) {
            throw SubscriptionAlreadyExistException(userId, subscriptionType)
        }
    }

    @Transactional
    fun removeSubscription(userId: String, subscriptionType: SubscriptionType) {
        subscriptionRepository.deleteById(SubscriptionId(userId, subscriptionType))
    }

    @Transactional(readOnly = true)
    fun getSubscriptionsByUserId(userId: String): List<SubscriptionDto> {
        return subscriptionRepository.findAllByUserId(userId)
            .map {
                SubscriptionDto(
                    it.userId,
                    it.subscriptionType,
                    it.filters.map(FilterEntity::string).toSet(),
                    it.createdAt
                )
            }
    }
}