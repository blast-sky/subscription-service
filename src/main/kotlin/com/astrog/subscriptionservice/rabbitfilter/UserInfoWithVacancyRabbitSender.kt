package com.astrog.subscriptionservice.rabbitfilter

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionEntity
import com.astrog.subscriptionservice.rabbitfilter.model.receive.Vacancy
import com.astrog.subscriptionservice.rabbitfilter.model.response.UserInfoWithVacancy
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.stereotype.Service

@Service
class UserInfoWithVacancyRabbitSender(
    private val rabbitMessageOperations: RabbitMessageOperations,
) {

    fun send(subscriptionId: String, subscriptionType: SubscriptionType, vacancy: Vacancy) {
        rabbitMessageOperations.convertAndSend(
            VACANCY_ANNOUNCER_EXCHANGE_NAME,
            subscriptionTypeToRoutingKey(subscriptionType),
            UserInfoWithVacancy(subscriptionId, vacancy)
        )
    }

    companion object {

        const val VACANCY_ANNOUNCER_EXCHANGE_NAME = "vacancy.announcer"

        fun subscriptionTypeToRoutingKey(type: SubscriptionType): String {
            return when (type) {
                SubscriptionType.TELEGRAM -> "telegram"
                SubscriptionType.SMS -> "sms"
                SubscriptionType.EMAIL -> "email"
            }
        }
    }
}