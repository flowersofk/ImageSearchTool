package com.flowersofk.imagesearchtool.data

import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.data.ImageMeta

/**
 * Image Json Model
 */
data class ImageSearchResponse (

    val meta : ImageMeta,
    val documents : MutableList<ImageDoc>

)