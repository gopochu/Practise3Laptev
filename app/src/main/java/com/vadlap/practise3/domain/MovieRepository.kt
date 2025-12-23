package com.vadlap.practise3.domain

interface MovieRepository {
    suspend fun getCinemaList(): List<MovieModel>
}