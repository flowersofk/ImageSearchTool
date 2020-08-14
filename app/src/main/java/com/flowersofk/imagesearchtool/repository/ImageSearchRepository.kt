package com.flowersofk.imagesearchtool.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.flowersofk.imagesearchtool.MainActivity.Companion.PAGE_SIZE
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.data.ImageSearchRequestData
import com.flowersofk.imagesearchtool.data.ImageSearchResult
import com.flowersofk.imagesearchtool.datasource.ImageDataFactory
import com.flowersofk.imagesearchtool.http.ImageSearchService
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ImageSearchRepository {

    private val service: ImageSearchService = ImageSearchService.create()
    private val executor: Executor = Executors.newFixedThreadPool(5)

    /**
     * Search repositories whose names match the query.
     */
    fun search(request: ImageSearchRequestData): ImageSearchResult {
//        Log.d("GithubRepository", "New query: $query")

        // 라이브 데이터 팩토리 생성
        val dataSourceFactory = ImageDataFactory(request.query, request.sort, request.filter, service)

        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)                 // 페이지 기본 사이즈
            .setInitialLoadSizeHint(PAGE_SIZE)      // 최초 호출시 사이즈
            .setPrefetchDistance(5)                 // 호출 시점이 맨 마지막 행에서 얼마만큼 떨어져있는지 설정(ex. 10이면 맨 마지막에서 위로 10번재 스크롤시점에 호출
            .setEnablePlaceholders(true)
            .build()

        // 라이브 데이터 생성
        val data: LiveData<PagedList<ImageDoc>> = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .setFetchExecutor(executor)
            .build()

        val networkErrors = Transformations.switchMap(dataSourceFactory.mutableLiveData, { dataSource -> dataSource.networkErrors })

        val collections = Transformations.switchMap(dataSourceFactory.mutableLiveData, { dataSource -> dataSource.collections })


        return ImageSearchResult(
            data,
            networkErrors,
            collections
        )

    }

}