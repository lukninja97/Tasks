package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface APIListener<T> {

    fun onSuceccess(model: T)

    fun onFailure(str: String)
}