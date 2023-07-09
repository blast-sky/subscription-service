package com.astrog.subscriptionservice.rabbitfilter.configuration

import com.astrog.subscriptionservice.rabbitfilter.NewVacanciesListener
import com.astrog.subscriptionservice.rabbitfilter.UserInfoWithVacancyRabbitSender
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Declarables
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean


@TestConfiguration
class RabbitListenerTestConfiguration {

    @Bean
    fun newVacanciesBinding(): Queue {
        return Queue(NewVacanciesListener.NEW_VACANCIES_QUEUE_NAME, NON_DURABLE)
    }

    @Bean
    fun announcerBinding(): Declarables {
        val telegramQueue = Queue(
            TELEGRAM_ANNOUNCER_QUEUE_NAME,
            NON_DURABLE,
        )

        val announcerExchange = DirectExchange(
            UserInfoWithVacancyRabbitSender.VACANCY_ANNOUNCER_EXCHANGE_NAME,
            true,
            DO_NOT_AUTO_DELETE,
        )

        return Declarables(
            telegramQueue,
            announcerExchange,
            BindingBuilder
                .bind(telegramQueue)
                .to(announcerExchange)
                .with(TELEGRAM_BINDING_KEY)
        )
    }

    companion object {
        private const val NON_DURABLE = false
        private const val DO_NOT_AUTO_DELETE = false

        const val TELEGRAM_ANNOUNCER_QUEUE_NAME = "announcer.telegram"
        const val TELEGRAM_BINDING_KEY = "telegram"
    }
}