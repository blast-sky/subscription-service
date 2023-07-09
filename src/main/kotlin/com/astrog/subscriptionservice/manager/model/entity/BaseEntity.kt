package com.astrog.subscriptionservice.manager.model.entity

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity<ID> {

    @get:Id
    abstract val id: ID

    override fun hashCode(): Int {
        return 42
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other::class.java != this::class.java) {
            return false
        }
        other as BaseEntity<*>
        return id == other.id
    }
}