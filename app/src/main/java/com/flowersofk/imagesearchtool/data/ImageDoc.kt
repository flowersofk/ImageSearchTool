package com.flowersofk.imagesearchtool.data

/**
 * Image Document Model
 */
data class ImageDoc (

    val collection: String,	        // 컬렉션
    val thumbnail_url: String, 	    // 미리보기 이미지 URL
    val image_url: String,          // 이미지 URL
    val width: Integer,	            // 이미지의 가로 길이
    val height:	Integer,            // 이미지의 세로 길이
    val display_sitename: String,   // 출처
    val doc_url: String,	        // 문서 URL
    val datetime: String	        // 문서 작성시간. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]

)