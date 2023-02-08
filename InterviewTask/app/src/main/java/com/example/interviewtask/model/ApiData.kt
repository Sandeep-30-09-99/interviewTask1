package com.example.interviewtask.model


data class AData(
    val a: ArrayList<Data>
)

data class ApiData(
    val list: List<Data>,
    val message: String,
    val success: Boolean
)