package com.astrog.subscriptionservice.rabbitfilter.model.receive

import com.fasterxml.jackson.annotation.JsonProperty

data class LogoUrls(
    @JsonProperty("90")
    val nineteen: String,

    @JsonProperty("240")
    val twoHundred: String,

    val original: String,
)