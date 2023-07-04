package com.astrog.subscriptionservice.repository

import com.astrog.subscriptionservice.model.entity.SubscriptionEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface SubscriptionRepository : CrudRepository<SubscriptionEntity, String> {

    @Query("SELECT * FROM subscription sub JOIN filter ON sub.id = filter.subscription_id AND :title CONTAINS filter.string")
    fun findSatisfiedSubscriptionsByFilters(title: String): Set<SubscriptionEntity>
}