package com.flowersofk.imagesearchtool.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class ImageSearchResult (

    val data: LiveData<PagedList<ImageDoc>>,
    val networkErrors: LiveData<String>,
    val collections: LiveData<List<String>>

)