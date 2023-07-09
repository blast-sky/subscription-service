package com.astrog.subscriptionservice.manager.model.entity

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
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
@IdClass(SubscriptionId::class)
data class SubscriptionEntity(

    @Id
    val id: String,

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    val subscriptionType: SubscriptionType,

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        orphanRemoval = true,
        mappedBy = "subscription",
    )
    val filters: MutableList<FilterEntity>,
) {

    override fun hashCode(): Int {
        return 42
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is SubscriptionEntity) {
            return false
        }

        return id == other.id && subscriptionType == other.subscriptionType
    }
}
