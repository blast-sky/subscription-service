package com.astrog.subscriptionservice.manager.model.entity

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "subscription",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["subscription_type", "id"]),
    ],
)
data class SubscriptionEntity(

    @Id
    val id: String,

    @Column(name = "subscription_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val subscriptionType: SubscriptionType,

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        orphanRemoval = true,
        mappedBy = "subscription",
    )
    val filters: MutableList<FilterEntity>,
)
