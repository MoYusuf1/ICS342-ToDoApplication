package com.example.to_do.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.*
import java.lang.reflect.Type

object RetrofitInstance {

    private const val BASE_URL = "https://todos.simpleapi.dev/"


    class BooleanDeserializer : JsonDeserializer<Boolean> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
            return when {
                json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> json.asInt != 0
                json.isJsonPrimitive && json.asJsonPrimitive.isBoolean -> json.asBoolean
                else -> throw JsonParseException("Invalid boolean type")
            }
        }
    }


    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
