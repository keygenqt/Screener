/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.screener.components.uploads

import com.keygenqt.screener.utils.IMGUR_CLIENT_ID
import okhttp3.*
import org.json.JSONObject
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import javax.xml.bind.DatatypeConverter

class UploadImgurImage {
    companion object {
        fun upload(path: String): String {

            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val url = HttpUrl.Builder()
                .scheme("https")
                .host("api.imgur.com")
                .addPathSegment("3")
                .addPathSegment("image")
                .build()

            val build = Request.Builder()
                .addHeader("Authorization", "Client-ID $IMGUR_CLIENT_ID")
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .url(url)

            val base64 = DatatypeConverter.printBase64Binary(
                Files.readAllBytes(
                    Paths.get(path)
                )
            )

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", base64)
                .build()

            build.post(requestBody)

            val response = client.newCall(build.build()).execute()
            val body = response.body?.string()
            val jsonObj = JSONObject(body)

            return jsonObj.getJSONObject("data").get("link").toString()
        }
    }
}