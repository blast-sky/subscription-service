package com.astrog.subscriptionservice.rabbitfilter.repository

import com.astrog.subscriptionservice.manager.model.entity.FilterEntity
import org.springframework.data.repository.CrudRepository

interface FilterEntityRepository : CrudRepository<FilterEntity, Long>