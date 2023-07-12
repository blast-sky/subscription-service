package com.astrog.subscriptionservice.manager.controller

import com.astrog.subscriptionservice.manager.model.exception.SubscriptionAlreadyExistException
import com.astrog.subscriptionservice.manager.model.exception.UnsupportedSubscriptionTypeException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleSubscriptionAlreadyExist(ex: SubscriptionAlreadyExistException): ResponseEntity<String> {
        logger.warn(ex.stackTraceToString())
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.message)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        logger.info { ex.stackTraceToString() }
        val privateSafeReason = getReasonByContainedWords(ex.message)

        return ResponseEntity
            .badRequest()
            .body(privateSafeReason)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnsupportedSubscriptionTypeException(ex: UnsupportedSubscriptionTypeException): ResponseEntity<String> {
        logger.info { ex.stackTraceToString() }
        return ResponseEntity
            .badRequest()
            .body(ex.message)
    }

    private fun getReasonByContainedWords(message: String?): String {
        if (message.isNullOrBlank()) {
            return ""
        }

        return when {
            message.contains("Cannot deserialize value of type `com.astrog.subscriptionservice.model.domain.SubscriptionType` from String") ->
                message.replace("com.astrog.subscriptionservice.model.domain.SubscriptionType", "SubscriptionType")

            message == "Instantiation of [simple type, class com.astrog.subscriptionservice.model.dto.CreateSubscriptionDto] value failed for JSON property filters due to missing (therefore NULL) value for creator parameter filters which is a non-nullable type" ->
                message.replace(
                    "[simple type, class com.astrog.subscriptionservice.model.dto.CreateSubscriptionDto]",
                    "CreateSubscriptionDto"
                )


            else -> message
        }
    }
}