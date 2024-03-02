package com.works.solutionchallange2024.viewmodel

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.works.solutionchallange2024.config.RetrofitClient
import com.works.solutionchallange2024.model.retrofit.ActionsHistory
import com.works.solutionchallange2024.model.retrofit.Advert
import com.works.solutionchallange2024.model.retrofit.AdvertDetail
import com.works.solutionchallange2024.model.retrofit.AdvertList
import com.works.solutionchallange2024.model.retrofit.TagList
import com.works.solutionchallange2024.model.room.TagDataClass
import com.works.solutionchallange2024.service.AdvertService
import com.works.solutionchallange2024.service.LocalDatabase
import com.works.solutionchallange2024.service.ProductService
import com.works.solutionchallange2024.service.TagsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageViewModel : ViewModel() {
    private val retrofit = RetrofitClient
    private val fetchProductApi = retrofit.getClient().create(ProductService::class.java)
    private val fetchAdvertApi = retrofit.getClient().create(AdvertService::class.java)

    private val _list = MutableLiveData<List<Advert>>()

    private lateinit var db: LocalDatabase
    private lateinit var dao: TagsDao

    val list: LiveData<List<Advert>> get() = _list

    private val _tagList = MutableLiveData<TagList>()
    val tagList: LiveData<TagList> get() = _tagList

    private val _actionList = MutableLiveData<List<Advert>>()
    val actionList: LiveData<List<Advert>> get() = _actionList


    fun getAllProductTags() {

        fetchProductApi.getAllProductTags().enqueue(object : Callback<TagList>{
            override fun onResponse(call: Call<TagList>, response: Response<TagList>) {
                val data = response.body()
                if (data != null)
                {
                    _tagList.postValue(data!!)

                }
            }

            override fun onFailure(call: Call<TagList>, t: Throwable) {

            }


        })
    }

    fun getLastActionsHistory(){
        fetchAdvertApi.getLast5Actions().enqueue(object : Callback<ActionsHistory>{
            override fun onResponse(
                call: Call<ActionsHistory>,
                response: Response<ActionsHistory>
            ) {
                val data = response.body()
                if (data != null)
                {
                    handleActionsHistory(data)
                }
            }

            override fun onFailure(call: Call<ActionsHistory>, t: Throwable) {
            }
        })
    }

    fun handleActionsHistory(actionsHistory: ActionsHistory) {
        val actionList = mutableListOf<Advert>()
        for (action in actionsHistory.actionsHistory) {
            val advert = action.advertId
            actionList.add(advert)
        }
        _actionList.value = actionList
    }




    fun getProductListByCity() {
        fetchAdvertApi.getLimitedAdvertListByToken().enqueue(object : Callback<AdvertList>{
            override fun onResponse(
                call: Call<AdvertList>,
                response: Response<AdvertList>
            ) {
                val data = response.body()
                if (data != null)
                {
                    _list.postValue(data.adverts)
                }
            }

            override fun onFailure(
                call: Call<AdvertList>,
                t: Throwable
            ) {
            }
        })

    }

    fun getProductListByTag(tag : String) {
        fetchAdvertApi.getAdvertListByTag(tag).enqueue(object : Callback<AdvertList>{
            override fun onResponse(call: Call<AdvertList>, response: Response<AdvertList>) {
                val data = response.body()
                if (data != null)
                {
                    _list.postValue(data.adverts)
                }

            }

            override fun onFailure(call: Call<AdvertList>, t: Throwable) {
            }

        })
    }


    fun getProductListByCategory(category: String) {
        fetchAdvertApi.getAdvertListByCategory(category).enqueue(object : Callback<AdvertList>{
            override fun onResponse(call: Call<AdvertList>, response: Response<AdvertList>) {
                val data = response.body()
                if (data != null)
                {
                    _list.postValue(data.adverts)
                }
            }

            override fun onFailure(call: Call<AdvertList>, t: Throwable) {
            }
        })



    }



    fun initializeDatabase(context: Context) {
        db = LocalDatabase.databaseAccess(context)!!
        dao = db.getTags()
    }

    fun loadTags(adapter: ArrayAdapter<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dao.allTags()
            adapter.clear()
            for (tag in list) {
                adapter.add(tag.tag_name)
            }
        }
    }

    fun searchTags(keyword: String, adapter: ArrayAdapter<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = dao.searchTag("%$keyword%")
            adapter.clear()
            for (tag in list) {
                adapter.add(tag.tag_name)
            }
        }
    }


    fun insertTagList(tagNames: List<String>, dao: TagsDao) {
        viewModelScope.launch {
            tagNames.forEach { tagName ->
                val newTag = TagDataClass(0, tagName)
                dao.addTag(newTag)
            }
        }
    }

    fun deleteAllTag(dao : TagsDao) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            dao.deleteAllTags()
        }
    }

}
