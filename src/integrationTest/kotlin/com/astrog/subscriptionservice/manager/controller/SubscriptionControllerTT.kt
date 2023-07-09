package com.astrog.subscriptionservice.manager.controller

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.entity.FilterEntity
import com.astrog.subscriptionservice.manager.model.entity.SubscriptionId
import com.astrog.subscriptionservice.manager.repository.SubscriptionRepository
import com.astrog.subscriptionservice.randomCreateSubscriptionDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql(
    statements = [
        "DELETE FROM filter",
        "DELETE FROM subscription",
    ],
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
)
class SubscriptionControllerTT(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val subscriptionRepository: SubscriptionRepository,
) {

    @Test
    fun `subscriptions-create Must save new subscription to db When data is valid`() {
        val createSubscriptionDto = randomCreateSubscriptionDto
            .copy(
                subscriptionType = SubscriptionType.TELEGRAM,
                filters = setOf("asd", "fgh", "jkl"),
            )

        val subscriptionId = SubscriptionId(
            id = createSubscriptionDto.userId,
            subscriptionType = createSubscriptionDto.subscriptionType,
        )

        // subscription is not in db already
        assertFalse(subscriptionRepository.existsById(subscriptionId))

        mockMvc.post("/subscriptions/create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createSubscriptionDto)
        }
            .andDo { print() }
            .andExpect { status { isOk() } }

        val subscriptionEntity = subscriptionRepository.findByIdOrNull(subscriptionId)

        assertNotNull(subscriptionEntity)
        checkNotNull(subscriptionEntity)

        assertEquals(SubscriptionType.TELEGRAM, subscriptionEntity.subscriptionType)
        assertEquals(3, subscriptionEntity.filters.size)
        assertTrue(subscriptionEntity.filters.map(FilterEntity::string).containsAll(createSubscriptionDto.filters))
        assertTrue(
            subscriptionRepository.findSatisfiedSubscriptionsByFilters("asdf123asdffasdf")
                .contains(subscriptionEntity)
        )
    }

    @Test
    fun `subscriptions-remove Must remove subscription created with subscription-create When data is valid`() {
        assertEquals(0, subscriptionRepository.count())

        val createSubscriptionDto = randomCreateSubscriptionDto
            .copy(
                subscriptionType = SubscriptionType.TELEGRAM,
                filters = setOf("asd", "fgh", "jkl"),
            )

        val subscriptionId = SubscriptionId(
            id = createSubscriptionDto.userId,
            subscriptionType = createSubscriptionDto.subscriptionType,
        )

        mockMvc.post("/subscriptions/create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createSubscriptionDto)
        }
            .andDo { print() }
            .andExpect { status { isOk() } }

        mockMvc.delete("/subscriptions/${createSubscriptionDto.userId}/telegram")
            .andDo { print() }
            .andExpect { status { isOk() } }

        assertFalse(subscriptionRepository.existsById(subscriptionId))
        assertTrue(subscriptionRepository.findSatisfiedSubscriptionsByFilters("asdf123asdffasdf").isEmpty())
    }

    companion object {

        @Container
        @JvmStatic
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.3")
            .withDatabaseName("integration-tests-db")
            .withUsername("db")
            .withPassword("db")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }
}