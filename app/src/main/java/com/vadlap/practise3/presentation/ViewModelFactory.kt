package com.vadlap.practise3.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vadlap.practise3.data.Api
import com.vadlap.practise3.data.FilterStorage
import com.vadlap.practise3.data.MoviesRepositoryImpl
import com.vadlap.practise3.data.ProfileStorage
import com.vadlap.practise3.data.db.AppDatabase
import com.vadlap.practise3.domain.FavoritesUseCase
import com.vadlap.practise3.domain.GetMovieUseCase
import com.vadlap.practise3.domain.GetMoviesUseCase
import com.vadlap.practise3.presentation.details.DetailsViewModel
import com.vadlap.practise3.presentation.list.ListViewModel
import com.vadlap.practise3.presentation.profile.ProfileViewModel
import com.vadlap.practise3.presentation.settings.SettingsViewModel
import com.vadlap.practise3.presentation.utils.BadgeCache
import com.vadlap.practise3.presentation.watch.WatchViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val database by lazy { AppDatabase.getDatabase(context) }
    private val favoriteDao by lazy { database.favoriteMovieDao() }

    private val api by lazy { Api() }
    
    private val moviesRepository by lazy { MoviesRepositoryImpl(api, favoriteDao) }
    
    private val getMoviesUseCase by lazy { GetMoviesUseCase(moviesRepository) }
    private val getMovieUseCase by lazy { GetMovieUseCase(moviesRepository) }
    private val favoritesUseCase by lazy { FavoritesUseCase(moviesRepository) }
    
    private val filterStorage by lazy { FilterStorage(context) }
    private val badgeCache by lazy { BadgeCache() }
    
    private val profileStorage by lazy { ProfileStorage(context) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(getMoviesUseCase, filterStorage, badgeCache) as T
        }
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(getMovieUseCase, favoritesUseCase) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(filterStorage) as T
        }
        if (modelClass.isAssignableFrom(WatchViewModel::class.java)) {
            return WatchViewModel(favoritesUseCase) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(profileStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
