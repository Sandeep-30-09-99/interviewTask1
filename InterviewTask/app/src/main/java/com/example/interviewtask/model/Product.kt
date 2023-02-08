package com.example.interviewtask.model

/*Class = Product
Fields
id
name
description
regular price
sale price
product photo (image)
colors (array)
stores (dictionary)*/

data class Product(
    val id: Int?,
    val name: String,
    val description: String,
    val regular_price: Float,
    val sale_price: Float,
    val product_photo: String,
    val color : Array<Int>
)
