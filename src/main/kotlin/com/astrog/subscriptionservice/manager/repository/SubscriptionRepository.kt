package com.astrog.subscriptionservice.manager.repository

import com.astrog.subscriptionservice.manager.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : CrudRepository<SubscriptionEntity, SubscriptionId> {

    // native SQL - "SELECT sub.id, sub.subscription_type FROM subscription sub JOIN filter ON sub.id = filter.subscription_id AND LOWER(:title) LIKE '%' || filter.string || '%'"
    // JPQL - "SELECT sub FROM SubscriptionEntity sub, FilterEntity filter WHERE sub = filter.subscription AND LOWER(:title) LIKE '%' || filter.string || '%'"
    @Query(
        "SELECT sub FROM SubscriptionEntity sub, FilterEntity filter WHERE sub = filter.subscription AND LOWER(:title) LIKE '%' || filter.string || '%'",
    )
    fun findSatisfiedSubscriptionsByFilters(title: String): Set<SubscriptionEntity>

    fun findAllByUserId(userId: String): List<SubscriptionEntity>
}