package com.flowersofk.imagesearchtool.model

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.repository.ImageSearchRepository
import com.flowersofk.imagesearchtool.data.ImageSearchRequestData
import com.flowersofk.imagesearchtool.data.ImageSearchResult

class ImageSearchViewModel : ViewModel(){

    private val repository: ImageSearchRepository =
        ImageSearchRepository()

    private val queryLiveData = MutableLiveData<ImageSearchRequestData>()

    // 'queryLiveData'값이 변경되면 'search(it)'함수를 수행하여 'repoResult'값을 업데이트
    private val repoResult: LiveData<ImageSearchResult>
            = Transformations.map(queryLiveData, { repository.search(it) })

    val repos: LiveData<PagedList<ImageDoc>> = Transformations.switchMap(repoResult, { it -> it.data })

    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult, { it -> it.networkErrors })

    val collectionResponse: LiveData<List<String>> = Transformations.switchMap(repoResult, { it -> it.collections})

    /**
     * Search a repository based on a query string.
     */
    fun searchKeywordRepo(request: ImageSearchRequestData) {
        queryLiveData.postValue(request)
    }

//    private val filter = MutableLiveData<String>()
//    private val filterResult: LiveData<PagedList<ImageDoc>> = Transformations.switchMap(filter, { repos.let { it } })
//
//    /* 기존 검색 결과에서 필터링 */
//    fun searchFilter(filterName: String) {
//        filter.postValue(filterName)
//
//    }

}