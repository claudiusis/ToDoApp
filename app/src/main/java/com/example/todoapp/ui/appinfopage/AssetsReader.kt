package com.example.todoapp.ui.appinfopage

import android.content.Context
import com.example.todoapp.core.IOUtils
import com.yandex.div.core.annotations.InternalApi
import org.json.JSONObject

class AssetsReader(private val context: Context) {

    @OptIn(InternalApi::class)
    fun read(filename : String): JSONObject {
        val data = IOUtils.toString(context.assets.open(filename))
        return JSONObject(data)
    }
}