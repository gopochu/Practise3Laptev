package com.vadlap.practise3.domain

class GetMoviesUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(title: String, type: String = "", year: String = ""): List<MovieModel> {
        return moviesRepository.getMovies(title, type, year)
    }
}
