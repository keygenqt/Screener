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

import com.keygenqt.screener.utils.PATH_APP_TEMP_DIR
import org.apache.commons.io.FileUtils
import java.io.File

class Checker {
    companion object {

        fun tempDir() {

            val tempDir = File(PATH_APP_TEMP_DIR)

            if (!tempDir.exists()) {
                try {
                    FileUtils.forceMkdir(tempDir)
                } catch (ex: Exception) {
                    Info.errorTempDir()
                }
            } else if (tempDir.isFile) {
                Info.errorTempDir()
            }
        }
    }
}