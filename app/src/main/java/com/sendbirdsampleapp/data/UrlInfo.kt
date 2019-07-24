package com.sendbirdsampleapp.data

import org.json.JSONObject

class UrlInfo {

    lateinit var url: String
    lateinit var siteName: String
    lateinit var title: String
    lateinit var description: String
    lateinit var imageUrl: String

    fun setUrlInfo(jsonString: String) {
        val obj = JSONObject(jsonString)
        url = obj.getString("url")
        siteName = obj.getString("site_name")
        title = obj.getString("title")
        description = obj.getString("description")
        imageUrl = obj.getString("image")
    }

    fun toJsonString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("url", url)
        jsonObject.put("site_name", siteName )
        jsonObject.put("title", title )
        jsonObject.put("description", description )
        jsonObject.put("image", imageUrl)

        return jsonObject.toString()
    }
}