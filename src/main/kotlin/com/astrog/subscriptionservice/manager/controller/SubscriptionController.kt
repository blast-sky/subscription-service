package com.astrog.subscriptionservice.manager.controller

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.dto.CreateSubscriptionDto
import com.astrog.subscriptionservice.manager.model.dto.SubscriptionDto
import com.astrog.subscriptionservice.manager.service.SubscriptionService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("subscriptions")
@Validated
class SubscriptionController(
    private val subscriptionService: SubscriptionService,
) {

    @PostMapping("create")
    @Operation(summary = "Create subscription")
    fun createSubscription(@RequestBody createSubscriptionDto: CreateSubscriptionDto) {
        subscriptionService.createSubscription(
            createSubscriptionDto.userId,
            createSubscriptionDto.subscriptionType,
            createSubscriptionDto.filters,
        )
    }

    @DeleteMapping("{userId}/{type}")
    @Operation(summary = "Remove subscription")
    fun removeSubscription(@PathVariable @NotBlank userId: String, @PathVariable type: SubscriptionType) {
        subscriptionService.removeSubscription(userId, type)
    }

    @GetMapping("{userId}")
    @Operation(summary = "Returns active subscriptions for the user (userId)")
    fun getActiveSubscriptions(@PathVariable @NotBlank userId: String): List<SubscriptionDto> {
        return subscriptionService.getSubscriptionsByUserId(userId)
    }
}