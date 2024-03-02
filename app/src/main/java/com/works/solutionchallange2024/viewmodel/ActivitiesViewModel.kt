package com.works.solutionchallange2024.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.model.retrofit.Advert
import com.works.solutionchallange2024.model.retrofit.AdvertBuy
import com.works.solutionchallange2024.model.retrofit.AdvertPurchases
import com.works.solutionchallange2024.model.retrofit.AdvertSell
import com.works.solutionchallange2024.model.retrofit.AdvertSellings
import com.works.solutionchallange2024.service.AdvertService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitiesViewModel: ViewModel() {

    private val retrofit = RetrofitClient
    private val fetchAdvertApi = retrofit.getClient().create(AdvertService::class.java)

    private val _list = MutableLiveData<List<Advert>>()
    val list: LiveData<List<Advert>> get() = _list

    private val _isPurchase = MutableLiveData<Boolean>()
    val isPurchase : LiveData<Boolean> get() = _isPurchase

    private val _isSelling = MutableLiveData<Boolean>()
    val isSelling : LiveData<Boolean> get() = _isSelling

    fun fetchAllMySellings(){
        fetchAdvertApi.getAllMySellings().enqueue(object : Callback<AdvertSellings>{
            override fun onResponse(
                call: Call<AdvertSellings>,
                response: Response<AdvertSellings>
            ) {
                val data = response.body()
                if (data != null)
                {
                    _isPurchase.postValue(false)
                    _isSelling.postValue(true)
                    _list.postValue(convertAdvertSellsToAdverts(data.adverts))
                }
            }
            override fun onFailure(call: Call<AdvertSellings>, t: Throwable) {
            }
        })
    }

    fun convertAdvertSellsToAdverts(advertSells: List<AdvertSell>): List<Advert> {
        val adverts = mutableListOf<Advert>()
        for (advertSell in advertSells) {
            val advert = Advert(
                _id = advertSell._id,
                owner = null, // Burayı doldurmanız gerekebilir, eğer mümkünse owner verisi mevcutsa doldurun.
                title = advertSell.title,
                description = advertSell.description,
                category = advertSell.category,
                tag = advertSell.tag,
                city = advertSell.city,
                createTime = advertSell.createTime,
                deadTime = advertSell.deadTime,
                point = advertSell.point,
                status = advertSell.status,
                visibility = advertSell.visibility,
                images = advertSell.images
            )
            adverts.add(advert)
        }
        return adverts
    }

    fun fetchAllMyPurchases(){
        fetchAdvertApi.getAllMyPurchases().enqueue(object : Callback<AdvertPurchases>{
            override fun onResponse(
                call: Call<AdvertPurchases>,
                response: Response<AdvertPurchases>
            ) {
                val data = response.body()
                if (data != null)
                {
                    _isPurchase.postValue(true)
                    _isSelling.postValue(false)
                    _list.postValue(convertAdvertBuysToAdverts(data.adverts))
                }
            }

            override fun onFailure(call: Call<AdvertPurchases>, t: Throwable) {
            }
        })
    }

    fun convertAdvertBuysToAdverts(advertBuys: List<AdvertBuy>): List<Advert> {
        val adverts = mutableListOf<Advert>()
        for (advertBuy in advertBuys) {
            val advert = Advert(
                _id = advertBuy._id,
                owner = null,
                title = advertBuy.title,
                description = advertBuy.description,
                category = advertBuy.category,
                tag = advertBuy.tag,
                city = advertBuy.city,
                createTime = advertBuy.createTime,
                deadTime = advertBuy.deadTime,
                point = advertBuy.point,
                status = advertBuy.status,
                visibility = advertBuy.visibility,
                images = advertBuy.images
            )
            adverts.add(advert)
        }
        return adverts
    }

}