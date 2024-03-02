# Welfare - Dont waste it, share it.</p>

## Table of Contents
|   |   |
|---|---|
| [1. Overview](#overview) | [6. Actions](#actions) |
| [2. Technologies](#technologies) | [7. Profile](#profile) |
| [3. Features](#features) | [8. Permissions](#permissions) |
| [4. Authentication](#authentication) | [9. Dependencies](#dependencies) |
| [5. Homepage](#homepage) | [10. Demo](#demo) |


  <img src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/815aa0c0-1b3b-4723-906f-f3eb2f6e0164" alt="SC logo design">




## Overview

Welfare is an application designed as a money-free sharing platform, facilitating the exchange of unused items among individuals in need. Users can offer their surplus items and also discover items they require, fostering a sustainable approach to resource utilization. By promoting the exchange of second-hand goods using application points, the app contributes to environmental protection, waste reduction, and community sharing. Our project participated in the GDSC Solution Challenge 2024, aiming to address the United Nations' Sustainable Development Goals (SDGs), specifically targeting **No Poverty** and **Responsible Consumption & Production**.



<p align="center">
  <img src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/3e997838-09bc-4829-9220-f5ebb2cc71da" alt="Solution Challenge">
</p>


## Technologies

| Technology | Description |
|------------|-------------|
| **Google Maps** | Integration for location-based services. |
| **Retrofit** | Used for handling RESTful API requests. |
| **Room** | Android SQLite database library for local data storage. |
| **MVVM Architecture** | Adopted to ensure a clear separation of concerns and facilitate maintainability and scalability. |

## Features

1. **Compatibility with Our Website:** We ensure seamless login and registration processes that are compatible with our [website](http://app.welfare.ws). It's essential to capture your real-time location during registration. Products near your location will be displayed on the homepage to facilitate easy shopping. We aim to make product browsing easier by allowing you to filter products by tags such as "Phone, Dress, Guitar." You can preview all categories on a separate screen by clicking on the category selection icon at the top left.

2. **Interactive Homepage:** Upon successful login, products available for lottery based on your city will be listed on the homepage. You can also check the status of your recent lottery entries. Additionally, you can search for products by tags, and by selecting a category, you can view products in that category.

3. **Product Details Page:** When viewing a product, you can see its description, the cost in points needed to participate in the lottery, the current number of participants, the minimum number of participants required for the lottery to start, the seller's rating, and the detailed Google Maps location. You can choose to participate in the lottery or add the product to your favorites without participating.

4. **Actions Section:** In this section, you can view all lottery entries you've participated in and their statuses. If you're a seller, you can also view a list of all your sales, along with their statuses. You can add new products by clicking on the plus icon at the bottom.

5. **Product Management:** When adding a new product, you can specify its type and category by selecting predefined tags. You can then write the product description. After selecting product photos from your phone's gallery, the product's location and address will be automatically determined using the phone's location data.

6. **Profile Section:** Here, you can view your profile information, including your average seller rating. You can also update your information, change your password, and update your location. Additionally, you can view and manage your favorites list and donations.

7. **Donations:** In the Donations section, brands can display special product campaigns where users can donate products in bulk. We plan to incorporate this feature into the homepage as a campaign in the future.

8. **Logout:** Finally, you can log out of the application by clicking on the logout button.

## Authentication



<div style="display: flex; flex-wrap: wrap;">
    <img width="200" alt="welfare1" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/670d2a6c-ad02-43c2-9526-16d84424edb0">
    <img width="200" alt="welfare2" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/90134abf-45c4-4f64-8011-a14203dcfff4">
    <img width="200" alt="welfare3" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/90f5ec40-3852-46d4-87c6-021a5f4b6c8e">
    <img width="200" alt="welfare4" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/e0ccf630-a467-48c0-9cb9-d7713944470c">
</div>

## Homepage


<div style="display: flex; flex-wrap: wrap;">
    <img width="200" alt="welfare5_1" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/ab99a31a-62e4-4d63-b1ef-8b36c7b87b57">
    <img width="200" alt="welfare5_2" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/a2a909c7-7d5b-4604-94a3-e58b36df1b57">
    <img width="200" alt="welfare5_3" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/6217bcd5-5d1e-4e71-8b5f-2235c1f83bc0">
    <img width="200" alt="welfare5_4" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/0f552d60-63a3-4df6-be08-f93abea71556">
</div>

## Actions

<div style="display: flex; flex-wrap: wrap;">

  <img width="200" alt="welfare6 1" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/3674ca97-83d9-45a3-bd5b-a35467c75c3f">
  <img width="200" alt="welfare6 2" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/cfb47316-dc87-4317-9064-912a79c46e75">
  <img width="200" alt="welfare6 3" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/aa317b9b-13c5-4d44-b44b-3f6cc37303cc">
  <img width="200" alt="welfare6 4" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/361412ad-5745-4771-ad0b-8a8c69729218">
</div>

## Profile

<div style="display: flex; flex-wrap: wrap;">
      <img width="200" alt="welfare7 1" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/9a8622a4-bc75-4a79-8fb8-4c532597dc36">
      <img width="200" alt="welfare7 2" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/e61b0035-33bc-4278-a90e-3e68b23a1529">
      <img width="200" alt="welfare7 3" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/174e2a7b-bea3-4bea-92d2-fa06f3220c19">
      <img width="205" alt="welfare7 4" src="https://github.com/gdsckou/Solution-Challange-Kotlin/assets/152609338/67a757c3-a58a-492a-9005-31afb8a23da9">
</div>


## Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" /> 
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

```
  
## Dependencies

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    val  room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:18.0.0")
}
```


## Demo

[Click here for our demo video](https://www.youtube.com/watch?v=oYDCdbKiqoI)












