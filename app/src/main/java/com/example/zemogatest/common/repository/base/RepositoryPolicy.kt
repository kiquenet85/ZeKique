package com.cornershop.counterstest.common.repository.base

interface RepositoryPolicy<Info> {
    suspend fun shouldGoRemote(info: Info): Boolean = false
}