package com.flowersofk.imagesearchtool.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.http.ImageSearchService
import com.flowersofk.imagesearchtool.http.getImageRepos

class ImageDataSource (
    private val query: String,
    private val sort: String,
    private val filter: String,
    private val service: ImageSearchService
) : PageKeyedDataSource<Int, ImageDoc>() {

    companion object {
        const val TAG = "ImageDataSource"
    }

    val networkErrors: MutableLiveData<String> = MutableLiveData()

    private var filters = mutableListOf<String>()
    var collections: MutableLiveData<List<String>> = MutableLiveData()

    private var isEnd = false

    // 리스트뷰 최초 호출
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ImageDoc>) {

        val curPage = 1
        val nextPage = curPage + 1

        getImageRepos(service, query, sort, filter, curPage, params.requestedLoadSize, { is_End, repos ->

            isEnd = is_End       // 마지막페이지 여부

            setCollections(true, repos)

            // onResult(@NonNull List<Value> data, @Nullable Key previousPageKey, @Nullable Key nextPageKey)
            callback.onResult(repos, null, nextPage)

        }, { error ->

            networkErrors.postValue(error)

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ImageDoc>) {

        if(!isEnd) {

            Log.i(TAG, "Loading key: ${params.key}, count: ${params.requestedLoadSize}")

            getImageRepos(service, query, sort, filter, params.key, params.requestedLoadSize, { is_End, repos ->

                isEnd = is_End       // 마지막페이지 여부
                val nextKey = params.key + 1

                setCollections(false, repos)

                callback.onResult(repos, nextKey)

            }, { error ->

                networkErrors.postValue(error)

            })

        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ImageDoc>
    ) {
    }

    /**
     * Collection 목록
     */
    fun setCollections(isFirstLoad: Boolean, list: List<ImageDoc>) {

        if(isFirstLoad) {
            filters = mutableListOf<String>()
            filters.add("All")
        }

        for(data in list) {
            filters.add(data.collection)
        }

        collections.postValue(filters.distinct())

    }
}