package com.astrog.subscriptionservice.rabbitfilter.model.response

import com.astrog.subscriptionservice.rabbitfilter.model.receive.Vacancy

data class UserInfoWithVacancy(
    val userInfo: String,
    val vacancy: Vacancy,
)
