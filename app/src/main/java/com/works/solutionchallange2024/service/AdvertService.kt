package com.works.solutionchallange2024.service

import com.works.solutionchallange2024.model.retrofit.ActionsHistory
import com.works.solutionchallange2024.model.retrofit.AdvertDetails
import com.works.solutionchallange2024.model.retrofit.AdvertJoinResponse
import com.works.solutionchallange2024.model.retrofit.AdvertList
import com.works.solutionchallange2024.model.retrofit.AdvertPurchases
import com.works.solutionchallange2024.model.retrofit.AdvertSellings

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdvertService {
    //@GET("advert/getAdvertList")
    //fun getAdvertList(): Call<List<AdvertModels.GetAdvertList>>

    //@GET("advert/publicAdverts/{city}")
    //fun getAdvertListByCity(@Path("city") city: String): Call<AdvertModels.GetAdvertListByCityResponse>

    @GET("advert/filteredAdverts")
    fun getLimitedAdvertListByToken() : Call<AdvertList>

    @GET("advert/filteredAdverts")
    fun getAdvertListByCategory(@Query("category") category: String): Call<AdvertList>

    @GET("advert/filteredAdverts")
    fun getAdvertListByTag(@Query("tag") tag : String): Call<AdvertList>

    @GET("advert/viewPublicAdvert/{id}")
    fun getAdvertById(@Path("id") id : String): Call<AdvertDetails>

    @GET("advert/actionsHistory/5")
    fun getLast5Actions() : Call<ActionsHistory> // Bunun modeli yapılacak daha net alınamıyor

    @GET("advert/actionsHistory/{count}")
    fun getLastActionsByCount(@Path("count") count : Int) : Call<ActionsHistory> // Bunun modeli yapılacak

    @POST("advert/join/{advertID}")
    fun joinAdvertRaffle(@Path("advertID") advertId : String) : Call<AdvertJoinResponse>

    @GET("advert/getAdvert")
    fun getAllMySellings() : Call<AdvertSellings>

    @GET("advert/advertStatus/participatedAdverts")
    fun getAllMyPurchases() : Call<AdvertPurchases>

    //@GET("advert/advertDetails/{ilanId}")
    //fun getMySellingDetail() :

    //@POST("advert/create")
    //fun addAdvert(@Body addAdvert: AdvertModels.AddAdvert): Call<AdvertModels.AddAdvertResponse>

    //@POST("advert/{id}")
    //fun deleteAdvert(@Path("id") id: String): Call<AdvertModels.DeleteAdvertResponse>


}