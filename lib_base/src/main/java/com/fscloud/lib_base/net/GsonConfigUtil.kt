package com.fscloud.lib_base.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author shaojunpei
 * @date 2020/12/11 13:54
 * @describe
 */

object GsonConfigUtil {

    fun createGson(): Gson{
        val builder = GsonBuilder()
        builder.registerTypeAdapter(Int::class.java,IntDefaultAdapter())
        builder.registerTypeAdapter(String::class.java,StringDefaultAdapter())
        builder.registerTypeAdapter(Boolean::class.java,BooleanDefaultAdapter())
        return builder.create()
    }

}

class IntDefaultAdapter: TypeAdapter<Int>() {
    override fun write(jsonWriter: JsonWriter, value: Int?) {
        jsonWriter.value(value)
    }

    override fun read(jsonReader: JsonReader): Int {
        return if (jsonReader.peek() == JsonToken.NULL){
            jsonReader.nextNull()
            -1
        }else {
            jsonReader.nextInt()
        }
    }
}

class StringDefaultAdapter: TypeAdapter<String>() {
    override fun write(jsonWriter: JsonWriter, value: String?)  {
        jsonWriter.value(value)
    }

    override fun read(jsonReader: JsonReader): String {
        return if (jsonReader.peek() == JsonToken.NULL){
            jsonReader.nextNull()
            ""
        }else {
            jsonReader.nextString()
        }
    }
}

class BooleanDefaultAdapter: TypeAdapter<Boolean>() {
    override fun write(jsonWriter: JsonWriter, value: Boolean?)  {
        jsonWriter.value(value)
    }

    override fun read(jsonReader: JsonReader): Boolean {
        return if (jsonReader.peek() == JsonToken.NULL){
            jsonReader.nextNull()
            false
        }else {
            jsonReader.nextBoolean()
        }
    }
}