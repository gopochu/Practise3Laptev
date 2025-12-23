package com.vadlap.practise3.data

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Api {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val apiKey = "6fd53cdb" 

    // Обновленный метод поиска с поддержкой фильтров
    suspend fun searchMovies(title: String, type: String = "", year: String = ""): List<SearchItemDto> {
        val url = "http://www.omdbapi.com/"
        return try {
            Log.d("API_DEBUG", "Searching: $url?s=$title&type=$type&y=$year&apikey=$apiKey")
            
            // Получаем ответ поиска
            val response: SearchResponseDto = client.get(url) {
                parameter("s", title)
                if (type.isNotEmpty()) parameter("type", type)
                if (year.isNotEmpty()) parameter("y", year)
                parameter("apikey", apiKey)
            }.body()

            // Если поиск успешен и список не пуст, возвращаем его
            if (response.response == "True" && response.search != null) {
                Log.d("API_DEBUG", "Found ${response.search.size} items")
                response.search
            } else {
                Log.d("API_DEBUG", "Search failed or empty: ${response.error}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Error searching: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getMovie(id: String): MovieResponseModel? {
        val url = "http://www.omdbapi.com/"
        return try {
            Log.d("API_DEBUG", "Requesting: $url?i=$id&apikey=$apiKey")
            val response: MovieResponseModel = client.get(url) {
                parameter("i", id)
                parameter("apikey", apiKey)
            }.body()
            
            // Если OMDb вернул ошибку, вернем null
            if (response.response == "False") return null

            Log.d("API_DEBUG", "Success: ${response.title}")
            response
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Error fetching $id: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}
