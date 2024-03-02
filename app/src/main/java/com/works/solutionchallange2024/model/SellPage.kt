package com.works.solutionchallange2024.model


data class SellPage(
    var id: Int?,
    var productName: String?,
    var adStatus: String?,
    var adress: String?,
    var imageList: ArrayList<String>,
    var description: String?,
    var category: String?,
)