package com.vadlap.practise3.domain

class GetMovieUseCase(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(id: String): MovieModel? {
        return moviesRepository.getMovie(id)
    }
}
