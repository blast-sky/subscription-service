package com.astrog.subscriptionservice.rabbitfilter.model.receive

data class Salary(
    val currency: String,
    val gross: Boolean,
    val from: Int,
    val to: Int,
)