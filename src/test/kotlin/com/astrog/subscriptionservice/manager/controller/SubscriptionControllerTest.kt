package com.astrog.subscriptionservice.manager.controller

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType
import com.astrog.subscriptionservice.manager.model.exception.SubscriptionAlreadyExistException
import com.astrog.subscriptionservice.randomCreateSubscriptionDto
import com.astrog.subscriptionservice.randomRemoveSubscriptionDto
import com.astrog.subscriptionservice.manager.service.SubscriptionService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SubscriptionControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var subscriptionService: SubscriptionService

    @Nested
    inner class CreateTest {

        @Test
        fun `create Must return ok When ok`() {
            val createSubscriptionDto = randomCreateSubscriptionDto

            every {
                subscriptionService.createSubscription(
                    userId = createSubscriptionDto.userId,
                    subscriptionType = createSubscriptionDto.subscriptionType,
                    filters = createSubscriptionDto.filters,
                )
            } returns Unit

            mockMvc.post("/subscriptions/create") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(createSubscriptionDto)
            }
                .andDo { print() }
                .andExpectAll {
                    status { isOk() }
                }

            verify {
                subscriptionService.createSubscription(
                    userId = createSubscriptionDto.userId,
                    subscriptionType = createSubscriptionDto.subscriptionType,
                    filters = createSubscriptionDto.filters,
                )
            }
        }

        @Test
        fun `create Must return CONFLICT When subscription already exist`() {
            val createSubscriptionDto = randomCreateSubscriptionDto

            every {
                subscriptionService.createSubscription(
                    userId = createSubscriptionDto.userId,
                    subscriptionType = createSubscriptionDto.subscriptionType,
                    filters = createSubscriptionDto.filters,
                )
            } throws SubscriptionAlreadyExistException(
                createSubscriptionDto.userId,
                createSubscriptionDto.subscriptionType,
            )

            mockMvc.post("/subscriptions/create") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(createSubscriptionDto)
            }
                .andDo { print() }
                .andExpectAll {
                    status { isConflict() }
                }

            verify {
                subscriptionService.createSubscription(
                    userId = createSubscriptionDto.userId,
                    subscriptionType = createSubscriptionDto.subscriptionType,
                    filters = createSubscriptionDto.filters,
                )
            }
        }

        @Test
        fun `create Must return BAD_REQUEST When subscription type is not valid`() {
            val notValidContent = """
                {
                    "userId": "123",
                    "type": "mms",
                    "filters": []
                }
            """.trimIndent()

            val expectedResult =
                "JSON parse error: Cannot deserialize value of type `SubscriptionType` from String \"mms\": not one of the values accepted for Enum class: [sms, email, telegram]"

            mockMvc.post("/subscriptions/create") {
                contentType = MediaType.APPLICATION_JSON
                content = notValidContent
            }
                .andDo { print() }
                .andExpectAll {
                    status { isBadRequest() }
                    //content { string(expectedResult) }
                }

            verify(exactly = 0) {
                subscriptionService.createSubscription(
                    userId = any(),
                    subscriptionType = any(),
                    filters = any(),
                )
            }
        }

        @Test
        fun `create Must return BAD_REQUEST When request body is not valid`() {
            val notValidContent = """
                {
                    "userId": 123,
                    "type": "telegram"
                }
            """.trimIndent()

            mockMvc.post("/subscriptions/create") {
                contentType = MediaType.APPLICATION_JSON
                content = notValidContent
            }
                .andDo { print() }
                .andExpectAll {
                    status { isBadRequest() }
                    content { string(containsString(JSON_PARSE_ERROR)) }
                }

            verify(exactly = 0) {
                subscriptionService.createSubscription(
                    userId = any(),
                    subscriptionType = any(),
                    filters = any(),
                )
            }
        }
    }

    @Nested
    inner class RemoveTest {

        @Test
        fun `remove Must return ok When ok`() {
            val removeSubscriptionDto = randomRemoveSubscriptionDto.copy(type = SubscriptionType.TELEGRAM)

            every {
                subscriptionService.removeSubscription(
                    userId = removeSubscriptionDto.userId,
                    subscriptionType = removeSubscriptionDto.type,
                )
            } returns Unit

            mockMvc.delete("/subscriptions/${removeSubscriptionDto.userId}/telegram")
                .andDo { print() }
                .andExpectAll {
                    status { isOk() }
                }

            verify {
                subscriptionService.removeSubscription(
                    userId = removeSubscriptionDto.userId,
                    subscriptionType = removeSubscriptionDto.type,
                )
            }
        }

        @Test
        fun `remove Must return NOT_FOUND When request body is not valid`() {
            mockMvc.delete("/subscriptions//sms")
                .andDo { print() }
                .andExpectAll {
                    status { isNotFound() }
                }

            verify(exactly = 0) {
                subscriptionService.createSubscription(
                    userId = any(),
                    subscriptionType = any(),
                    filters = any(),
                )
            }
        }
    }

    companion object {
        const val JSON_PARSE_ERROR = "JSON parse error"
    }
}