package com.astrog.subscriptionservice.manager.model.entity

import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinColumns
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "filter", indexes = [Index(columnList = "string")])
class FilterEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long? = null,

    @Column(nullable = false)
    val string: String,

    @ManyToOne
    @JoinColumns(
        JoinColumn(name = "subscription_id", nullable = false, referencedColumnName = "user_id"),
        JoinColumn(name = "subscription_type", nullable = false, referencedColumnName = "subscription_type"),
    )
    val subscription: SubscriptionEntity,
) : BaseEntity<Long?>()
