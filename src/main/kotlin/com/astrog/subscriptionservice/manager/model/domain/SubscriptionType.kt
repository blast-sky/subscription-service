package com.astrog.subscriptionservice.manager.model.domain

import com.fasterxml.jackson.annotation.JsonProperty

enum class SubscriptionType {

    @JsonProperty("telegram")
    TELEGRAM,

    @JsonProperty("sms")
    SMS,

    @JsonProperty("email")
    EMAIL,
}