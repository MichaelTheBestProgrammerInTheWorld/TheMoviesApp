package com.michaelmagdy.themoviesapp.model.webservice

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : ApiInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Result>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Result> {

        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}