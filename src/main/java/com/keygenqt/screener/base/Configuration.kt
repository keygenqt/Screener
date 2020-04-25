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

package com.keygenqt.screener.base

import com.keygenqt.screener.utils.PATH_APP_CONFIG
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class Configuration {
    companion object {

        private var FOLDER_PATH_FOR_SAVE_SCREENSHOT = "Folder path for save screenshot"
        private var CLOUD_CREDENTIALS_PATH_TO_FILE = "Cloud credentials path to file"
        private var CLOUD_TRANSLATE_LANGUAGE =
            "Cloud translate language" // https://cloud.google.com/translate/docs/languages
        private var UPLOAD_SCREENSHOT_TO_IMGUR = "Upload screenshot to Imgur"

        private var PARAMS = hashMapOf(
            FOLDER_PATH_FOR_SAVE_SCREENSHOT to "",
            CLOUD_CREDENTIALS_PATH_TO_FILE to "",
            UPLOAD_SCREENSHOT_TO_IMGUR to "true"
        )

        private fun checkParam(key: String): String {
            val f = File(PATH_APP_CONFIG)
            if (f.exists() && f.isFile) {
                try {
                    val params = JSONObject(BufferedReader(FileReader(f)).readText())
                    if (params.has(key)) {
                        when (val value = params.get(key)) {
                            is String -> {
                                PARAMS[key] = value
                            }
                            is JSONObject -> {
                                PARAMS[key] = value.toString()
                            }
                            is JSONArray -> {
                                PARAMS[key] = value.toString()
                            }
                        }
                    }
                } catch (ex: JSONException) {
                    println("Error parse config $key")
                    println(ex)
                }
            }
            return PARAMS[key] ?: ""
        }

        fun getFolder(): String {
            val path = checkParam(FOLDER_PATH_FOR_SAVE_SCREENSHOT)
            if (path.contains("{HOME}")) {
                return path.replace("{HOME}", System.getProperty("user.home"))
            }
            return checkParam(FOLDER_PATH_FOR_SAVE_SCREENSHOT)
        }

        fun getCredentials(): String {
            return checkParam(CLOUD_CREDENTIALS_PATH_TO_FILE)
        }

        fun getLanguage(): String {
            return checkParam(CLOUD_TRANSLATE_LANGUAGE)
        }

        fun isImgur(): Boolean {
            return checkParam(UPLOAD_SCREENSHOT_TO_IMGUR) == "true"
        }
    }
}