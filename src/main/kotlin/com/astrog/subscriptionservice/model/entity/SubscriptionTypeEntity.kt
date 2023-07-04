package com.astrog.subscriptionservice.model.entity

import com.astrog.subscriptionservice.model.domain.SubscriptionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "subscription_type")
data class SubscriptionTypeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    val type: SubscriptionType,
)