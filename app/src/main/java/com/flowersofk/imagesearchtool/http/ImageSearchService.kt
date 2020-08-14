package com.flowersofk.imagesearchtool.http

import android.util.Log
import com.flowersofk.imagesearchtool.data.ImageDoc
import com.flowersofk.imagesearchtool.data.ImageSearchResponse
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val TAG = "ImageSearchService"

interface ImageSearchService{

    @Headers("Authorization: KakaoAK " + API_KEY)
    @GET("v2/search/image")
    fun getImageRepos(
        @Query("query") keyword: String,
        @Query("sort") sort: String?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ImageSearchResponse>

    companion object {

        private const val BASE_URL = "https://dapi.kakao.com/"
        private const val API_KEY = "20637b9fc9479ca3d7c56c58632be137"

        fun create(): ImageSearchService {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImageSearchService::class.java)

        }

    }

}

/**
 * 이미지검색 API 호출 정의
 */
fun getImageRepos(
    service: ImageSearchService,
    keyword: String,
    sort: String,
    filter: String,
    page: Int,
    size: Int,
    onSuccess: (isEnd: Boolean, response: List<ImageDoc>) -> Unit,
    onFailure: (error: String) -> Unit) {

    service.getImageRepos(keyword, sort, page, size).enqueue(
        object : Callback<ImageSearchResponse> {
            override fun onFailure(call: Call<ImageSearchResponse>, t: Throwable) {
                Log.d(TAG, "fail to get data")
                onFailure(t.message ?: "unknown error")
            }

            override fun onResponse(call: Call<ImageSearchResponse>, @NotNull response: Response<ImageSearchResponse>) {
                Log.d(TAG, "got a response $response")

                if(response != null && response.isSuccessful) {

                    val isEnd = response.body()?.meta?.is_end ?: true  // null이 들어올 경우 더이상 호출 안함
                    var repos = response.body()?.documents ?: mutableListOf()

                    Log.i(TAG, "filter : " + filter)

                    // 필터값이 있을경우 호출한 데이터에서 필터검색 (전체 : "")
                    if(!filter.equals("All")) {

                        var filteredList: MutableList<ImageDoc> = mutableListOf()

                        for(data in repos) {
                            if(filter.equals(data.collection)) {
                                filteredList.add(data)
                            }
                        }
                        onSuccess(isEnd, filteredList)
                    } else {
                        onSuccess(isEnd, repos)
                    }

                } else {

                    onFailure(response.errorBody()?.string() ?: "Unknown error")

                }
            }

        })

}