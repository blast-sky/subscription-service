package com.astrog.subscriptionservice.manager.model.exception

import com.astrog.subscriptionservice.manager.model.domain.SubscriptionType

class SubscriptionAlreadyExistException(userId: String, subscriptionType: SubscriptionType) :
    RuntimeException("Subscription already exist (userId = $userId and subscriptionType = $subscriptionType).")

class UnsupportedSubscriptionTypeException(source: String) :
    RuntimeException("Unsupported subscription type ($source).")