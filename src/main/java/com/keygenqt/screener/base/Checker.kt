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

import com.google.common.io.Resources
import com.keygenqt.screener.utils.PATH_APP_CONFIG
import com.keygenqt.screener.utils.PATH_APP_TEMP_DIR
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@Suppress("JAVA_CLASS_ON_COMPANION")
class Checker {
    companion object {

        fun paramsCloud(value: String) {
            if (value.isEmpty()) {
                Info.error("Your need add credentials cloud in config.json (~/snap/screener/common/config.json) \"Cloud credentials path to file\"")
            }
            val cred = File(value)
            if (!cred.exists()) {
                Info.error("File $value not found")
            } else if (cred.isDirectory) {
                Info.error("Credentials path is directory. Need json.")
            }
            val content: String = Resources.toString(cred.toURI().toURL(), StandardCharsets.UTF_8)
            try {
                val json = JSONObject(content)
//                println(json)
            } catch (ex: Exception) {
                Info.error("Credentials file is not json.")
            }
        }

        fun delay(value: String) {
            value.toIntOrNull()?.let {
                if (it > 300) {
                    Info.error("Max value delay - 300 second")
                }
            } ?: run {
                Info.error("Value delay must be a number")
            }
        }

        fun tempDir() {
            val tempDir = File(PATH_APP_TEMP_DIR)
            if (!tempDir.exists()) {
                try {
                    FileUtils.forceMkdir(tempDir)
                } catch (ex: Exception) {
                    Info.error("I can't create temp dir $PATH_APP_TEMP_DIR")
                }
            } else if (tempDir.isFile) {
                Info.error("Path is file: $PATH_APP_TEMP_DIR")
            }
            if (!File(PATH_APP_CONFIG).exists()) {
                val url: URL = Resources.getResource("other/config.json")
                val content: String = Resources.toString(url, StandardCharsets.UTF_8)
                Files.write(Paths.get(PATH_APP_CONFIG), content.toByteArray())
                Info.showInfo("Add default config to: $PATH_APP_CONFIG")
            }

            val screenshot = File(Configuration.getFolder())
            if (!screenshot.exists()) {
                try {
                    FileUtils.forceMkdir(screenshot)
                } catch (ex: Exception) {
                    Info.error("I can't create screenshot dir $PATH_APP_TEMP_DIR")
                }
            }
        }
    }
}