## Endpoints
- GET /subscriptions/{userId}
- DELETE /subscriptions/{userId}/{subscriptionType}
- POST /subscriptions/create
@RequestBody{
  userID,
  subscriptionType,
  filters
}

## Rabbit mq
#### Listened queues
- "hhsva.service.vacancies.new"
#### Write to
- { exchange: "hhsva.service", routing_key: "announcer.telegram" }
