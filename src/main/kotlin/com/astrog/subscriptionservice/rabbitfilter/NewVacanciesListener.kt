package com.astrog.subscriptionservice.rabbitfilter

import com.astrog.subscriptionservice.manager.repository.SubscriptionRepository
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Vacancy
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class NewVacanciesListener(
    private val subscriptionRepository: SubscriptionRepository,
    private val userInfoWithVacancyRabbitSender: UserInfoWithVacancyRabbitSender,
) {

    @RabbitListener(queues = [NEW_VACANCIES_QUEUE_NAME])
    fun notifyQueueOnNewVacancy(@Payload vacancy: Vacancy) {
        val keywords = setOfNotNull(
            vacancy.name,
            vacancy.snippet?.requirement,
            vacancy.snippet?.responsibility,
        )

        val subscriptions = keywords.flatMap { keyword ->
            subscriptionRepository.findSatisfiedSubscriptionsByFilters(keyword)
        }.toSet()

        subscriptions.forEach { subscription ->
            userInfoWithVacancyRabbitSender.send(subscription.id, subscription.subscriptionType, vacancy)
        }
    }

    companion object {
        const val NEW_VACANCIES_QUEUE_NAME = "vacancies.new"
    }
}