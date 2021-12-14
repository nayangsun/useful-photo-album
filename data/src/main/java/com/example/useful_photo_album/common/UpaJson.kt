package com.example.useful_photo_album.common

import com.example.useful_photo_album.json.Exclude
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

// Useful photo album
object UpaJson {
    private val usefulPhotoAlbumExclusionStrategy = object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
            return false
        }

        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            val exclude = f!!.getAnnotation(Exclude::class.java)
            return exclude != null
        }
    }


    private val internalBuilder = GsonBuilder()
        //.registerTypeAdapterFactory()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .addSerializationExclusionStrategy(usefulPhotoAlbumExclusionStrategy)
        .addDeserializationExclusionStrategy(usefulPhotoAlbumExclusionStrategy)

    private val internalNoUnderscoreBuilder = GsonBuilder()
        //.registerTypeAdapterFactory()
        .addSerializationExclusionStrategy(usefulPhotoAlbumExclusionStrategy)
        .addDeserializationExclusionStrategy(usefulPhotoAlbumExclusionStrategy)



    /**
     * @suppress
     */
    val base: Gson = internalBuilder.create()
    val baseNoUnderscore: Gson = internalNoUnderscoreBuilder.create()

    /**
     * @suppress
     */
    val pretty: Gson = internalBuilder.setPrettyPrinting().create()

    fun <T> listFromJson(string: String, type: Class<T>): List<T> =
        base.fromJson(string, TypeToken.getParameterized(List::class.java, type).type)

    fun <T> parameterizedFromJson(string: String, type1: Type, type2: Type): T =
        base.fromJson(string, TypeToken.getParameterized(type1, type2).type)


    fun <T> toJson(model: T): String = base.toJson(model)
    fun <T> fromJson(string: String, type1: Type): T = base.fromJson(string, type1)
}