package com.works.solutionchallange2024.viewmodel

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.model.retrofit.Advert
import com.works.solutionchallange2024.model.retrofit.AdvertDetail
import com.works.solutionchallange2024.model.retrofit.AdvertDetails
import com.works.solutionchallange2024.model.retrofit.AdvertJoinResponse
import com.works.solutionchallange2024.service.AdvertService
import com.works.solutionchallange2024.service.LocalDatabase
import com.works.solutionchallange2024.service.ProductService
import com.works.solutionchallange2024.service.TagsDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {

    private val retrofit = RetrofitClient
    private val fetchProductApi = retrofit.getClient().create(ProductService::class.java)
    private val fetchAdvertApi = retrofit.getClient().create(AdvertService::class.java)

    private val _advert = MutableLiveData<AdvertDetail>()
    val advert: LiveData<AdvertDetail> get() = _advert
    private val _success = MutableLiveData<Boolean>()
    val success  : LiveData<Boolean> get() = _success

    val _message = MutableLiveData<String>()
    val message : LiveData<String> get() = _message


    fun getAdvertByDetail(id : String){
        fetchAdvertApi.getAdvertById(id).enqueue(object : Callback<AdvertDetails> {
            override fun onResponse(call: Call<AdvertDetails>, response: Response<AdvertDetails>) {
                val data = response.body()

                if (data != null)
                {
                    _advert.postValue(data.advert)

                }
            }

            override fun onFailure(call: Call<AdvertDetails>, t: Throwable) {
            }
        })
    }

    fun joinAdvertRaffle(id : String){
        fetchAdvertApi.joinAdvertRaffle(id).enqueue(object  : Callback<AdvertJoinResponse>{
            override fun onResponse(
                call: Call<AdvertJoinResponse>,
                response: Response<AdvertJoinResponse>
            ) {
                val data = response.body()
                if (data?.success != null && data.success)
                {
                    _success.postValue(data.success)
                }
                else if(data?.success == false)
                {
                    _message.postValue(data.message)
                }
            }

            override fun onFailure(call: Call<AdvertJoinResponse>, t: Throwable) {

            }
        })
    }
}