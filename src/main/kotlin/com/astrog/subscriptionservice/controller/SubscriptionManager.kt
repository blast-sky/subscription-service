package com.astrog.subscriptionservice.controller

import com.astrog.subscriptionservice.model.domain.SubscriptionType
import com.astrog.subscriptionservice.model.dto.CreateSubscriptionDto
import com.astrog.subscriptionservice.service.SubscriptionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subscriptions")
@Validated
class SubscriptionManager(
    private val subscriptionService: SubscriptionService,
) {

    @PostMapping("create")
    @Operation(summary = "Create subscription")
    fun createSubscription(@RequestBody createSubscriptionDto: CreateSubscriptionDto) {
        subscriptionService.createSubscription(
            createSubscriptionDto.userId,
            createSubscriptionDto.type,
            createSubscriptionDto.filters,
        )
    }

    @DeleteMapping("{userId}/{type}")
    @Operation(summary = "Remove subscription")
    fun removeSubscription(@PathVariable userId: String, @PathVariable type: SubscriptionType) {
        subscriptionService.removeSubscription(userId, type)
    }
}