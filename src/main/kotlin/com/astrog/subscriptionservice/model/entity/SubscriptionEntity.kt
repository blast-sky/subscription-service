package com.astrog.subscriptionservice.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "subscription",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["subscription_type_id", "id"]),
    ],
)
data class SubscriptionEntity(

    @Id
    val id: String,

    @ManyToOne
    @JoinColumn(name = "subscription_type_id", nullable = false)
    val subscriptionType: SubscriptionTypeEntity,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "subscription_id")
    val filters: Set<FilterEntity>,
)
