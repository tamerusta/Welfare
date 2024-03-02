package com.works.solutionchallange2024.model.retrofit

import okhttp3.MultipartBody

data class AdvertList(
    val success: Boolean,
    val adverts: List<Advert>
)

data class Advert(
    val participants: List<Any?>? = null,
    val lostParticipants: List<Any?>? = null,
    val drawCompleted: Boolean? = null,
    val _id: String,
    val owner: Owner? = null,
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val city: String,
    val createTime: String? = null,
    val deadTime: String? = null,
    val point: Long? = null,
    val status: String,
    val visibility: String,
    val images: List<String>,
)


data class AdvertDetails(
    val success: Boolean,
    val advert: AdvertDetail
)

data class Owner(
    val _id: String,
    val username: String
)

data class ActionsHistory(
    val success: Boolean,
    val actionsHistory: List<Action>
)

data class Action(
    val type: String,
    val advertId: Advert,
    val timestamp: String
)

data class AdvertJoinResponse(
    val success: Boolean,
    val message: String
)

data class AdvertDetail(
    val _id: String,
    val owner: String,
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val city: String,
    val point: Long,
    val status: String,
    val visibility: String,
    val images: List<String>,
    val drawCompleted: Boolean,
    val location: String,
    val latitude: String,
    val longitude: String,
    val participantCount: Int,
    val minParticipants: Int
)

data class AdvertPurchases(
    val success: Boolean,
    val adverts: List<AdvertBuy>
)

data class AdvertSellings(
    val success: Boolean,
    val adverts: List<AdvertSell>
)

data class AdvertSell(
    val _id: String,
    val owner: String,
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val city: String,
    val createTime: String,
    val deadTime: String,
    val point: Long,
    val status: String,
    val visibility: String,
    val images: List<String>,
    val participants: List<String>,
    val lostParticipants: List<Any?>,
    val minParticipants: Long,
    val drawCompleted: Boolean,
    val location: String,
    val latitude: String,
    val longitude: String,
    val v: Long
)

data class AdvertBuy(
    val _id: String,
    val owner: String,
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val city: String,
    val createTime: String,
    val deadTime: String,
    val point: Long,
    val status: String,
    val visibility: String,
    val images: List<String>,
    val participants: List<String>,
    val lostParticipants: List<Any?>,
    val minParticipants: Long,
    val drawCompleted: Boolean,
    val location: String,
    val latitude: String,
    val longitude: String,
    val v: Long
)

data class AddAdvert(
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val deadTime: String,
    val point: String,
    val status: String,
    val visibility: String,
    val image: List<MultipartBody.Part>, // multipart file
    val city: String,
    val minParticipants: String
)

data class AddAdvertResponse(
    val success: Boolean,
    val message: String,
    val advert: Advert2
)

data class Advert2(
    val _id: String,
    val owner: String,
    val title: String,
    val description: String,
    val category: String,
    val tag: String,
    val city: String,
    val createTime: String,
    val deadTime: String,
    val point: Int,
    val status: String,
    val visibility: String,
    val images: List<String>,
    val __v: Int
)






