package com.sendbirdsampleapp.util

import android.util.Patterns
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.sendbirdsampleapp.data.UrlInfo

/*Copyright 2013 Leonardo Cardoso

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

object UrlUtil {

    private val textCrawler = TextCrawler()

    fun extractUrl(text: String): ArrayList<String> {
        val result = ArrayList<String>()

        val words = text.split("\\s+")
        val pattern = Patterns.WEB_URL

        for (word in words) {
            var url = word
            if (pattern.matcher(word).find()) {
                if (!word.toLowerCase().contains("http://") && !word.toLowerCase().contains("https://"))
                    url = "http://$word"
                result.add(url)
            }
        }
        return result
    }

    fun generateLinkPreviewCallback(url: String, linkPreviewCallback: LinkPreviewCallback) {
        textCrawler.makePreview(linkPreviewCallback, url)
    }

    fun parseContent(sourceContent: SourceContent): UrlInfo {

        val urlInfo = UrlInfo()
        urlInfo.title = sourceContent.title.toString()
        if (urlInfo.title == "Twitter") {
            urlInfo.imageUrl = sourceContent.images?.get(7).toString()
        } else {
            urlInfo.imageUrl = sourceContent.images?.get(0).toString()
        }
        urlInfo.description = sourceContent.description.toString()
        urlInfo.url = sourceContent.url.toString()
        val siteName = sourceContent.cannonicalUrl?.replace("www.", "")
        urlInfo.siteName = siteName?.replace(".com", "").toString()

        return urlInfo
    }



    fun onDestroy() {
        textCrawler.cancel()
    }


}