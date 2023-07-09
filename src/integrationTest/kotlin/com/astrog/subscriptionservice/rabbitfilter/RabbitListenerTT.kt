package com.astrog.subscriptionservice.rabbitfilter

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.repository.SubscriptionRepository
import com.astrog.subscriptionservice.manager.service.SubscriptionService
import com.astrog.subscriptionservice.rabbitfilter.configuration.RabbitListenerTestConfiguration
import com.astrog.subscriptionservice.rabbitfilter.model.response.UserInfoWithVacancy
import com.astrog.subscriptionservice.rabbitfilter.repository.FilterEntityRepository
import com.astrog.subscriptionservice.randomString
import com.astrog.subscriptionservice.randomVacancy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    properties = [
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    ],
    classes = [RabbitListenerTestConfiguration::class],
)
@AutoConfigureTestDatabase
@Testcontainers
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RabbitListenerTT(
    private val subscriptionRepository: SubscriptionRepository,
    private val filterEntityRepository: FilterEntityRepository,
    private val rabbitTemplate: RabbitTemplate,
    private val rabbitAdmin: RabbitAdmin,
    private val subscriptionService: SubscriptionService,
) {

    @AfterEach
    fun clearDatabase() {
        subscriptionRepository.deleteAll()
        filterEntityRepository.deleteAll()
    }

    @AfterEach
    fun clearAllQueues() {
        rabbitAdmin.purgeQueue(NewVacanciesListener.NEW_VACANCIES_QUEUE_NAME)
        rabbitAdmin.purgeQueue(RabbitListenerTestConfiguration.TELEGRAM_ANNOUNCER_QUEUE_NAME)
    }

    @Test
    fun `notifyQueueOnNewVacancy Must notify When have satisfied filters`() {
        val textForFilter = "java dev"
        val newVacancy = randomVacancy.copy(name = randomString + textForFilter + randomString)
        val userId = randomString
        val subscriptionType = SubscriptionType.TELEGRAM

        subscriptionService.createSubscription(userId, subscriptionType, setOf(textForFilter))

        rabbitTemplate.convertAndSend(NewVacanciesListener.NEW_VACANCIES_QUEUE_NAME, newVacancy)

        val userInfoWithVacancy = rabbitTemplate.receiveAndConvert(
            RabbitListenerTestConfiguration.TELEGRAM_ANNOUNCER_QUEUE_NAME,
            Long.MAX_VALUE,
            object : ParameterizedTypeReference<UserInfoWithVacancy>() {}
        )

        assertEquals(UserInfoWithVacancy(userId, newVacancy), userInfoWithVacancy)
    }

    companion object {

        @JvmStatic
        @Container
        val rabbitmqContainer = RabbitMQContainer("rabbitmq:3-management")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.rabbitmq.host", rabbitmqContainer::getHost)
            registry.add("spring.rabbitmq.port", rabbitmqContainer::getAmqpPort)
        }
    }
}