package com.michaelmagdy.themoviesapp.model.webservice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.michaelmagdy.themoviesapp.util.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource (private val apiService : ApiInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Result>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Result>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
                apiService.getPopularMovie(page)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    callback.onResult(it.results, null, page+1)
                                    networkState.postValue(NetworkState.LOADED)
                                },
                                {
                                    networkState.postValue(NetworkState.ERROR)
                                    Log.e("MovieDataSource", it.message.toString())
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
                apiService.getPopularMovie(params.key)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    if(it.totalPages >= params.key) {
                                        callback.onResult(it.results, params.key+1)
                                        networkState.postValue(NetworkState.LOADED)
                                    }
                                    else{
                                        networkState.postValue(NetworkState.ENDOFLIST)
                                    }
                                },
                                {
                                    networkState.postValue(NetworkState.ERROR)
                                    Log.e("MovieDataSource", it.message.toString())
                                }
                        )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {

    }
}