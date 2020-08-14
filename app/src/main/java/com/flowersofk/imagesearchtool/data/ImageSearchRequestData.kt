package com.flowersofk.imagesearchtool.data

/**
 * 이미지 검색 요청 데이터 모델
 */
data class ImageSearchRequestData(
    val query: String,
    val sort: String,
    val filter: String
)