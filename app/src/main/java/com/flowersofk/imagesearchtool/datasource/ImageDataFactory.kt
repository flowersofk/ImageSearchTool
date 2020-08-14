package com.flowersofk.imagesearchtool.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.http.ImageSearchService

/**
 * DataSource 생성 역할
 */
class ImageDataFactory (
    private val query: String,
    private val sort: String,
    private val filter: String,
    private val service: ImageSearchService
) : DataSource.Factory<Int, ImageDoc>() {

    val mutableLiveData: MutableLiveData<ImageDataSource> = MutableLiveData<ImageDataSource>()

    private var imageDataSource: ImageDataSource? = null

    override fun create(): DataSource<Int, ImageDoc> {

        imageDataSource = ImageDataSource(query, sort, filter, service)
        mutableLiveData.postValue(imageDataSource)

        return imageDataSource!!

    }

}