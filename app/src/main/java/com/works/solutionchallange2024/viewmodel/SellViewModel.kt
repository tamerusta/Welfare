package com.works.solutionchallange2024.viewmodel

import androidx.lifecycle.ViewModel
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.service.AdvertService

class SellViewModel  : ViewModel()
{
    val retrofit = RetrofitClient
    private val fetchAdvertApi = retrofit.getClient().create(AdvertService::class.java)

}