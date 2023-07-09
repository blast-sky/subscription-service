package com.astrog.subscriptionservice

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.dto.CreateSubscriptionDto
import com.astrog.subscriptionservice.manager.model.dto.RemoveSubscriptionDto
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Employer
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Experience
import com.astrog.subscriptionservice.rabbitfilter.model.receive.LogoUrls
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Region
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Salary
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Snippet
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Type
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Vacancy
import java.util.UUID

val randomString: String
    get() = UUID.randomUUID().toString()

val randomCreateSubscriptionDto: CreateSubscriptionDto
    get() = CreateSubscriptionDto(
        userId = randomString,
        subscriptionType = SubscriptionType.TELEGRAM,
        filters = emptySet(),
    )

val randomRemoveSubscriptionDto: RemoveSubscriptionDto
    get() = RemoveSubscriptionDto(
        userId = randomString,
        type = SubscriptionType.TELEGRAM,
    )

val randomVacancy: Vacancy
    get() = Vacancy(
        acceptIncompleteResumes = true,
        alternateURL = randomString,
        applyAlternateURL = randomString,
        hasTest = false,
        professionalRoles = emptyList(),
        publishedAt = randomString,
        responseLetterRequired = false,
        responseURL = randomString,
        sortPointDistance = 0.0,
        id = randomString,
        type = Type(id = "0", name = randomString),
        url = randomString,
        name = randomString,
        area = Region(randomString, randomString, randomString),
        salary = Salary(
            currency = randomString,
            gross = false,
            from = 0,
            to = 0,
        ),
        snippet = Snippet(requirement = null, responsibility = null),
        employer = Employer(
            accreditedIdEmployer = false,
            alternateURL = randomString,
            logoUrls = LogoUrls(randomString, randomString, randomString),
            vacanciesUrl = null,
            id = null,
            name = randomString,
            trusted = false,
            url = randomString,
        ),
        schedule = null,
        experience = Experience(id = "0", name = randomString)
    )