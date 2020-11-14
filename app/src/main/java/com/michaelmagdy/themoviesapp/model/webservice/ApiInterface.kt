package com.michaelmagdy.themoviesapp.model.webservice

import com.michaelmagdy.themoviesapp.model.webservice.MoviesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET("discover/movie")
    fun getPopularMovie(@Query("page") page: Int): Single<MoviesApiResponse>
}