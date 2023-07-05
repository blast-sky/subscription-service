package com.astrog.subscriptionservice.controller

import com.astrog.subscriptionservice.model.exception.SubscriptionAlreadyExistException
import com.astrog.subscriptionservice.model.exception.UnsupportedSubscriptionTypeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleSubscriptionAlreadyExist(ex: SubscriptionAlreadyExistException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ex.message)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        val privateSafeReason = getReasonByContainedWords(ex.message)

        return ResponseEntity
            .badRequest()
            .body(privateSafeReason)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnsupportedSubscriptionTypeException(ex: UnsupportedSubscriptionTypeException): ResponseEntity<String> {
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