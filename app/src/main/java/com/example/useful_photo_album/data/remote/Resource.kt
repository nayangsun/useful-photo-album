package com.example.useful_photo_album.data.remote

import com.example.useful_photo_album.data.remote.Status.SUCCESS
import com.example.useful_photo_album.data.remote.Status.ERROR
import com.example.useful_photo_album.data.remote.Status.LOADING

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
