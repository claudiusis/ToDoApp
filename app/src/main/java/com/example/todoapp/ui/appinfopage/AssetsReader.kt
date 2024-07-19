package com.example.todoapp.ui.appinfopage

import android.content.Context
import com.example.todoapp.core.IOUtils
import org.json.JSONObject

class AssetsReader(private val context: Context) {

    fun read(filename : String): JSONObject {
        val data = IOUtils.toString(context.assets.open(filename))
        return JSONObject(data)
    }
}