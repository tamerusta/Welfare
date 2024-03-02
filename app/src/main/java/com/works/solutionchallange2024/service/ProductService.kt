package com.works.solutionchallange2024.service

import com.works.solutionchallange2024.model.retrofit.ProductData
import com.works.solutionchallange2024.model.retrofit.TagList
import com.works.solutionchallange2024.model.retrofit.UpdateProductData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface   ProductService {
    @GET("product/getProductById/{id}")
    fun getProductById(@Path("id") id: Int): Call<ProductData>

    @GET("product/getProductByCity/{city}")
    fun getProductListByCity(@Path("city") city: String): Call<List<ProductData>>

    @GET("product/getProductByCategoryAndTag/category/{category}/tag/{tag}")
    fun getProductListByCategoryAndTag(@Path("category") category: String, @Path("tag") tag: String): Call<List<ProductData>>

    @GET("product/getProductByCategory/category/{category}")
    fun getProductListByCategory(@Path("category") category: String): Call<List<ProductData>>

    @GET("product/getProductByTag/tag/{tag}")
    fun getProductListByTag(@Path("tag") tag: String): Call<List<ProductData>>

    @GET("product/getProductByCategoryAndCity/category/{category}/city/{city}")
    fun getProductListByCategoryAndCity(@Path("category") category: String, @Path("city") city: String): Call<List<ProductData>>

    @PUT("product/productUpdate/{id}")
    fun updateProductById(@Path("id") id: Int, @Body newProductData: UpdateProductData): Call<ProductData>

    @GET("categories/")
    fun getAllProductTags() : Call<TagList>
}